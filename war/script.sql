UPDATE station
  JOIN network ON network.id = station.network_id
  SET station.tenantId = network.subdomain, networkId = network_id;

UPDATE person
  JOIN users ON person.user_id = users.id
  JOIN network ON network.id = users.networkId
  SET person.tenantId = network.subdomain, person.networkId = users.networkId;

UPDATE users
  JOIN network ON network.id = users.network_id
SET networkId = network_id, tenantId = network.subdomain;

update person_network_role set networkId = network_id;
update personnetworkregid set networkId = network_id;
update users set networkId = network_id;
update authorities set networkId = network_id;

update term
  join taxonomy on term.taxonomy_id = taxonomy.id
  join station on taxonomy.owningStation_id = station.id
  JOIN network ON network.id = station.networkId
  set term.tenantId = network.subdomain, term.networkId = station.networkId;
update term
  join taxonomy on term.taxonomy_id = taxonomy.id
  JOIN network ON network.id = taxonomy.owningNetwork_id
  set term.tenantId = network.subdomain, term.networkId = taxonomy.owningNetwork_id where taxonomy.owningNetwork_id is not null;

alter table post add networkId INT(11) DEFAULT '0';
UPDATE post
  JOIN station ON post.station_id = station.id
  JOIN network ON network.id = station.networkId
  SET post.networkId = station.networkId, post.tenantId = network.subdomain;