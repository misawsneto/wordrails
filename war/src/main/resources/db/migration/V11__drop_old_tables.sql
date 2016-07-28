drop table query_fixed_indexes;
drop table query_fixed;
drop table query_object_base_exceptions;
drop table query_object_post_categories;
drop table query_object_post_stations;
drop table query_object_post_tags;
drop table query_object_post;
drop table query_pageable;
DROP TABLE query_sorter;
drop table query_object_base;
DROP TABLE section_base;
DROP TABLE section_fixedquery;

ALTER TABLE person_station_role DROP FOREIGN KEY `FK_b5bn0626atkb37wk1fblq03pd`;
alter table person_station_role drop COLUMN wordpress_id;
ALTER TABLE station DROP FOREIGN KEY `FK_ioixd3yaijoq6klpa9y3m9swm`;
ALTER TABLE station DROP COLUMN wordpress_id;
DROP table wordpress;
