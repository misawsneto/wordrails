Manual de configuração local

 - Baixar eclipse JEE. Baixar no eclipse marketplace o plugin para maven (m2e).
 - Fazer o import do projeto via eclipse: File > Import > Maven > Existing Maven Projects
 - Fazer o build do projeto para baixar todas as dependencias
 - Criar base de dados MySQL em localhost:3306 com nome de scheme wordrails2, usuario wordrails e senha wordrails. Importar o dump da base fornecido.
 - Se estiver usando Linux, setar o MySQL para case insensitive. Abra o arquivo /etc/mysql/my.conf e em baixo da linha [mysqld] adicionar esse parametro: lower_case_table_names=1
 - Instalar Tomcat versão 7
 - Para rodar a aplicação, rodar a classe RunTomcat que se encontra no modulo wordrails2-war, pacote com.wordrails.test

Elasticsearch

- Download elasticsearch: https://www.elastic.co/downloads/elasticsearch
- Intale de acordo com a plataforma e inicie o serviço
- Se o serviço do elasticsearch estiver em outra máquina configure o arquivo application.properties (war/src/main/resources/application.properties)

* Um tutorial para implantação no servidor: https://www.digitalocean.com/community/tutorials/how-to-install-elasticsearch-on-an-ubuntu-vps
