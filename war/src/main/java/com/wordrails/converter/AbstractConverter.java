package com.wordrails.converter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractConverter<Entity, View> {
	abstract Entity convertToEntity(View v);
	abstract View convertToView(Entity t);

	public List<View> convertToViews(List<Entity> entities){
		List<View> views = new ArrayList<View>(entities.size());
		for (Entity entity : entities) {
			views.add(convertToView(entity));
		}
		return views;
	}
	
	public List<Entity> convertToEntities(List<View> views){
		List<Entity> entities = new ArrayList<Entity>(views.size());
		for (View view : views) {
			entities.add(convertToEntity(view));
		}
		return entities;
	}
}