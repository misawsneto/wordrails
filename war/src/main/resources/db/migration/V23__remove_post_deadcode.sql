ALTER TABLE post DROP readsCount;
ALTER TABLE post DROP externalFeaturedImgUrl;
ALTER TABLE post DROP externalVideoUrl;
ALTER TABLE post DROP sponsor;
ALTER TABLE post DROP imageSmallId;
ALTER TABLE post DROP imageLargeHash;
ALTER TABLE post DROP imageHash;
ALTER TABLE post DROP imageMediumId;
ALTER TABLE post DROP wordpressId;
ALTER TABLE post DROP imageLargeId;
ALTER TABLE video DROP FOREIGN KEY FK_66ur0mmvrjwaf68rltacbpyml;
DROP INDEX FK_66ur0mmvrjwaf68rltacbpyml ON video;
ALTER TABLE video DROP file;