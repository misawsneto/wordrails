IFacadeRelationshipMethods(entity, relationship) ::=

<<$if(relationship.collection)$@GET("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
ContentResponse<$relationship.type$\> get$entity.name$$relationship.nameUppercase$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$);

@GET("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
void get$entity.name$$relationship.nameUppercase$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$, Callback<ContentResponse<$relationship.type$\>\> _callback);
$else$@GET("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
$relationship.type$ get$entity.name$$relationship.nameUppercase$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$);

@GET("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
void get$entity.name$$relationship.nameUppercase$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$, Callback<$relationship.type$\> _callback);
$endif$$if(!relationship.mappedBy)$

@PATCH("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
@Headers("Content-Type: text/uri-list")
Response patch$entity.name$$relationship.nameUppercase$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$, @Body TypedString $relationship.name$);

@PATCH("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
@Headers("Content-Type: text/uri-list")
void patch$entity.name$$relationship.nameUppercase$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$, @Body TypedString $relationship.name$, Callback<Void\> _callback);


@PUT("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
@Headers("Content-Type: text/uri-list")
Response put$entity.name$$relationship.nameUppercase$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$, @Body TypedString $relationship.name$);

@PUT("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
@Headers("Content-Type: text/uri-list")
void put$entity.name$$relationship.nameUppercase$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$, @Body TypedString $relationship.name$, Callback<Void\> _callback);

@DELETE("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
Response delete$entity.name$$relationship.nameUppercase$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$);

@DELETE("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
void delete$entity.name$$relationship.nameUppercase$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$, Callback<Void\> _callback);
$endif$

$relationship.entity.projections:{projection | $IFacadeProjectionRelationshipMethods(entity, relationship, projection)$}; separator="\n\n"$>>