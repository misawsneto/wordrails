#Manual de configuração local

 - Compile with: mvn clean package
 - Install MySQL
 - Set the following parameters in mysql config file:
    - Under [mysqld] add: lower_case_table_names=1
    - Under [mysqld] add: character-set-server=utf8
    - Under [client] add: default-character-set=utf8
 - Install Tomcat 7
 - Install Redis
 - Edit redis config file (default: /etc/redis/6379.conf)
    - Uncomment attr requirepass and set your password
 - Install ElasticSearch
 - Edit elasticsearch config file 
     - Set attr cluster.name=trix_dev
     - Execute elasticsearch/bin/plugin -install mobz/elasticsearch-head
     - Execute elasticsearch/bin/plugin -install elasticsearch/license/latest
     - Execute elasticsearch/bin/plugin -install elasticsearch/shield/latest
     - Execute sudo bin/shield/esusers useradd [password] -r [username]
 - Run redis and elasticsearch on background
     - Ubuntu: sudo service redis start && sudo service elasticsearch start
     - Arch Linux: sudo systemctl start redis && sudo systemctl start elasticsearch
  
##Executing

_mvn run:tomcat7_

###Required Parameters
dbName - Database schema name
dbUser - Database username
dbPass - Database password
redisPass - Redis password
esUser = Elasticsearch username
esPass = Elasticsearch password


###Optional Parameters
spring.profiles.active - Active profile (dev or prod)
indexES - Run elasticsearch indexer on startup

 **In the first time you must set indexES=true so all data are indexed to elasticsearch**