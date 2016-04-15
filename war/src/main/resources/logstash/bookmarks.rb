input {
	jdbc {
	  jdbc_driver_library => "../mysql-connector-java-5.1.38-bin.jar"
	  jdbc_driver_class => "com.mysql.jdbc.Driver"
	  jdbc_connection_string => "jdbc:mysql://localhost:3306/trix_dev"
	  jdbc_user => "wordrails"
	  jdbc_password => "wordrails"
	  # schedule => "* * * * *"
	  statement => "SELECT b.person_id as personId, b.post_id as postId, p.station_id as stationId, p.tenantId FROM person_bookmark b, post p WHERE b.post_id = p.id;"
	}
}
output {
    stdout {
        codec => json_lines
    }
}