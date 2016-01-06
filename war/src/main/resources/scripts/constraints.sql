ALTER TABLE network DROP FOREIGN KEY FK_nflo9f6ndf7e7s0ohaguxv13y;
DROP INDEX UK_nflo9f6ndf7e7s0ohaguxv13y ON trix_dev.network;
ALTER TABLE network ADD CONSTRAINT FK_nflo9f6ndf7e7s0ohaguxv13y FOREIGN KEY (favicon_id) REFERENCES image (id);

