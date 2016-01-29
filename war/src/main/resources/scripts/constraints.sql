ALTER TABLE network DROP FOREIGN KEY UK_nflo9f6ndf7e7s0ohaguxv13y;
DROP INDEX UK_nflo9f6ndf7e7s0ohaguxv13y ON network;
ALTER TABLE network ADD CONSTRAINT FK_nflo9f6ndf7e7s0ohaguxv13y FOREIGN KEY (favicon_id) REFERENCES image (id);

DROP INDEX UK_rqf8v0h4ficqija7ihgxcrle5 ON person;
DROP INDEX UK_3bm7h73jen16vshmhxmdvw2t8 ON person;

ALTER TABLE image DROP FOREIGN KEY FK_47t3yd2jjthshd9fo7akkstbx;
DROP INDEX FK_47t3yd2jjthshd9fo7akkstbx ON image;
ALTER TABLE image DROP FOREIGN KEY FK_aedika0r5f8e3v0o7mxp9mbjj;
DROP INDEX FK_aedika0r5f8e3v0o7mxp9mbjj ON image;
ALTER TABLE image DROP FOREIGN KEY FK_jlmrknjsp36q8dsgmedn7dvc6;
DROP INDEX FK_jlmrknjsp36q8dsgmedn7dvc6 ON image;
ALTER TABLE image DROP FOREIGN KEY FK_t2gabdofdotdaxia4yamfwve5;
DROP INDEX FK_t2gabdofdotdaxia4yamfwve5 ON image;