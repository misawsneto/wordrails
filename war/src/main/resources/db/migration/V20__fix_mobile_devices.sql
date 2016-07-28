alter table mobiledevice MODIFY type VARCHAR(20);
update mobiledevice set type = 'APPLE' where deviceCode like 'APA%';
update mobiledevice set type = 'ANDROID' where deviceCode not like 'APA%';