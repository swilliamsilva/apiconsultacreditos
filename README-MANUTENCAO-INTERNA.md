README-MANUTENCAO-INTERNA.md

Limpe o cache do Maven:
mvn dependency:purge-local-repository

Excluir a versão corrompida do repositório local**:
   - Vá até o diretório e apague
   `C:\Users\4OFFICE\.m2\repository\org\apache\maven\plugins\maven-surefire-plugin\3.2.3`
 2. **Forçar o download novamente**:
   - Execute `mvn clean install -U` para forçar a atualização dos snapshots e refazer o download.  

   Passos para executar:

    Limpe o projeto:

bash

mvn clean install -DskipTests

    Execute os testes isoladamente:

bash

mvn test -Dtest=CreditoServiceTest
mvn test -Dtest=CreditoControllerIntegrationTest

    Execute todos os testes com perfil ativo:

bash

mvn test -Dspring.profiles.active=test

Verificações adicionais:

    Certifique-se que não há containers Docker antigos rodando:

bash

docker-compose down -v --remove-orphans

    Atualize as configurações do Kafka no application.properties principal:

properties

spring.kafka.bootstrap-servers=${KAFKA_BOOTSTRAP_SERVERS:kafka:29092}

Essas correções devem resolver os problemas de contexto nos testes e as falhas de conexão com Kafka e banco de dados durante a execução dos testes.