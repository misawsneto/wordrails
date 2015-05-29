package com.wordrails;

import com.wordrails.business.AccessControllerUtil;
import com.wordrails.business.File;
import com.wordrails.business.FileContents;
import com.wordrails.business.Image;
import com.wordrails.business.Network;
import com.wordrails.business.PasswordReset;
import com.wordrails.business.Post;
import com.wordrails.business.PostRead;
import com.wordrails.persistence.FileContentsRepository;
import com.wordrails.persistence.FileRepository;
import com.wordrails.persistence.ImageRepository;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.PostReadRepository;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.QueryPersistence;
import com.wordrails.util.WordpressParsedContent;
import com.wordrails.util.WordrailsUtil;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletRequest;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class WordrailsService {

	private @Autowired NetworkRepository networkRepository;
	private @PersistenceContext EntityManager manager;
	private @Autowired AccessControllerUtil accessControllerUtil;
	private @Autowired PostReadRepository postReadRepository;
	private @Autowired QueryPersistence queryPersistence;
	private @Autowired FileContentsRepository contentsRepository;
	private @Autowired FileRepository fileRepository;
	private @Autowired ImageRepository imageRepository;
	private @Autowired PostRepository postRepository;

	/**
	 * This method should be used with caution because it accesses the database.
	 */
	public Network getNetworkFromHost(ServletRequest srq){
		String host = ((HttpServletRequest) srq).getHeader("Host");

		List<Network> networks = null;

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			networks = networkRepository.findAll();
		}else{
			String[] names = host.split("\\.");
			String topDomain = names[names.length - 2] + "." + names[names.length - 1];
			String subdomain = !topDomain.equals(host) ? host.split("." + topDomain)[0] : null;
			if(subdomain != null && !subdomain.isEmpty())
				networks = networkRepository.findBySubdomain(subdomain);
		}

		if(networks == null || networks.size() == 0){
			networks = networkRepository.findByDomain(host);
		}

		Network network = (networks != null && networks.size() > 0) ? networks.get(0) : networkRepository.findOne(1);
		return network;
	}

	@Async
	@Transactional
	public void countPostRead(Post post, String sessionId){
		PostRead postRead = new PostRead();
		postRead.person = accessControllerUtil.getLoggedPerson();
		postRead.post = post;
		if(postRead.person != null && postRead.person.id == 1) // if user wordrails, include session to uniquely identify the user.
			postRead.sessionid = sessionId;
		try {
			postReadRepository.save(postRead);
			queryPersistence.incrementReadsCount(post.id);
		} catch (org.springframework.dao.DataIntegrityViolationException ex) {}
	}
	
	@Async
	@Transactional
	public void countPostRead(Integer postId, String sessionId){
		PostRead postRead = new PostRead();
		postRead.person = accessControllerUtil.getLoggedPerson();
		postRead.post = postRepository.findOne(postId);
		if(postRead.person != null && postRead.person.id == 1) // if user wordrails, include session to uniquely identify the user.
			postRead.sessionid = sessionId;
		try {
			postReadRepository.save(postRead);
			queryPersistence.incrementReadsCount(postId);
		} catch (org.springframework.dao.DataIntegrityViolationException ex) {}
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

		wpc.content = doc.text();
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
}
