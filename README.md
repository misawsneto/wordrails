Manual de configuração local

 - Baixe o IntelliJ. (opcional)
 - Execute o comando: mvn -DskipTests=true
 - Criar base de dados MySQL em localhost:3306 com nome de scheme trix_dev, usuario wordrails e senha wordrails. Importar o dump da base fornecido.
 - Se estiver usando Linux, setar o MySQL para case insensitive. Abra o arquivo /etc/mysql/my.conf e em baixo da linha [mysqld] adicionar esse parametro: lower_case_table_names=1
 - Instalar Tomcat versão 7
 - Instalar o Redis e rodar o server com configuracoes padrão
 - Instalar o Elastic Search
 - Edite o arquivo elasticsearch.yml e sete o parametro cluster.name=trix_dev
 - Execute elasticsearch/bin/plugin -install mobz/elasticsearch-head
 - Deixe tanto o Redis quanto o Elastic Search rodando. Pra trix rodar, é necessario esses dois servicos rodando em background.
 - No Ubuntu: sudo service redis start && sudo service elasticsearch start
 - No Arch Linux: sudo systemctl start redis && sudo systemctl start elasticsearch
  
 - Para rodar a aplicação:
    - rodar a classe RunTomcat que se encontra no modulo trix-war, pacote co.xarx.trix.test
    ou
    - executar mvn tomcat7:run

Elasticsearch

- Download elasticsearch: https://www.elastic.co/downloads/elasticsearch
- Intale de acordo com a plataforma e inicie o serviço

* Um tutorial para implantação no servidor: https://www.digitalocean.com/community/tutorials/how-to-install-elasticsearch-on-an-ubuntu-vps
