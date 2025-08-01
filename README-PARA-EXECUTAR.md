 README-PARA-EXECUTAR.md
###  Execute o sistema:

powershell

# Parar de executar a aplicação e reiniciar 
docker-compose down
docker-compose down -v  # Remove containers e volumes antigos

# Iniciar containers
docker-compose up -d --build

docker logs -f api-creditos <<< Roda o log e a aplicação >>
---------------------------

Depois de subir aplicação no docker-compose faça outras verificações.
Próximos passos para validação:
    Teste o health check da aplicação:
curl http://localhost:8080/actuator/health

Resposta esperada:
json
{"status":"UP"}

    Verifique os health checks específicos:
bash
# Banco de dados
curl http://localhost:8080/actuator/health/db
Resposta esperada:
json
{"status":"UP"}

# Kafka
curl http://localhost:8080/actuator/health/kafka
    Teste a conexão com o Kafka:
bash
# Dentro do container do Kafka
kafka-topics --bootstrap-server kafka:9092 --list
    Verifique as tabelas no PostgreSQL:
bash

# Conecte ao banco

* Teste a conecção
docker-compose logs -f db

docker exec -it creditos-db psql -U postgres -d CreditoDB
* Liste as tabelas
\dt

* Veja o conteúdo do banco de dados
docker exec -it creditos-db psql -U postgres -d CreditoDB -c 'SELECT * FROM credito'


#  Outros comandos de verificar status

      docker-compose ps
      docker-compose logs -f app  <<< Ver o log da aplicação
      docker logs creditos-db <<< Ver o log do banco de dados

      Verificações adicionais
      =======================
      docker exec -it creditos-db psql -U postgres -d CreditoDB -c "\dt"   <<< Para ver se executou 
      docker exec -it api-creditos printenv DB_PASSWORD <<< Para ver se a senha do banco de dados 
      docker exec -it api-creditos env <<< Listar toda a configuração
Certifique-se de estar com o perfil docker ativo (-Dspring.profiles.active=docker ou no application-docker.yaml configurado corretamente) e execute:
      mvn clean verify
      docker run --name creditos-db -e POSTGRES_PASSWORD=suasenha -d postgres << Checar o BD
      docker exec -it api-creditos ping creditos-db << Teste a rede entre os containers
Testar a API com Postman / curl

curl -X POST http://localhost:8080/credito \
  -H "Content-Type: application/json" \
  -d '{
    "numeroCredito": "123",
    "numeroNfse": "456",
    "dataConstituicao": "2024-01-01",
    "valorIssqn": 100.00,
    "tipoCredito": "FIXO",
    "simplesNacional": true,
    "aliquota": 0.05,
    "valorFaturado": 2000.00
}'


# Build da aplicação para gerar o pacote
 mvn clean package              <<< Para rodar no docker precisa ter uma jar na pasta target
 mvn clean package -DskipTests  <<< Gerar o pacote pulando os testes 

# Parar rodar local
 $env:DB_PASSWORD = "suasenha" << DEFINA a variavel de ambiente no docker antes de executar sempre
 mvn spring-boot:run

# Para rodar o pacote jar
java -jar target/apiconsultacreditos-0.0.1-SNAPSHOT.jar

# Checkar os endpoints
powershell

# curl http://localhost:8080/api/creditos
# curl http://localhost:8080/api/creditos/nfse/12345
# curl http://localhost:8080/api/creditos/numero/CRD-2024-0001

Para testar a aplicação:

    Verifique os healthchecks:

powershell

docker inspect creditos-db --format='{{.State.Health.Status}}'
docker inspect kafka --format='{{.State.Health.Status}}'
docker inspect api-creditos --format='{{.State.Health.Status}}'

    Acesse os endpoints:

powershell

curl http://localhost:8080/actuator/health

    Para testar o Kafka:

powershell

# Listar tópicos
docker exec kafka kafka-topics --bootstrap-server kafka:9092 --list

# Produzir mensagem
docker exec -it kafka bash
kafka-console-producer --bootstrap-server localhost:9092 --topic consulta-creditos-topic

Dicas de segurança:

    Proteja o arquivo .env:

        Adicione ao .gitignore

        Não compartilhe em repositórios públicos

    Para produção:

        Use um secrets manager (Docker Secrets, HashiCorp Vault)

        Restrinja acesso às portas expostas

        Adicione autenticação ao Kafka