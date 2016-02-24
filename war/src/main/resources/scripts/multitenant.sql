#--------------------------------- ADD TENANT ID ---------------------------------#
ALTER TABLE station ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE post ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE users ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE authorities add tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE cell add tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE comment add tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE file add tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE image add tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE invitation add tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE network CHANGE subdomain tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE notification add tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE passwordreset CHANGE networkSubdomain tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE person_network_role ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE person add tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE person_station_role ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE personnetworkregid ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE personnetworktoken ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE picture ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE postread ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE promotion ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE recommend ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE term_perspective ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE row add tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE section ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE sponsor ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE station_perspective ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE taxonomy ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE term ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE userconnection ADD tenantId VARCHAR(255) DEFAULT '';


#--------------------------------- SET TENANT ID ---------------------------------#
UPDATE station
  JOIN network ON network.id = station.network_id
SET station.tenantId = network.tenantId;

UPDATE post
  JOIN station ON station.id = post.station_id
SET post.tenantId = station.tenantId;

UPDATE users
  JOIN network ON network.id = users.networkId
SET users.tenantId = network.tenantId;

UPDATE authorities
  JOIN users ON users.id = authorities.user_id
SET authorities.tenantId = users.tenantId;

UPDATE cell
  JOIN post ON post.id = cell.post_id
SET cell.tenantId = post.tenantId;

UPDATE comment
  JOIN post ON post.id = comment.post_id
SET comment.tenantId = post.tenantId;

UPDATE file
  JOIN network ON network.id = file.networkId
SET file.tenantId = network.tenantId;

UPDATE invitation
  JOIN network ON network.id = invitation.network_id
SET invitation.tenantId = network.tenantId;

UPDATE notification
  JOIN post ON post.id = notification.post_id
SET notification.tenantId = post.tenantId;
UPDATE person
  JOIN users ON users.id = person.user_id
SET person.tenantId = users.tenantId;

UPDATE person_network_role
  JOIN person ON person.id = person_network_role.person_id
SET person_network_role.tenantId = person.tenantId;

UPDATE person_station_role
  JOIN person ON person.id = person_station_role.person_id
SET person_station_role.tenantId = person.tenantId;

UPDATE personnetworkregid
  JOIN network ON network.id = personnetworkregid.networkId
SET personnetworkregid.tenantId = network.tenantId;

UPDATE personnetworktoken
  JOIN network ON network.id = personnetworktoken.network_id
SET personnetworktoken.tenantId = network.tenantId;

UPDATE picture
  JOIN network ON network.id = picture.networkId
SET picture.tenantId = network.tenantId;

UPDATE postread
  JOIN post ON post.id = postread.post_id
SET postread.tenantId = post.tenantId;

UPDATE promotion
  JOIN post ON post.id = promotion.post_id
SET promotion.tenantId = post.tenantId;

UPDATE recommend
  JOIN post ON post.id = recommend.post_id
SET recommend.tenantId = post.tenantId;

UPDATE term_perspective
  JOIN station ON station.id = term_perspective.stationId
SET term_perspective.tenantId = station.tenantId;

UPDATE row
  JOIN term_perspective
    ON (term_perspective.id = row.featuring_perspective AND row.featuring_perspective IS NOT NULL)
       OR (term_perspective.id = row.home_perspective AND row.home_perspective IS NOT NULL)
       OR (term_perspective.id = row.perspective_id AND row.perspective_id IS NOT NULL)
       OR (term_perspective.id = row.splashed_perspective AND row.splashed_perspective IS NOT NULL)
SET row.tenantId = term_perspective.tenantId;

UPDATE section
  JOIN network ON network.id = section.network_id
SET section.tenantId = network.tenantId;

UPDATE sponsor
  JOIN network ON network.id = sponsor.network_id
SET sponsor.tenantId = network.tenantId;

UPDATE station_perspective
  JOIN station ON station.id = station_perspective.station_id
SET station_perspective.tenantId = station.tenantId;

UPDATE taxonomy
  JOIN network ON (network.id = taxonomy.owningNetwork_id AND taxonomy.owningNetwork_id IS NOT NULL)
SET taxonomy.tenantId = network.tenantId;
UPDATE taxonomy
  JOIN station ON (station.id = taxonomy.owningStation_id AND taxonomy.owningStation_id IS NOT NULL)
SET taxonomy.tenantId = station.tenantId;

UPDATE term
  JOIN taxonomy ON taxonomy.id = term.taxonomy_id
SET term.tenantId = taxonomy.tenantId;

UPDATE userconnection
  JOIN users ON users.id = userconnection.user_id
SET userconnection.tenantId = users.tenantId;