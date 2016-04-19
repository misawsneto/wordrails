ALTER TABLE section MODIFY title VARCHAR(255);

ALTER TABLE trix_dev.basesection
	DROP FOREIGN KEY FK_923dxw7uhuir61nwlyri7op71;
ALTER TABLE trix_dev.page_section
	DROP FOREIGN KEY FK_1m6euxsk7udytpvue262dyxmc;
ALTER TABLE trix_dev.page_section
	DROP FOREIGN KEY FK_d1ofn33nn3x3olnirec26y6xb;
ALTER TABLE trix_dev.section_properties
	DROP FOREIGN KEY FK_4ebk6749h7smsa3ytqc1v6fpn;
ALTER TABLE trix_dev.sectioncontainer_children
	DROP FOREIGN KEY FK_5wk5upso4defqvjr44wmfnwq3;
ALTER TABLE trix_dev.sectioncontainer_children
	DROP FOREIGN KEY FK_rhrccsjvwnh0jjf2bktd32229;
ALTER TABLE trix_dev.sectionqueryablelist_queryfixed
	DROP FOREIGN KEY FK_dpw6rl79qsxnfuhj728hs50uw;
ALTER TABLE trix_dev.sectionqueryablelist_queryfixed
	DROP FOREIGN KEY FK_wbcv8ome7qv1ye36y7wast3y;
ALTER TABLE trix_dev.statement_exceptions
	DROP FOREIGN KEY FK_4uds8my2es59u901hot0mkq7d;
ALTER TABLE trix_dev.queryfixed_indexes
	DROP FOREIGN KEY FK_neddhcj130hf876j8y3245g25;
ALTER TABLE trix_dev.query_sorts
	DROP FOREIGN KEY FK_lepubr8twau47k8pf4tfnrc6y;
ALTER TABLE trix_dev.sectioncontainer
	DROP FOREIGN KEY FK_35kk8otydsdp5ollf3bkhrr2y;
ALTER TABLE trix_dev.sectioncontainer
	DROP FOREIGN KEY FK_hm2mcsadsovk89awycoffswd5;
ALTER TABLE trix_dev.sectionqueryablelist
	DROP FOREIGN KEY FK_drxyc5iygfxkqmrj4cb1xu5w4;
ALTER TABLE trix_dev.sectionqueryablelist
	DROP FOREIGN KEY FK_odqqaxrw0hatgqm1u3hu665cp;
ALTER TABLE trix_dev.page
	DROP FOREIGN KEY FK_5kyn0p2xj8q3wkdrpnuuivwjo;
ALTER TABLE trix_dev.querypageable
	DROP FOREIGN KEY FK_hn56lsm7koek8kcqlejylppcu;
ALTER TABLE trix_dev.queryfixed
	DROP FOREIGN KEY FK_dchaamq80u4iste4guigts77w;
ALTER TABLE trix_dev.sectionqueryablelist_queryfixed
	DROP FOREIGN KEY FK_wbcv8ome7qv1ye36y7wast3y;
ALTER TABLE trix_dev.sectionqueryablelist
	DROP FOREIGN KEY FK_drxyc5iygfxkqmrj4cb1xu5w4;
ALTER TABLE trix_dev.sectionqueryablelist
	DROP FOREIGN KEY FK_odqqaxrw0hatgqm1u3hu665cp;
ALTER TABLE trix_dev.queryfixed_indexes
	DROP FOREIGN KEY FK_si17cp9w1cllpa8jdq9x7vn11;
ALTER TABLE trix_dev.sectioncontainer
	DROP FOREIGN KEY FK_35kk8otydsdp5ollf3bkhrr2y;
ALTER TABLE trix_dev.sectioncontainer
	DROP FOREIGN KEY FK_hm2mcsadsovk89awycoffswd5;
ALTER TABLE trix_dev.section
	DROP FOREIGN KEY FK_7g9qsnrvtamerxwwwq483dggd;
ALTER TABLE trix_dev.queryfixed
	DROP FOREIGN KEY FK_dchaamq80u4iste4guigts77w;
DROP TABLE trix_dev.sectionqueryablelist_queryfixed;
DROP TABLE trix_dev.sectionqueryablelist;
DROP TABLE trix_dev.queryfixed_indexes;
DROP TABLE trix_dev.sectioncontainer;
DROP TABLE trix_dev.section;
DROP TABLE trix_dev.queryfixed;
DROP TABLE trix_dev.section_properties;
DROP TABLE trix_dev.sectioncontainer_children;
DROP TABLE trix_dev.basesection;
DROP TABLE trix_dev.page_section;
DROP TABLE trix_dev.section_properties;
DROP TABLE trix_dev.sectioncontainer_children;
DROP TABLE trix_dev.sectionqueryablelist_queryfixed;
DROP TABLE trix_dev.statement_exceptions;
DROP TABLE trix_dev.queryfixed_indexes;
DROP TABLE trix_dev.query_sorts;
DROP TABLE trix_dev.sectioncontainer;
DROP TABLE trix_dev.sectionqueryablelist;
DROP TABLE trix_dev.page;
DROP TABLE trix_dev.section;
DROP TABLE trix_dev.querypageable;
DROP TABLE trix_dev.queryfixed;
DROP TABLE trix_dev.statement;
DROP TABLE statementobjectpost_categories;
DROP TABLE statementobjectpost_stations;
DROP TABLE statementobjectpost_tags;
DROP TABLE statementobjectpost;