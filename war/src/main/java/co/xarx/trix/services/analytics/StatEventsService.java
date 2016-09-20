package co.xarx.trix.services.analytics;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Comment;
import co.xarx.trix.domain.ESstatEvent;
import co.xarx.trix.domain.Post;
import co.xarx.trix.persistence.ESstatEventRepository;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.util.Constants;
import co.xarx.trix.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;

@Service
public class StatEventsService {
    private AuthService authProvider;
    private ESstatEventRepository statEventRepository;

    @Autowired
    public StatEventsService(ESstatEventRepository statEventRepository, AuthService authProvider){
        this.statEventRepository = statEventRepository;
        this.authProvider = authProvider;
    }

    public void newRecommendEvent(Post post, HttpServletRequest request){
        ESstatEvent recommend = newEvent(post, request);
        recommend.setType(Constants.StatsEventType.POST_RECOMMEND);
        statEventRepository.save(recommend);
    }

    public void newBookmarkEvent(Post post, HttpServletRequest request){
        ESstatEvent bookmark = newEvent(post, request);
        bookmark.setType(Constants.StatsEventType.POST_BOOKMARK);
        statEventRepository.save(bookmark);
    }

    public void newCommentEvent(Comment comment){
        ESstatEvent event = newEvent(comment.getPost());
        event.setType(Constants.StatsEventType.POST_COMMENT);
        event.setPersonId(comment.getAuthor().getId());
        statEventRepository.save(event);
    }

    public void newPostreadEvent(Post post, HttpServletRequest request){
        ESstatEvent postread = newEvent(post, request);
        postread.setType(Constants.StatsEventType.POST_READ);
        statEventRepository.save(postread);
    }

    private ESstatEvent newEvent(Post post){
        ESstatEvent event = new ESstatEvent();

        Integer id = generateRandonId();

        event.setId(id);
        event.setPostId(post.getId());
        event.setTimestamp(new Date());
        event.setPostSlug(post.getSlug());
        event.setAuthorId(post.getAuthor().getId());
        event.setStationId(post.getStation().getId());
        event.setTenantId(TenantContextHolder.getCurrentTenantId());
        event.setPersonId(authProvider.getLoggedPerson().getId());

        return event;
    }

    private ESstatEvent newEvent(Post post, HttpServletRequest request){
        ESstatEvent event = newEvent(post);

        String message = request.getHeader("User-Agent");
        event.setMessage(message);
        event.setDevice(RestUtil.getDeviceFromUserAgent(message));
        event.setHost(request.getLocalName());
        event.setClientip(request.getRemoteAddr());
        event.setReferrer(request.getHeader("referer"));

        return event;
    }

    private Integer generateRandonId(){
        return new BigInteger(UUID.randomUUID().toString().getBytes()).intValue();
    }
}
