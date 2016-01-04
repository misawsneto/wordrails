package co.xarx.trix.persistence;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.xarx.trix.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class QueryPersistence {
	private @PersistenceContext EntityManager manager;
	
	@Transactional
	public void incrementReadsCount(Integer postId) {
		manager.createNativeQuery("UPDATE Post post SET post.readsCount = post.readsCount + 1 WHERE post.id = :postId").setParameter("postId", postId).executeUpdate();
	}
	
	@Transactional
	public void decrementReadsCount(Integer postId) {
		manager.createNativeQuery("UPDATE Post post SET post.readsCount = post.readsCount - 1 WHERE post.id = :postId and post.readsCount > 0").setParameter("postId", postId).executeUpdate();
	}
	
	@Transactional
	public void incrementBookmarksCount(Integer postId) {
		manager.createNativeQuery("UPDATE Post post SET post.bookmarksCount = post.bookmarksCount + 1 WHERE post.id = :postId").setParameter("postId", postId).executeUpdate();
	}
	
	@Transactional
	public void decrementBookmarksCount(Integer postId) {
		manager.createNativeQuery("UPDATE Post post SET post.bookmarksCount = post.bookmarksCount - 1 WHERE post.id = :postId and post.bookmarksCount > 0").setParameter("postId", postId).executeUpdate();
	}
	
	@Transactional
	public void incrementFavoritesCount(Integer postId) {
		manager.createNativeQuery("UPDATE Post post SET post.favoritesCount = post.favoritesCount + 1 WHERE post.id = :postId").setParameter("postId", postId).executeUpdate();
	}
	
	@Transactional
	public void decrementFavoritesCount(Integer postId) {
		manager.createNativeQuery("UPDATE Post post SET post.favoritesCount = post.favoritesCount - 1 WHERE post.id = :postId and post.favoritesCount > 0").setParameter("postId", postId).executeUpdate();
	}
	
	@Transactional
	public void incrementRecommendsCount(Integer postId) {
		manager.createNativeQuery("UPDATE Post post SET post.recommendsCount = post.recommendsCount + 1 WHERE post.id = :postId").setParameter("postId", postId).executeUpdate();
	}
	
	@Transactional
	public void decrementRecommendsCount(Integer postId) {
		manager.createNativeQuery("UPDATE Post post SET post.recommendsCount = post.recommendsCount - 1 WHERE post.id = :postId and post.recommendsCount > 0").setParameter("postId", postId).executeUpdate();
	}
	
	@Transactional
	public void deleteBookmark(Integer postId, Integer personId){
		manager.createNativeQuery("DELETE FROM Bookmark WHERE post_id = :postId AND person_id = :personId").setParameter("postId", postId).setParameter("personId", personId).executeUpdate();
	}

	@Transactional
	public void deleteRecommend(Integer postId, Integer personId) {
		manager.createNativeQuery("DELETE FROM Recommend WHERE post_id = :postId AND person_id = :personId").setParameter("postId", postId).setParameter("personId", personId).executeUpdate();
	}

	@Transactional
	public void changePostState(Integer postId, String state) {
		manager.createNativeQuery("UPDATE post SET state=:state where id = :postId").setParameter("postId", postId).setParameter("state", state).executeUpdate();
	}

	@Transactional
	public void updateCommentsCount(Integer postId) {
		manager.createNativeQuery("UPDATE Post set commentsCount = (select count(*) FROM comment WHERE post_id = :postId) WHERE id = :postId;").setParameter("postId", postId).executeUpdate();
	}

	@Transactional
	public void deletePostReadsInPosts(List<Integer> ids) {
		manager.createQuery("delete from PostRead postRead where postRead.post.id in (:ids)").setParameter("ids", ids).executeUpdate();
	}
	
	@Transactional
	public void deleteNotificationsInPosts(List<Integer> ids) {
		manager.createQuery("delete from Notification notification where notification.post.id in (:ids)").setParameter("ids", ids).executeUpdate();
	}

	@Transactional
	public void deleteBookmarksInPosts(List<Integer> ids) {
		manager.createQuery("delete from Bookmark bookmark where bookmark.post.id in (:ids)").setParameter("ids", ids).executeUpdate();
	}
	
	@Transactional
	public void deleteCellsInPosts(List<Integer> ids) {
		manager.createQuery("delete from Cell cell where cell.post.id in (:ids)").setParameter("ids", ids).executeUpdate();
	}
	
	@Transactional
	public void deleteCommentsInPosts(List<Integer> ids) {
		manager.createQuery("delete from Comment comment where comment.post.id in (:ids)").setParameter("ids", ids).executeUpdate();
	}
	
	@Transactional
	public void deleteImagesInPosts(List<Integer> ids) {
		manager.createQuery("delete from Image image where image.post.id in (:ids)").setParameter("ids", ids).executeUpdate();
	}
	
	@Transactional
	public void deletePromotionsInPosts(List<Integer> ids) {
		manager.createQuery("delete from Promotion promotion where promotion.post.id in (:ids)").setParameter("ids", ids).executeUpdate();
	}
	
	@Transactional
	public void deleteRecommendsInPosts(List<Integer> ids) {
		manager.createQuery("delete from Recommend recommend where recommend.post.id in (:ids)").setParameter("ids", ids).executeUpdate();
	}

	@Transactional
	public void updateMainStation(Integer id, boolean main) {
		manager.createNativeQuery("update Station station set main = :main where station.id = :id").setParameter("id", id).setParameter("main", main).executeUpdate();		
	}

	@Transactional
	public List<Object[]> getPersonPublicationsCount(Integer personId) {
		return manager.createNativeQuery("select (select count(*) from Post po where po.author_id = p.id and po.state = 'PUBLISHED')," +
										"(select count(*) from Post po where po.author_id = p.id and po.state = 'DRAFT')," +
										"(select count(*) from Post po where po.author_id = p.id and po.state = 'SCHEDULED')" +
				"from Person p where p.id = :personId").setParameter("personId", personId).getResultList();
	}

	@Transactional
	public void setNoAuthor(Integer personId) {
		manager.createNativeQuery("UPDATE Post post SET post.author_id = 1, post.state = 'NOAUTHOR' WHERE post.author_id = :personId").setParameter("personId", personId).executeUpdate();
	}

	@Transactional
	public List<Object[]> getStationsPublicationsCount(List<Integer> stationIds) {
		return manager.createNativeQuery("select (select count(*) from Post po where po.station_id = s.id and po.state = 'PUBLISHED')," +
				"(select count(*) from Post po where po.station_id = s.id and po.state = 'DRAFT')," +
				"(select count(*) from Post po where po.station_id = s.id and po.state = 'SCHEDULED')" +
				"from Station s where s.id in (:stationIds)").setParameter("stationIds", stationIds).getResultList();
	}

	@Transactional
	public void updateDefaultPerspective(Integer id, Integer perspectiveId) {
		manager.createQuery("update Station set defaultPerspectiveId = :defaultPerspectiveId where id = :id").setParameter("defaultPerspectiveId", perspectiveId).setParameter("id", id).executeUpdate();
	}

	@Transactional
	public void deleteFeaturedRow(Integer perspectiveId, Integer notId) {
		manager.createQuery("delete from Row where type = 'F' and featuringPerspective.id = :perspectiveId and id <> :notId")
				.setParameter("perspectiveId", perspectiveId).setParameter("notId", notId).executeUpdate();
	}

	@Transactional
	public void deleteSplashedRow(Integer perspectiveId, Integer notId) {
		manager.createQuery("delete from Row where type = 'S' and splashedPerspective.id = :perspectiveId and id <> :notId")
				.setParameter("perspectiveId", perspectiveId).setParameter("notId", notId).executeUpdate();
	}

    public List<Post> findPostsByTag(Set<String> tags, Integer stationId, int page, int size) {
        return manager.createQuery("select p from Post p join p.tags tgs where tgs in :tags and p.station.id = :stationId", Post.class)
                .setParameter("tags", tags).setParameter("stationId", stationId)
                .setMaxResults(size)
                .setFirstResult(page * size)
                .getResultList();
    }

	@Transactional
	public List<Object[]> getRepeatedImage(){
		return manager.createNativeQuery("SELECT id, originalHash, count(originalHash) AS c FROM Image WHERE originalHash IS NOT NULL GROUP BY originalHash HAVING c > 1").getResultList();
	}

	@Transactional
	public List<Integer> getRepeatedImageHash(String hash){
		return manager.createNativeQuery("select id from Image where originalHash = :hash")
				.setParameter("hash", hash)
				.getResultList();
	}

	@Transactional
	public void updatePostFeaturedImageId(List<Integer> repeatedImages, Integer imageId){
		manager.createQuery("update Post set featuredImage.id = :imageId where featuredImage.id in (:repeatedImages)")
				.setParameter("imageId", imageId)
				.setParameter("repeatedImages", repeatedImages)
				.executeUpdate();
	}

	@Transactional
	public void updateNetworkLogoId(List<Integer> repeatedImages, Integer imageId){
		manager.createQuery("update Network set logo.id = :imageId where logo.id in (:repeatedImages)")
				.setParameter("imageId", imageId)
				.setParameter("repeatedImages", repeatedImages)
				.executeUpdate();
	}

	@Transactional
	public void updateNetworkLoginImageId(List<Integer> repeatedImages, Integer imageId){
		manager.createQuery("update Network set loginImage.id = :imageId where loginImage.id in (:repeatedImages)")
				.setParameter("imageId", imageId)
				.setParameter("repeatedImages", repeatedImages)
				.executeUpdate();
	}

	@Transactional
	public void updateNetworkFaviconId(List<Integer> repeatedImages, Integer imageId){
		manager.createNativeQuery("update Network set faviconId = :imageId where faviconId in (:repeatedImages)")
				.setParameter("imageId", imageId)
				.setParameter("repeatedImages", repeatedImages)
				.executeUpdate();
	}

	@Transactional
	public void updateNetworkFavicon(List<Integer> repeatedImages){
		manager.createNativeQuery("update Network set favicon_id = NULL where network.favicon_id in (:repeatedImages)")
				.setParameter("repeatedImages", repeatedImages)
				.executeUpdate();
	}


	@Transactional
	public void updateNetworkSplashImageId(List<Integer> repeatedImages, Integer imageId){
		manager.createQuery("update Network set splashImage.id = :imageId where splashImage.id in (:repeatedImages)")
				.setParameter("imageId", imageId)
				.setParameter("repeatedImages", repeatedImages)
				.executeUpdate();
	}

	@Transactional
	public void updateStationLogo(List<Integer> repeatedImages, Integer imageId){
		manager.createNativeQuery("update Station set logo_id = :imageId where station.logo_id in (:repeatedImages)")
				.setParameter("imageId", imageId)
				.setParameter("repeatedImages", repeatedImages)
				.executeUpdate();
	}

	@Transactional
	public void updateImagePicture(List<Integer> repeatedImages, Integer imageId){
		manager.createNativeQuery("update image_picture set image_id = :imageId where image_picture.image_id in (:repeatedImages)")
				.setParameter("imageId", imageId)
				.setParameter("repeatedImages", repeatedImages)
				.executeUpdate();
	}

	@Transactional
	public void deleteImagePicture(List<Integer> ids){
		manager.createNativeQuery("DELETE FROM image_picture WHERE image_picture.image_id in (:ids)")
				.setParameter("ids", ids)
				.executeUpdate();
	}

	@Transactional
	public void deleteImageHash(List<Integer> ids){
		manager.createNativeQuery("DELETE FROM image_hash WHERE image_hash.image_id in (:ids)")
				.setParameter("ids", ids)
				.executeUpdate();
	}

	@Transactional
	public void deleteImageList(List<Integer> ids){
		manager.createNativeQuery("DELETE FROM image WHERE id in (:ids)")
				.setParameter("ids", ids)
				.executeUpdate();
	}
}
