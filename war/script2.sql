
use wordrails2;

ALTER TABLE PersonNetworkRegId MODIFY person_id INT(11) default NULL;
ALTER TABLE PersonNetworkToken MODIFY person_id INT(11) default NULL;
ALTER TABLE PostRead MODIFY person_id INT(11) default NULL;