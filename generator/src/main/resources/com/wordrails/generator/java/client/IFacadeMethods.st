IFacadeMethods(entity) ::=

<</*---------------------------------------------------------------------------*/
	@GET("/$entity.pluralLowercase$")
	ContentResponse<List<$entity.name$Dto\>\> get$entity.plural$(@Query("page") Integer _page, @Query("size") Integer _size, @Query("sort") List<String\> _sort);
	
	@GET("/$entity.pluralLowercase$")
	void get$entity.plural$(@Query("page") Integer _page, @Query("size") Integer _size, @Query("sort") List<String\> _sort, Callback<ContentResponse<List<$entity.name$Dto\>\>\> _callback);
	
	@POST("/$entity.pluralLowercase$")
	Response post$entity.name$(@Body $entity.name$Dto $entity.nameLowercase$);
	
	@POST("/$entity.pluralLowercase$")
	void post$entity.name$(@Body $entity.name$Dto $entity.nameLowercase$, Callback<Void> _callback);	
	
	@GET("/$entity.pluralLowercase$/{$entity.id.name$}")
	$entity.name$Dto get$entity.name$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$);
	
	@GET("/$entity.pluralLowercase$/{$entity.id.name$}")
	void get$entity.name$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$, Callback<$entity.name$Dto\> _callback);	
	
	@PUT("/$entity.pluralLowercase$/{$entity.id.name$}")
	Response put$entity.name$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$, @Body $entity.name$Dto $entity.nameLowercase$);
	
	@PUT("/$entity.pluralLowercase$/{$entity.id.name$}")
	void put$entity.name$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$, @Body $entity.name$Dto $entity.nameLowercase$, Callback<Void\> _callback);	
	
	@DELETE("/$entity.pluralLowercase$/{$entity.id.name$}")
	Response delete$entity.name$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$);
	
	@DELETE("/$entity.pluralLowercase$/{$entity.id.name$}")
	void delete$entity.name$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$, Callback<Void\> _callback);
	
	$entity.projections:{projection | $IFacadeProjectionMethods(entity, projection)$}; separator="\n\n"$

	$entity.queries:{query | $IFacadeQueries(entity, query)$}; separator="\n\n"$	
	
	$entity.relationships:{relationship | $IFacadeRelationshipMethods(entity, relationship)$}; separator="\n\n"$	
/*---------------------------------------------------------------------------*/>>