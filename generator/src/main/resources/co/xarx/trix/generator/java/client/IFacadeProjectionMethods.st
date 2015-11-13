IFacadeProjectionMethods(entity, projection) ::=

<<@GET("/$entity.pluralLowercase$")
ContentResponse<List<$projection.name$Dto\>\> get$entity.plural$_$projection.name$(@Query("page") Integer _page, @Query("size") Integer _size, @Query("sort") List<String\> _sort, @Query("projection") String projection);
	
@GET("/$entity.pluralLowercase$")
void get$entity.plural$_$projection.name$(@Query("page") Integer _page, @Query("size") Integer _size, @Query("sort") List<String\> _sort, @Query("projection") String projection, Callback<ContentResponse<List<$projection.name$Dto\>\>\> _callback);

@GET("/$entity.pluralLowercase$/{$entity.id.name$}")
$projection.name$Dto get$entity.name$_$projection.name$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$, @Query("projection") String projection);

@GET("/$entity.pluralLowercase$/{$entity.id.name$}")
void get$entity.name$_$projection.name$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$, @Query("projection") String projection, Callback<$projection.name$Dto\> _callback);>>