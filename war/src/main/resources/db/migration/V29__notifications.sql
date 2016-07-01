RENAME TABLE notification to mobilenotification;

ALTER TABLE mobilenotification
	DROP FOREIGN KEY FK_7yfwvid0troi27k14ngffe6ql;
DROP INDEX FK_7yfwvid0troi27k14ngffe6ql ON mobilenotification;