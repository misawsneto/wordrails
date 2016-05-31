input {
  jdbc {
    #Define driver's absolute path
    jdbc_driver_library => "/home/jonas/apps/mysql-connector-java-5.1.38-bin.jar"
    jdbc_driver_class => "com.mysql.jdbc.Driver"
    jdbc_connection_string => "jdbc:mysql://localhost:3306/trix_dev"
    jdbc_user => "wordrails"
    jdbc_password => "wordrails"
    # schedule => "* * * * *"
    statement => "SELECT c.id, c.body, c.date, c.lastModificationDate, c.title, c.author_id as authorId, p.author_id as postAuthorId, c.post_id as postId, p.station_id as stationId, c.tenantId, c.createdAt, c.updatedAt, c.version FROM comment c, post p WHERE p.id = c.post_id;"
  }
}
filter {
    mutate {
    rename => { "postauthorid" => "postAuthorId" }
    rename => { "createdat" => "createdAt" }
    rename => { "updatedat" => "updatedAt" }
    rename => { "stationid" => "stationId" }
    rename => { "lastmodificationdate" => "lastModificationDate" }
    rename => { "authorid" => "authorId" }
    rename => { "postid" => "postId" }
    rename => { "tenantid" => "tenantId" }
  }
}
output {
    elasticsearch {
        action => "index"
        hosts => "localhost:9200"
        index => "analytics"
        document_type => "comment"
        document_id => "%{id}"
        workers => 2
    }
    stdout {
        codec => json_lines
    }
}