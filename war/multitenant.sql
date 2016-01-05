ALTER TABLE station ADD tenantId VARCHAR(255) DEFAULT '';
UPDATE station
  JOIN network ON network.id = station.network_id
SET station.tenantId = network.subdomain;

ALTER TABLE post ADD tenantId VARCHAR(255) DEFAULT '';
UPDATE post
  JOIN station ON station.id = post.station_id
SET post.tenantId = station.tenantId;

ALTER TABLE users ADD tenantId VARCHAR(255) DEFAULT '';
UPDATE users
  JOIN network ON network.id = users.networkId
SET users.tenantId = network.subdomain;

ALTER table authorities add tenantId VARCHAR(255) DEFAULT '';
UPDATE authorities
  JOIN users ON users.id = authorities.user_id
SET authorities.tenantId = users.tenantId;

alter table cell add tenantId VARCHAR(255) DEFAULT '';
UPDATE cell
  JOIN post ON post.id = cell.post_id
SET cell.tenantId = post.tenantId;

alter table comment add tenantId VARCHAR(255) DEFAULT '';
UPDATE comment
  JOIN post ON post.id = comment.post_id
SET comment.tenantId = post.tenantId;

alter table file add tenantId VARCHAR(255) DEFAULT '';
UPDATE file
  JOIN network ON network.id = file.networkId
SET file.tenantId = network.subdomain;

alter table image add tenantId VARCHAR(255) DEFAULT '';
#execute /api/util/addPicturesToImages

alter table invitation add tenantId VARCHAR(255) DEFAULT '';
UPDATE invitation
  JOIN network ON network.id = invitation.network_id
SET invitation.tenantId = network.subdomain;

alter table notification add tenantId VARCHAR(255) DEFAULT '';
UPDATE notification
  JOIN post ON post.id = notification.post_id
SET notification.tenantId = post.tenantId;

ALTER TABLE passwordreset CHANGE networkSubdomain tenantId VARCHAR(255) DEFAULT '';

alter table person add tenantId VARCHAR(255) DEFAULT '';
UPDATE person
  JOIN users ON users.id = person.user_id
SET person.tenantId = users.tenantId;

alter table person_network_role add tenantId VARCHAR(255) DEFAULT '';
UPDATE person_network_role
  JOIN person ON person.id = person_network_role.person_id
SET person_network_role.tenantId = person.tenantId;

alter table person_station_role add tenantId VARCHAR(255) DEFAULT '';
UPDATE person_station_role
  JOIN person ON person.id = person_station_role.person_id
SET person_station_role.tenantId = person.tenantId;

alter table personnetworkregid add tenantId VARCHAR(255) DEFAULT '';
UPDATE personnetworkregid
  JOIN network ON network.id = personnetworkregid.networkId
SET personnetworkregid.tenantId = network.subdomain;

alter table personnetworktoken add tenantId VARCHAR(255) DEFAULT '';
UPDATE personnetworktoken
  JOIN network ON network.id = personnetworktoken.network_id
SET personnetworktoken.tenantId = network.subdomain;

alter table picture add tenantId VARCHAR(255) DEFAULT '';
UPDATE picture
  JOIN network ON network.id = picture.networkId
SET picture.tenantId = network.subdomain;

alter table postread add tenantId VARCHAR(255) DEFAULT '';
UPDATE postread
  JOIN post ON post.id = postread.post_id
SET postread.tenantId = post.tenantId;

alter table promotion add tenantId VARCHAR(255) DEFAULT '';
UPDATE promotion
  JOIN post ON post.id = promotion.post_id
SET promotion.tenantId = post.tenantId;

alter table recommend add tenantId VARCHAR(255) DEFAULT '';
UPDATE recommend
  JOIN post ON post.id = recommend.post_id
SET recommend.tenantId = post.tenantId;

ALTER TABLE term_perspective ADD tenantId VARCHAR(255) DEFAULT '';
UPDATE term_perspective
  JOIN station ON station.id = term_perspective.stationId
SET term_perspective.tenantId = station.tenantId;

alter table row add tenantId VARCHAR(255) DEFAULT '';
UPDATE row
  JOIN term_perspective
    ON (term_perspective.id = row.featuring_perspective AND row.featuring_perspective IS NOT NULL)
    OR (term_perspective.id = row.home_perspective AND row.home_perspective IS NOT NULL)
    OR (term_perspective.id = row.perspective_id AND row.perspective_id IS NOT NULL)
    OR (term_perspective.id = row.splashed_perspective AND row.splashed_perspective IS NOT NULL)
SET row.tenantId = term_perspective.tenantId;

alter table section add tenantId VARCHAR(255) DEFAULT '';
UPDATE section
  JOIN network ON network.id = section.network_id
SET section.tenantId = network.subdomain;

alter table sponsor add tenantId VARCHAR(255) DEFAULT '';
UPDATE sponsor
  JOIN network ON network.id = sponsor.network_id
SET sponsor.tenantId = network.subdomain;

alter table station_perspective add tenantId VARCHAR(255) DEFAULT '';
UPDATE station_perspective
  JOIN station ON station.id = station_perspective.station_id
SET station_perspective.tenantId = station.tenantId;

alter table taxonomy add tenantId VARCHAR(255) DEFAULT '';
UPDATE taxonomy
  JOIN network ON (network.id = taxonomy.owningNetwork_id AND taxonomy.owningNetwork_id IS NOT NULL)
SET taxonomy.tenantId = network.subdomain;
UPDATE taxonomy
  JOIN station ON (station.id = taxonomy.owningStation_id AND taxonomy.owningStation_id IS NOT NULL)
SET taxonomy.tenantId = station.tenantId;

alter table term add tenantId VARCHAR(255) DEFAULT '';
UPDATE term
  JOIN taxonomy ON taxonomy.id = term.taxonomy_id
SET term.tenantId = taxonomy.tenantId;

alter table userconnection add tenantId VARCHAR(255) DEFAULT '';
UPDATE userconnection
  JOIN users ON users.id = userconnection.user_id
SET userconnection.tenantId = users.tenantId;