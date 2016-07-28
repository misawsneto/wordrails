IFacadeProjectionRelationshipMethods(entity, relationship, projection) ::=

<<$if(relationship.collection)$
@GET("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
ContentResponse<$relationship.collectionType$<$projection.name$Dto\>\> get$entity.name$$relationship.nameUppercase$_$projection.name$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$, @Query("projection") String projection);

@GET("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
void get$entity.name$$relationship.nameUppercase$_$projection.name$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$, @Query("projection") String projection, Callback<ContentResponse<$relationship.collectionType$<$projection.name$Dto\>\>\> _callback);
$else$
@GET("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
$projection.name$Dto get$entity.name$$relationship.nameUppercase$_$projection.name$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$, @Query("projection") String projection);

@GET("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
void get$entity.name$$relationship.nameUppercase$_$projection.name$(@Path("$entity.id.name$") $entity.id.type$ $entity.id.name$, @Query("projection") String projection, Callback<$projection.name$Dto\> _callback);
$endif$>>
