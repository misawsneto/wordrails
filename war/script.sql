update station set networkId = network_id;
UPDATE station
  JOIN network ON network.id = station.networkId
  SET station.tenantId = network.subdomain;

UPDATE person
  JOIN network ON network.id = person.networkId
  SET person.tenantId = network.subdomain;

update person_network_role set networkId = network_id;
update personnetworkregid set networkId = network_id;
update users set networkId = network_id;
update authorities set networkId = network_id;
update term
  join taxonomy on term.taxonomy_id = taxonomy.id
  join station on taxonomy.owningStation_id = station.id
  set term.networkId = station.networkId;
update term
  join taxonomy on term.taxonomy_id = taxonomy.id
  set term.networkId = taxonomy.owningNetwork_id where taxonomy.owningNetwork_id is not null;

alter table post add networkId INT(11) DEFAULT '0';
UPDATE post
  JOIN station ON post.station_id = station.id
  SET post.networkId = station.networkId;
UPDATE post
  JOIN station ON post.station_id = station.id
  JOIN network ON network.id = station.networkId
  SET post.tenantId = network.subdomain;