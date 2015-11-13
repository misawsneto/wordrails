package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.UnauthorizedException;
import co.xarx.trix.domain.Bookmark;
import co.xarx.trix.persistence.elasticsearch.BookmarkEsRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

import co.xarx.trix.security.FavoriteSecurityChecker;

@RepositoryEventHandler(Bookmark.class)
@Component
public class BookmarkEventHandler {
	
	private @Autowired FavoriteSecurityChecker favoriteSecurityChecker;
	private @Autowired BookmarkEsRespository bookmarkEsRespository;
	
	@HandleBeforeCreate
	public void handleBeforeCreate(Bookmark bookmark) throws UnauthorizedException {
		if(!favoriteSecurityChecker.canWriteBookmark(bookmark)){
			throw new UnauthorizedException();
		}
		bookmarkEsRespository.save(bookmark);
	}

	@HandleBeforeSave
	public void handleBeforeSave(Bookmark bookmark) throws UnauthorizedException {
		if(!favoriteSecurityChecker.canWriteBookmark(bookmark)){
			throw new UnauthorizedException();
		}
	}

	@HandleBeforeDelete
	public void handleBeforeDelete(Bookmark bookmark) throws UnauthorizedException {
		if(!favoriteSecurityChecker.canWriteBookmark(bookmark)){
			throw new UnauthorizedException();
		}
		bookmarkEsRespository.delete(bookmark);
	}
}