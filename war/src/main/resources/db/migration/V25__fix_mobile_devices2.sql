update mobiledevice set type = 'ANDROID' where deviceCode like 'APA%';
update mobiledevice set type = 'APPLE' where deviceCode not like 'APA%';