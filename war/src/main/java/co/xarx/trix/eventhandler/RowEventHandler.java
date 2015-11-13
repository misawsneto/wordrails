package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.Row;
import co.xarx.trix.persistence.QueryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

/**
 * Created by misael on 02/10/2015.
 */
@RepositoryEventHandler(Row.class)
@Component
public class RowEventHandler {

    private @Autowired
    QueryPersistence queryPersistence;

    @HandleAfterCreate
    public void handleAfterCreate (Row row){
        if(row.type != null && row.type.equals(Row.FEATURED_ROW)){
            queryPersistence.deleteFeaturedRow(row.featuringPerspective.id, row.id);
        }else if(row.type != null && row.type.equals(Row.SPLASHED_ROW)){
            queryPersistence.deleteSplashedRow(row.splashedPerspective.id, row.id);
        }
    }
}
