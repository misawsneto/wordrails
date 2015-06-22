package com.wordrails;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.LobCreator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.wordrails.api.Link;
import com.wordrails.api.PersonData;
import com.wordrails.api.PerspectiveResource;
import com.wordrails.api.StationDto;
import com.wordrails.api.StationPermission;
import com.wordrails.api.StationsPermissions;
import com.wordrails.api.TermPerspectiveView;
import com.wordrails.business.File;
import com.wordrails.business.FileContents;
import com.wordrails.business.Image;
import com.wordrails.business.Network;
import com.wordrails.business.PasswordReset;
import com.wordrails.business.Person;
import com.wordrails.business.Post;
import com.wordrails.business.PostRead;
import com.wordrails.business.Station;
import com.wordrails.business.StationRole;
import com.wordrails.business.Term;
import com.wordrails.converter.PostConverter;
import com.wordrails.persistence.FileContentsRepository;
import com.wordrails.persistence.FileRepository;
import com.wordrails.persistence.ImageRepository;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.PostReadRepository;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.QueryPersistence;
import com.wordrails.persistence.StationRepository;
import com.wordrails.persistence.StationRolesRepository;
import com.wordrails.services.CacheService;
import com.wordrails.services.WordpressParsedContent;
import com.wordrails.util.WordrailsUtil;

@Component
public class WordrailsService {

	private @Autowired NetworkRepository networkRepository;
	private @PersistenceContext EntityManager manager;
	private @Autowired PostReadRepository postReadRepository;
	private @Autowired QueryPersistence queryPersistence;
	private @Autowired FileContentsRepository contentsRepository;
	private @Autowired FileRepository fileRepository;
	private @Autowired ImageRepository imageRepository;
	private @Autowired PostRepository postRepository;
	private @Autowired PerspectiveResource perspectiveResource;
	private @Autowired PostConverter postConverter;
	private @Autowired CacheService cacheService;
	private @Autowired StationRepository stationRepository;
	private @Autowired StationRolesRepository stationRolesRepository;
	public @Autowired @Qualifier("objectMapper") ObjectMapper mapper;
	
	private LoadingCache<PermissionId, StationsPermissions> stationsPermissions;
	
	public void init(){
		// ------------- init person cache
		stationsPermissions = CacheBuilder.newBuilder().maximumSize(1000)
				.expireAfterWrite(1, TimeUnit.MINUTES)
				//	       .removalListener(MY_LISTENER)
				.build(
						new CacheLoader<PermissionId, StationsPermissions>() {
							public StationsPermissions load(PermissionId id) {
								List<StationPermission> permissions = getStationPermissions(id.baseUrl, id.personId, id.networkId);
								if(permissions != null){
									StationsPermissions stationsPermissions = new StationsPermissions();
									stationsPermissions.stationPermissionDtos = permissions;
									return stationsPermissions;
								}else{
									return null;
								}
							}
						});
	}
	
	public StationsPermissions getPersonPermissions(PermissionId id) throws ExecutionException{
		return stationsPermissions.get(id);
	}

	public List<Link> generateSelfLinks(String self){
		Link link = new Link();
		link.href = self;
		link.rel = "self";
		return Arrays.asList(link);
	} 

	public Network getNetworkFromHost(ServletRequest srq){
		String host = ((HttpServletRequest) srq).getHeader("Host");

		List<Network> networks = new ArrayList<Network>();

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			networks = networkRepository.findAll();
		}else{
			String[] names = host.split("\\.");
			String topDomain = names[names.length - 2] + "." + names[names.length - 1];
			String subdomain = !topDomain.equals(host) ? host.split("." + topDomain)[0] : null;
			if(subdomain != null && !subdomain.isEmpty()){
				Network network = null;
				try {
					network = cacheService.getNetworkBySubdomain(subdomain);
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				if(network != null)
					networks.add(network); 
			}
		}

		if(networks == null || networks.size() == 0){
			networks = networkRepository.findByDomain(host);
		}

		Network network = (networks != null && networks.size() > 0) ? networks.get(0) : networkRepository.findOne(1);
		return network;
	}

	@Async
	@Transactional
	public void countPostRead(Post post, Person person, String sessionId){
		PostRead postRead = new PostRead();
		postRead.person = person;
		postRead.post = post;
		if(postRead.person != null && postRead.person.username.equals("wordrails")) // if user wordrails, include session to uniquely identify the user.
			postRead.sessionid = sessionId;
		try {
			postReadRepository.save(postRead);
			queryPersistence.incrementReadsCount(post.id);
		} catch (org.springframework.dao.DataIntegrityViolationException ex) {
//			ex.printStackTrace();
		}
	}

	@Async
	@Transactional
	public void countPostRead(Integer postId, Person person, String sessionId){
		PostRead postRead = new PostRead();
		postRead.person = person;
		postRead.post = postRepository.findOne(postId);
		if(postRead.person != null && postRead.person.username.equals("wordrails")) // if user wordrails, include session to uniquely identify the user.
			postRead.sessionid = sessionId;
		try {
			postReadRepository.save(postRead);
			queryPersistence.incrementReadsCount(postId);
		} catch (org.springframework.dao.DataIntegrityViolationException ex) {
//			ex.printStackTrace();
		}
	}

	@Async
	@org.springframework.transaction.annotation.Transactional(readOnly=true)
	public void sendResetEmail(PasswordReset passwordReset) {

	}

	@Transactional
	public WordpressParsedContent extractImageFromContent(String content){
		if(content == null || content.isEmpty()){
			content = "";
		}
		Document doc = Jsoup.parse(content);
		// Get all img tags
		String featuredImageUrl = null;
		Elements imgs = doc.getElementsByTag("img");

		WordpressParsedContent wpc = new WordpressParsedContent();

		com.wordrails.business.File file = null;

		try{
			for (Element element : imgs) {
				String imageURL = element.attr("src");
				if(imageURL != null && !imageURL.isEmpty()){
					URL url;
					try {
						url = new URL(imageURL);
						try(InputStream is = url.openStream()){
							try(ImageInputStream in = ImageIO.createImageInputStream(is)){
								final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
								if (readers.hasNext()) {
									ImageReader reader = readers.next();
									try {
										reader.setInput(in);
										int dimensions = reader.getWidth(0) * reader.getHeight(0);
										if(dimensions > 250000){
											Element parent = element.parent();
											if(parent != null && parent.tagName().equals("a")){
												parent.remove();
											}
											featuredImageUrl = imageURL;

											String fileName = FilenameUtils.getBaseName(featuredImageUrl);
											String fileFormat = FilenameUtils.getExtension(featuredImageUrl);
											String tempFileName = fileName + WordrailsUtil.generateRandomString(5, "Aa#") + "." + fileFormat;

											BufferedImage image = null;
											image = ImageIO.read(url);
											java.io.File dataFile = java.io.File.createTempFile(tempFileName, "." + fileFormat);
											ImageIO.write(image, fileFormat, dataFile);

											try(InputStream fileIS = new FileInputStream(dataFile)){
												file = new File();
												file.type = File.INTERNAL_FILE;
												file.mime = file.mime == null || file.mime.isEmpty() ? new Tika().detect(fileIS) : file.mime;    
												file.name = FilenameUtils.getBaseName(featuredImageUrl) + "." + FilenameUtils.getExtension(featuredImageUrl);
												fileRepository.save(file);
												Integer id = file.id;

												Session session = (Session) manager.getDelegate();
												LobCreator creator = Hibernate.getLobCreator(session);
												FileContents contents = contentsRepository.findOne(id);

												contents.contents = creator.createBlob(FileUtils.readFileToByteArray(dataFile));
												contentsRepository.save(contents);

												Image img = new Image();
												img.original = file;
												createImages(img);
												imageRepository.save(img);
												wpc.image = img;
											};

											break;
										}
									} finally {
										reader.dispose();
									}
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}catch (IOException e) {
							e.printStackTrace();
						}
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		wpc.content = doc.select("body").html();
		wpc.externalImageUrl = featuredImageUrl;
		wpc.content = wpc.content.replaceAll("\\[(.*?)\\](.*?)\\[/(.*?)\\]", "");
		wpc.content = wpc.content.trim();

		return wpc;
	}

	private void createImages(Image image) throws SQLException, IOException {
		com.wordrails.business.File original = image.original;

		String format = original.mime == null || original.mime.isEmpty() ? null : original.mime.split("image\\/").length == 2 ? original.mime.split("image\\/")[1] : null;

		if (original != null) {
			com.wordrails.business.File small = new File();
			small.type = File.INTERNAL_FILE; 
			small.mime = image.original.mime != null ? image.original.mime : MIME;
			small.name = original.name;			
			fileRepository.save(small);

			com.wordrails.business.File medium = new File();
			medium.type = File.INTERNAL_FILE;
			medium.mime = image.original.mime != null ? image.original.mime : MIME;
			medium.name = original.name;			
			fileRepository.save(medium);

			com.wordrails.business.File large = new File();
			large.type = File.INTERNAL_FILE;
			large.mime = image.original.mime != null ? image.original.mime : MIME;
			large.name = original.name;
			fileRepository.save(large);

			image.small = small;
			image.medium = medium;
			image.large = large;

			BufferedImage bufferedImage;
			FileContents contents = contentsRepository.findOne(original.id);
			try (InputStream input = contents.contents.getBinaryStream()) {
				bufferedImage = ImageIO.read(input);
			}
			image.vertical = bufferedImage.getHeight() > bufferedImage.getWidth();
			updateContents(small.id, bufferedImage, 150, format);
			updateContents(medium.id, bufferedImage, 300, format);
			updateContents(large.id, bufferedImage, 1024, format);
		}
	}

	private static final String MIME = "image/jpeg";
	private static final String FORMAT = "jpg";
	private static final double QUALITY = 1;

	private void updateContents(Integer id, BufferedImage image, int size, String format) throws IOException {
		format = (format != null  ? format : FORMAT);
		java.io.File file = java.io.File.createTempFile("image", "." + format);
		try {
			Thumbnails.of(image).size(size, size).outputFormat(format).outputQuality(QUALITY).toFile(file);			
			Session session = (Session) manager.getDelegate();
			LobCreator creator = Hibernate.getLobCreator(session);
			FileContents contents = contentsRepository.findOne(id);
			contents.contents = creator.createBlob(FileUtils.readFileToByteArray(file));
			contentsRepository.save(contents);
		} finally {
			file.delete();
		}
	}

	public WordpressParsedContent extractImageFromContent(String body, String externalFeaturedImgUrl) {
		Pattern urlPattern = Pattern.compile("[^(http\\:\\/\\/[a-zA-Z0-9_\\-]+(?:\\.[a-zA-Z0-9_\\-]+)*\\.[a-zA-Z]{2,4}(?:\\/[a-zA-Z0-9_]+)*(?:\\/[a-zA-Z0-9_]+\\.[a-zA-Z]{2,4}(?:\\?[a-zA-Z0-9_]+\\=[a-zA-Z0-9_]+)?)?(?:\\&[a-zA-Z0-9_]+\\=[a-zA-Z0-9_]+)*)$]", Pattern.CASE_INSENSITIVE);
		if(externalFeaturedImgUrl != null && urlPattern.matcher(externalFeaturedImgUrl).matches()){
			body = "<img src=\" + "+ externalFeaturedImgUrl +"\">" + body;
		}
		return extractImageFromContent(body);
	}

	public StationDto getDefaultStation(PersonData personData, Integer currentStationId){
		List<StationPermission> stationPermissions = personData.personPermissions.stationPermissions;

		Integer stationId = 0;

		if(stationPermissions == null)
			return null;

		for (StationPermission stationPermission : stationPermissions) {
			if((currentStationId != null && currentStationId == stationPermission.stationId) || stationPermission.main)
				stationId = stationPermission.stationId;
		}

		if(stationId == 0)
			for (StationPermission stationPermission : stationPermissions) {
				if(stationPermission.visibility.equals(Station.UNRESTRICTED))
					stationId = stationPermission.stationId;
			}

		if(stationId == 0)
			for (StationPermission stationPermission : stationPermissions) {
				if(stationPermission.visibility.equals(Station.RESTRICTED_TO_NETWORKS))
					stationId = stationPermission.stationId;
			}

		if(stationId == 0)
			for (StationPermission stationPermission : stationPermissions) {
				if(stationPermission.visibility.equals(Station.RESTRICTED))
					stationId = stationPermission.stationId;
			}

		for (StationDto station : personData.stations) {
			if(stationId == station.id){
				return station;
			}
		}

		return null;
	}

	public TermPerspectiveView getDefaultPerspective(Integer stationPerspectiveId, int size) {
		return perspectiveResource.getTermPerspectiveView(null, null, stationPerspectiveId, 0, size);
	}

	public Integer getStationIdFromCookie(HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		if(cookies != null)
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("stationId")){
					return Integer.parseInt(cookie.getValue());
				}
			}
		return null;
	}

	public List<Term> createTermTree(List<Term> allTerms){
		List<Term> roots = getRootTerms(allTerms);
		for (Term term : roots) {
			getChilds(term, allTerms);
		}
		return roots;
	}

	public Set<Term> getChilds(Term parent, List<Term> allTerms){
		cleanTerm(parent);
		parent.children = new HashSet<Term>();
		for (Term term : allTerms) {
			if(term.parent != null && parent.id.equals(term.parent.id)){
				parent.children.add(term);
			}
		}

		for (Term term: parent.children) {
			getChilds(term, allTerms);
		}

		return parent.children;
	}

	private List<Term> getRootTerms(List<Term> allTerms){
		List<Term> roots = new ArrayList<Term>();
		for (Term term : allTerms) {
			if(term.parent == null){
				roots.add(term);
			}
		}
		return roots;
	}

	private void cleanTerm(Term term){
		term.posts = null;
		term.rows = null;
		term.termPerspectives = null;
		term.cells = null;
		term.taxonomy = null;
	}
	
	public List<StationPermission> getStationPermissions(String baseUrl, Integer personId, Integer networkId) {
		return getStationPermissions(baseUrl, personId, networkId, null);
	}

	public List<StationPermission> getStationPermissions(String baseUrl, Integer personId, Integer networkId, List<StationDto> stationDtos) {
		List<Station> stations = new ArrayList<Station>();
		//Stations Permissions
		List<StationPermission> stationPermissionDtos = new ArrayList<StationPermission>();
		try {
			stations = stationRepository.findByPersonIdAndNetworkId(personId, networkId);
			for (Station station : stations) {
				StationPermission stationPermissionDto = new StationPermission();
				StationDto stationDto = new StationDto();
				stationDto.links = generateSelfLinks(baseUrl + "/api/stations/" + station.id);

				//Station Fields
				stationPermissionDto.stationId = station.id;
				stationPermissionDto.stationName = station.name;
				stationPermissionDto.writable = station.writable;
				stationPermissionDto.main = station.main;
				stationPermissionDto.visibility = station.visibility;
				stationPermissionDto.defaultPerspectiveId = station.defaultPerspectiveId;

				stationPermissionDto.social = station.social;
				stationPermissionDto.subheading = station.subheading;
				stationPermissionDto.sponsored = station.sponsored;
				stationPermissionDto.topper = station.topper;

				stationPermissionDto.allowComments = station.allowComments;
				stationPermissionDto.allowSignup = station.allowSignup;
				stationPermissionDto.allowSocialLogin = station.allowSocialLogin;
				stationPermissionDto.allowSocialShare = station.allowSocialShare;

				stationDto = mapper.readValue(mapper.writeValueAsString(station).getBytes(), StationDto.class);
				stationDto.links = generateSelfLinks(baseUrl + "/api/stations/" + station.id);
				//StationRoles Fields
				StationRole stationRole = stationRolesRepository.findByStationAndPersonId(station, personId);
				if(stationRole != null){
					stationPermissionDto.admin = stationRole.admin;
					stationPermissionDto.editor = stationRole.editor;
					stationPermissionDto.writer = stationRole.writer;
				}

				stationPermissionDtos.add(stationPermissionDto);
				if(stationDtos != null)
					stationDtos.add(stationDto);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stationPermissionDtos;
	}

	public void toggleBookmark(Integer id, Integer postId) {
		// TODO Auto-generated method stub
		
	}

	public List<Integer> getReadableStationIds(StationsPermissions permissions) {
		List<Integer> ids = new ArrayList<Integer>();
		if(permissions.stationPermissionDtos != null)
			for (StationPermission sp : permissions.stationPermissionDtos) {
				if(sp.visibility.equals(Station.UNRESTRICTED) || sp.writer)
					ids.add(sp.stationId);
			}
		return ids;
	}
	
	public List<Integer> getWritableStationIds(StationsPermissions permissions) {
		List<Integer> ids = new ArrayList<Integer>();
		if(permissions.stationPermissionDtos != null)
			for (StationPermission sp : permissions.stationPermissionDtos) {
				if(sp.writable || sp.writer || sp.editor)
					ids.add(sp.stationId);
			}
		return ids;
	}
}
