package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.Bookmark;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.security.FavoriteSecurityChecker;
import co.xarx.trix.services.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@RepositoryEventHandler(Bookmark.class)
@Component
public class BookmarkEventHandler {

	@Autowired
	private FavoriteSecurityChecker favoriteSecurityChecker;
	@Autowired
	private BookmarkService bookmarkService;
	
	@HandleBeforeCreate
	public void handleBeforeCreate(Bookmark bookmark) throws UnauthorizedException {
		if(!favoriteSecurityChecker.canWriteBookmark(bookmark)){
			throw new UnauthorizedException();
		}
		bookmarkService.saveIndex(bookmark);
	}

	@HandleBeforeSave
	public void handleBeforeSave(Bookmark bookmark) throws UnauthorizedException {
		if(!favoriteSecurityChecker.canWriteBookmark(bookmark)){
			throw new UnauthorizedException();
		}
		bookmarkService.saveIndex(bookmark);
	}

	@HandleBeforeDelete
	public void handleBeforeDelete(Bookmark bookmark) throws UnauthorizedException {
		if(!favoriteSecurityChecker.canWriteBookmark(bookmark)){
			throw new UnauthorizedException();
		}
		bookmarkService.deleteIndex(bookmark.getId());
	}
}