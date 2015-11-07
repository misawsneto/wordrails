package com.wordrails;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.wordrails.api.*;
import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.business.*;
import com.wordrails.persistence.*;
import com.wordrails.services.CacheService;
import com.wordrails.services.WordpressParsedContent;
import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Component
public class WordrailsService {

	Logger log = Logger.getLogger(WordrailsService.class.getName());

	private @Autowired NetworkRepository networkRepository;
	private @Autowired PostReadRepository postReadRepository;
	private @Autowired QueryPersistence queryPersistence;
	private @Autowired PostRepository postRepository;
	private @Autowired PerspectiveResource perspectiveResource;
	private @Autowired CacheService cacheService;
	private @Autowired
	TrixAuthenticationProvider authProvider;
	private @Autowired StationRepository stationRepository;
	private @Autowired StationRolesRepository stationRolesRepository;
	public @Autowired @Qualifier("objectMapper") ObjectMapper mapper;
	public @Autowired TaxonomyRepository taxonomyRepository;
	public @Autowired PersonRepository personRepository;

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
								if (permissions != null) {
									StationsPermissions stationsPermissions = new StationsPermissions();
									stationsPermissions.stationPermissionDtos = permissions;
									return stationsPermissions;
								} else {
									return null;
								}
							}
						});
	}

	public StationsPermissions getPersonPermissions(PermissionId id) throws ExecutionException{
		return stationsPermissions.get(id);
	}

	public void updatePersonPermissions(PermissionId id){
		stationsPermissions.refresh(id);
	}

	public List<Link> generateSelfLinks(String self){
		Link link = new Link();
		link.href = self;
		link.rel = "self";
		return Collections.singletonList(link);
	}

	public String getSubdomainFromHost(String host) {
		String[] names = host.split("\\.");
		String topDomain = names[names.length - 2] + "." + names[names.length - 1];
		return !topDomain.equals(host) ? host.split("." + topDomain)[0] : null;
	}

	public Network getNetworkFromHost(String host){
		Network network = authProvider.getNetwork();

		if(network != null)
			return network;

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<Network> networks = networkRepository.findAll();
			if(networks != null)
				return networks.get(0);
		}else{
			String subdomain = getSubdomainFromHost(host);
			if(subdomain != null && !subdomain.isEmpty()){
				try {
					network = cacheService.getNetworkBySubdomain(subdomain);
				} catch (Exception e) {
					// no network found in cache or db.
				}
				if (network != null)
					return network;
			}
		}

		try {
			return cacheService.getNetworkByDomain(host);
		} catch (Exception e) {
			return networkRepository.findOne(1);
		}
	}

	@Async
	@Transactional(readOnly = false)
	public void countPostRead(Post post, Person person, String sessionId){
		try {
			PostRead postRead = new PostRead();
			postRead.person = person;
			postRead.post = post;
			postRead.sessionid = "0"; // constraint fails if null
			if(postRead.person != null && postRead.person.username.equals("wordrails")) { // if user wordrails, include session to uniquely identify the user.
				postRead.person = null;
				postRead.sessionid = sessionId;
			}

			try {
				postReadRepository.save(postRead);
			} catch (ConstraintViolationException e) {
				log.info("user already read this post");
			}
			queryPersistence.incrementReadsCount(post.id);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Async
	@Transactional(noRollbackFor = org.springframework.dao.DataIntegrityViolationException.class)
	public void countPostRead(Integer postId, Person person, String sessionId){
		PostRead postRead = new PostRead();
		postRead.person = person;
		postRead.post = postRepository.findOne(postId);
		postRead.sessionid = "0"; // constraint fails if null
		if(postRead.person != null && postRead.person.username.equals("wordrails")) { // if user wordrails, include session to uniquely identify the user.
			postRead.person = null;
			postRead.sessionid = sessionId;
		}
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

	@Deprecated
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

		File file = null;

		try{
			for (Element element : imgs) {
				featuredImageUrl = element.attr("src");
				if(featuredImageUrl != null && !featuredImageUrl.isEmpty()){
					URL url;
					try {
						url = new URL(featuredImageUrl);
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

//												Image img = new Image();
//												img.original = fileService.newFile(featuredImageUrl, "");
//												createImages(img);
//												imageRepository.save(img);
//												wpc.image = img;
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

//	private void createImages(Image image) throws SQLException, IOException {
//		TrixFile original = image.original;
//
//		String format = original.mime == null || original.mime.isEmpty() ? null : original.mime.split("image\\/").length == 2 ? original.mime.split("image\\/")[1] : null;
//
//		if (original != null) {
//			TrixFile small = new TrixFile();
//			small.type = TrixFile.INTERNAL_FILE;
//			small.mime = image.original.mime != null ? image.original.mime : MIME;
//			small.name = original.name;
//			fileRepository.save(small);
//
//			TrixFile medium = new TrixFile();
//			medium.type = TrixFile.INTERNAL_FILE;
//			medium.mime = image.original.mime != null ? image.original.mime : MIME;
//			medium.name = original.name;
//			fileRepository.save(medium);
//
//			TrixFile large = new TrixFile();
//			large.type = TrixFile.INTERNAL_FILE;
//			large.mime = image.original.mime != null ? image.original.mime : MIME;
//			large.name = original.name;
//			fileRepository.save(large);
//
//			image.small = small;
//			image.medium = medium;
//			image.large = large;
//
//			BufferedImage bufferedImage;
//			FileContents contents = contentsRepository.findOne(original.id);
//			try (InputStream input = contents.contents.getBinaryStream()) {
//				bufferedImage = ImageIO.read(input);
//			}
//			image.vertical = bufferedImage.getHeight() > bufferedImage.getWidth();
//			updateContents(small.id, bufferedImage, 150, format);
//			updateContents(medium.id, bufferedImage, 300, format);
//			updateContents(large.id, bufferedImage, 1024, format);
//		}
//	}
//
//	private static final String MIME = "image/jpeg";
//	private static final String FORMAT = "jpg";
//	private static final double QUALITY = 1;
//
//	private void updateContents(Integer id, BufferedImage image, int size, String format) throws IOException {
//		format = (format != null  ? format : FORMAT);
//		java.io.File file = java.io.File.createTempFile("image", "." + format);
//		try {
//			Thumbnails.of(image).size(size, size).outputFormat(format).outputQuality(QUALITY).toFile(file);
//			Session session = (Session) manager.getDelegate();
//			LobCreator creator = Hibernate.getLobCreator(session);
//			FileContents contents = contentsRepository.findOne(id);
//			contents.contents = creator.createBlob(FileUtils.readFileToByteArray(file));
//			contentsRepository.save(contents);
//		} finally {
//			file.delete();
//		}
//	}

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
			if((currentStationId != null && currentStationId.equals(stationPermission.stationId)) || stationPermission.main)
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
			if(stationId.equals(station.id)){
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
				term.parent = null;
				term.cells = null;
				term.termPerspectives = null;
				term.posts = null;
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
		//Stations Permissions
		List<StationPermission> stationPermissionDtos = new ArrayList<StationPermission>();
		try {
			List<Station> stations = stationRepository.findByPersonIdAndNetworkId(personId, networkId);
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

				stationPermissionDto.subheading = station.subheading;
				stationPermissionDto.sponsored = station.sponsored;
				stationPermissionDto.topper = station.topper;

				stationPermissionDto.allowComments = station.allowComments;
				stationPermissionDto.allowSocialShare = station.allowSocialShare;

				stationDto = mapper.readValue(mapper.writeValueAsString(station).getBytes("UTF-8"), StationDto.class);
				stationDto.links = generateSelfLinks(baseUrl + "/api/stations/" + station.id);
				//StationRoles Fields
				StationRole stationRole = stationRolesRepository.findByStationAndPersonId(station, personId);
				if(stationRole != null){
					stationPermissionDto.admin = stationRole.admin;
					stationPermissionDto.editor = stationRole.editor;
					stationPermissionDto.writer = stationRole.writer;
				}

				stationPermissionDtos.add(stationPermissionDto);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stationPermissionDtos;
	}

	public List<Integer> getReadableStationIds(StationsPermissions permissions) {
		List<Integer> ids = new ArrayList<Integer>();
		if(permissions.stationPermissionDtos != null)
			for (StationPermission sp : permissions.stationPermissionDtos) {
				//if(sp.visibility.equals(Station.UNRESTRICTED) || sp.writer)
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

	@Async
	@Transactional
	public void updateLastLogin(String username) {
		queryPersistence.updateLastLogin(username);
	}

	@Async
	@Transactional
	public void deleteTaxonomyNetworks(Taxonomy taxonomy){
		taxonomyRepository.deleteTaxonomyNetworks(taxonomy.id);
	}
}
