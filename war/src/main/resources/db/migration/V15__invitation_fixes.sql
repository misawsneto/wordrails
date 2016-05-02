drop table invitation_station;
drop table invitation;
update network set invitationMessage = '{{inviterName}} convidou-lhe para participar da rede <strong class="uc"
style="text-transform: uppercase;">{{networkName}}</strong>. Acesse o link e confira';