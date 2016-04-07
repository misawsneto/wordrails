(-) GET /posts/{stationId}/findPostsAndPostsPromotedByTermId
GET /posts
GET /posts/{postId}/body
GET /posts/getPostViewBySlug
GET /posts/{postId}/getPostViewById
GET /posts/{id}/terms
(-) GET /posts/search/networkPosts
GET /posts/search/findBySlug
(-) GET /posts/{stationId}/popular
(-) GET /posts/{stationId}/recent
PUT /posts/{id}
GET /posts/{id}
(?) PUT /posts/{postId}/convert
POST /posts
GET /posts/{postId}/getPostViewById
GET /posts/search/findBySlug
GET /posts/search/networkPosts


GET /comments/search/findPostCommentsOrderByDate
POST /comments (/posts/{postId}/comments)
(X) GET /comments
GET /comments/search/findPostCommentsOrderByDate
GET /comments/search/findPostCommentsOrderByDate


GET /bookmarks/searchBookmarks
GET /notifications/searchNotifications
GET /terms/termTree
(?) GET /taxonomies/search/findNetworkCategories
(X) DELETE /terms/{id}
(X) PUT /terms/{id}
(X) POST /terms
(X) PUT /networks/{id}
(X) POST /images
GET /networks/stats
GET /persons/stats/count
(-) GET /perspectives/termPerspectiveViews
(-) GET /stationPerspectives/{id}
(?) GET /taxonomies/allCategories
(-) GET /perspectives/rowViews
(-) GET /terms/search/findPostsByTagAndStationId
(X) POST /perspectives/termPerspectiveDefinitions
(X) PUT /perspectives/termPerspectiveDefinitions/{id}
(X) PUT /stations/{stationId}/setDefaultPerspective
(X) GET /stations/{id}/stationPerspectives
(X) POST /stationPerspectives
(X) GET /stationPerspectives/{id}
(X) DELETE /stationPerspectives/{id}
(?) deleteSponsor
(?) setMainSponsor
(?) findSponsorByNetworkId
(?) getSponsor
(?) postSponsor
(?) putSponsor
DELETE /stations/{id}
PUT /stations/{stationId}/setMainStation
(-) GET /persons/allInit
(-) GET /stations/{id}
PUT /stations/{id}
POST /stations
(?) findByStationIdAndPersonId
(-) POST /persons/create
(?) postStationRole
(?) findRolesByStationIds
(?) countRolesByStationIds
(-) GET /persons
(?) deletePersonStationRoles
(?) deleteStationRole
(-) GET /persons/search/findByUsername
PUT /persons/{id}/disable
PUT /persons/{id}/enable
PUT /persons/deleteMany/network
PUT /persons/enable/all
PUT /persons/disable/all
PUT /persons/updateStationRoles
(-) POST /networks/createNetwork
(-) POST /persons/tokenSignin
(X) GET /perspectives/rowViews
(-) GET /terms/search/findPostsByTagAndStationId
(?) findRecommendsByPersonIdOrderByDate
GET /persons/me/publicationsCount
GET /persons/me/stats
(-) GET /persons/getPostsByState
PUT /auth/{hash}
POST /auth/forgotPassword
(-) POST /persons/login
(-) GET /j_spring_security_logout
POST /auth/signin
PUT /persons/me/password
(-) GET /persons/{id}
(-) GET /persons/init
(X) GET /persons/{id}/image
(X) GET /persons/{id}/cover
(-) GET /persons/search/findByUsername
(X) GET /perspectives/termPerspectiveViews
(X) GET /perspectives/rowViews
GET /terms/search/findPostsByTagAndStationId
GET /terms/allTerms
PUT /bookmarks/toggleBookmark
GET /bookmarks/searchBookmarks
(X) GET /networks
(X) PUT /recommends/toggleRecommend
GET /amazon/signedUrl
PUT /mobile/location
GET /notifications/searchNotifications
GET /networks/stats
GET /stations