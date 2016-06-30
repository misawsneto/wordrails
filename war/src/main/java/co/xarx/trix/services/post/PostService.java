package co.xarx.trix.services.post;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.api.PostView;
import co.xarx.trix.config.security.Permissions;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.MobileDevice;
import co.xarx.trix.domain.MobileNotification;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Post;
import co.xarx.trix.exception.NotificationException;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.ImageService;
import co.xarx.trix.services.MobileService;
import co.xarx.trix.services.notification.MobileNotificationService;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.services.security.PersonPermissionService;
import co.xarx.trix.util.Constants;
import co.xarx.trix.util.FileUtil;
import co.xarx.trix.util.StringUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostService {

	@Value("${spring.profiles.active:'dev'}")
	private String profile;

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private PostConverter postConverter;
	@Autowired
	private AuthService authProvider;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private MobileDeviceRepository mobileDeviceRepository;
	@Autowired
	private MobileService mobileService;
	@Autowired
	private MobileNotificationService mobileNotificationService;
	@Autowired
	private PersonPermissionService personPermissionService;
	@Autowired
	private MobileNotificationRepository mobileNotificationRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private QueryPersistence queryPersistence;
	@Autowired
	private PostSearchService postSearchService;

	@Autowired
	private ImageService imageService;

	public void sendNewPostNotification(Post post) throws NotificationException {
		List<MobileDevice> mobileDevices;
		if (stationRepository.isUnrestricted(post.getStationId())) {
			mobileDevices = mobileDeviceRepository.findAll();
		} else {
			List<Person> personFromStation = Lists.newArrayList(personPermissionService.getPersonFromStation(post.station.getId(), Permissions.READ));
			List<Integer> personIds = personFromStation.stream().map(Person::getId).collect(Collectors.toList());

//			personIds.remove(authProvider.getLoggedPerson().getId());

			mobileDevices = mobileDeviceRepository.findByPersonIds(personIds);
		}

		Collection appleDevices = mobileService.getDeviceCodes(mobileDevices, Constants.MobilePlatform.APPLE);
		Collection androidDevices = mobileService.getDeviceCodes(mobileDevices, Constants.MobilePlatform.ANDROID);

		NotificationView notification = getCreatePostNotification(post);
		List<MobileNotification> mobileNotifications = mobileNotificationService.sendNotifications(notification, androidDevices, appleDevices);
		for (MobileNotification n : mobileNotifications) {
			n.setPostId(post.getId());
			mobileNotificationRepository.save(n);
		}
	}

	public NotificationView getCreatePostNotification(Post post) {
		String hash = StringUtil.generateRandomString(10, "Aa#");
		NotificationView notification = new NotificationView(post.title, post.title, hash, false);
		notification.type = MobileNotification.Type.POST_ADDED.toString();
		notification.post = postConverter.convertTo(post);
		notification.postId = post.id;
		notification.postTitle = post.title;
		notification.postSnippet = StringUtil.simpleSnippet(post.body);
		return notification;
	}

	public void publishScheduledPost(Integer postId, boolean allowNotifications) throws NotificationException {
		Post post = postRepository.findOne(postId);
		turnPublished(allowNotifications, post);
		if (post != null) {
			post.date = new Date();
			postRepository.save(post);
		}
	}

	public void turnPublished(boolean allowNotifications, Post post) {
		if (post != null && !post.state.equals(Post.STATE_PUBLISHED)) {
			if (post.notify && allowNotifications) {
				sendNewPostNotification(post);
			}

			post.state = Post.STATE_PUBLISHED;
		}
	}

	public List<PostView> searchRecommends(String q, Integer page, Integer size){
		Person person = personRepository.findByUsername(authProvider.getUser().getUsername());

		Pair<Integer, List<PostView>> postsViews = postSearchService.
				searchPosts(q, person.getId(), page, size, person.getRecommendPosts());

		return postsViews.getRight();
	}

	public List<PostView> searchBookmarks(String q, Integer page, Integer size){
		Person person = authProvider.getLoggedPerson();

		Pair<Integer, List<PostView>> postsViews = postSearchService.searchPosts(q, person.getId(), page, size, person.getBookmarkPosts());

		List<Integer> ids = new ArrayList<Integer>();

		for (PostView pv : postsViews.getRight()) {
			ids.add(pv.postId);
		}

		List<Post> posts = postRepository.findAll(ids);

		List<PostView> pvs = new ArrayList<>();

		for (Post post : posts) {
			pvs.add(postConverter.convertTo(post));
		}
		return pvs;
	}

	public boolean toggleBookmark(Integer postId){
		Person person = authProvider.getLoggedPerson();
		Person originalPerson = personRepository.findOne(person.id);

		boolean success;

		if (originalPerson.bookmarkPosts.contains(postId)) {
			originalPerson.bookmarkPosts.remove(postId);
			person.bookmarkPosts.remove(postId);
			success = false;
		} else {
			originalPerson.bookmarkPosts.add(postId);
			person.bookmarkPosts.add(postId);
			success = true;
		}

		originalPerson.bookmarkPosts = new ArrayList<>(new HashSet<>(originalPerson.bookmarkPosts));

		personRepository.save(originalPerson);
		queryPersistence.updateBookmarksCount(postId);

		return success;

	}

	public boolean toggleRecommend(Integer postId){
		Person person = authProvider.getLoggedPerson();
		Person originalPerson = personRepository.findOne(person.id);

		boolean response;

		if (originalPerson.recommendPosts.contains(postId)) {
			originalPerson.recommendPosts.remove(postId);
			person.recommendPosts.remove(postId);
			response = false;
		} else {
			originalPerson.recommendPosts.add(postId);
			person.recommendPosts.add(postId);
			response = true;
		}

		originalPerson.recommendPosts = new ArrayList<>(new HashSet<>(originalPerson.recommendPosts));

		personRepository.save(originalPerson);
		queryPersistence.updateRecommendsCount(postId);

		return response;
	}

	public void setVideoFeaturedImage(Post post) {
		try{
			if(post.featuredVideo != null && post.featuredVideo.identifier != null && "youtube".equals(post
					.featuredVideo.provider)){
				post.featuredImage = getYoutubeThumbFeaturedImage(post.featuredVideo.provider, post.featuredVideo
						.identifier);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private Image getYoutubeThumbFeaturedImage(String provider, String identifier) {
		if("youtube".equals(provider)) {
			try {
				URL url = new URL("http://img.youtube.com/vi/" + identifier + "/0.jpg");
				InputStream in = new BufferedInputStream(url.openStream());
				Image newImage = new Image(Image.Type.POST.toString());
				String name = "youtube_" + identifier;
				newImage.setTitle(name);
				java.io.File originalFile = FileUtil.createNewTempFile(in);

				newImage = imageService.createAndSaveNewImage(Image.Type.POST.toString(), name, originalFile,
						"image/jpg");
				in.close();
				return newImage;
			}catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return  null;
	}
}
