IFacadeQueries(entity, query) ::=

<<@GET("/$entity.pluralLowercase$/search/$query.name$")
ContentResponse<List<$entity.name$Dto\>\> $query.name$($query.parameters:{parameter | @Query("$parameter.name$") $parameter.type$ $parameter.name$}; separator=", "$);

@GET("/$entity.pluralLowercase$/search/$query.name$")
void $query.name$($query.parameters:{parameter | @Query("$parameter.name$") $parameter.type$ $parameter.name$}; separator=", "$, Callback<ContentResponse<List<$entity.name$Dto\>\>\> _callback);

$entity.projections:{projection | $IFacadeProjectionQueries(entity, query, projection)$}; separator="\n\n"$>>