update station set networkId = network_id;
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