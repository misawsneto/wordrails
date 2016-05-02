-- alter TABLE invitation drop foreign key FK_l5pu6eucd3r2xw1225jyucvyh;
-- ALTER  TABLE invitation drop COLUMN person_id;
drop table if exists invitation_station;
drop table if exists invitation;
update network set invitationMessage = '{{inviterName}} convidou-lhe para participar da rede <strong class="uc"
style="text-transform: uppercase;">{{networkName}}</strong>.';