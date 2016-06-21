DELETE e FROM acl_entry e INNER JOIN acl_sid sid ON e.sid = sid.id
WHERE e.granting = 0 AND sid.sid = 'ROLE_USER';

DELETE n1 FROM acl_entry n1, acl_entry n2
WHERE n1.id > n2.id AND n1.sid = n2.sid AND n1.granting = n2.granting and n1.acl_object_identity = n2.acl_object_identity;