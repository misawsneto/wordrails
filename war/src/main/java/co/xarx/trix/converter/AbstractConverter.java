package co.xarx.trix.converter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractConverter<Entity, View> {
	public abstract Entity convertFrom(View v);
	public abstract View convertTo(Entity t);

	public List<View> convertToViews(List<Entity> entities){
		List<View> views = new ArrayList<View>(entities.size());
		for (Entity entity : entities) {
			View view = convertTo(entity);
			if(view!=null)
				views.add(view);
		}
		return views;
	}
	
	public List<Entity> convertToEntities(List<View> views){
		List<Entity> entities = new ArrayList<Entity>(views.size());
		for (View view : views) {
			entities.add(convertFrom(view));
		}
		return entities;
	}
}