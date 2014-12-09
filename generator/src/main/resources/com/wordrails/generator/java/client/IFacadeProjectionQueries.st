IFacadeProjectionQueries(entity, query, projection) ::=

<<@GET("/$entity.pluralLowercase$/search/$query.name$")
ContentResponse<List<$projection.name$Dto\>\> $query.name$_$projection.name$($query.parameters:{parameter | @Query("$parameter.name$") $parameter.type$ $parameter.name$, }$@Query("projection") String projection);

@GET("/$entity.pluralLowercase$/search/$query.name$")
void $query.name$_$projection.name$($query.parameters:{parameter | @Query("$parameter.name$") $parameter.type$ $parameter.name$, }$@Query("projection") String projection, Callback<ContentResponse<List<$projection.name$Dto\>\>\> _callback);>>