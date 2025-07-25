 README-PARA-EXECUTAR.md
###  Execute o sistema:

powershell

# Parar de executar a aplicação e reiniciar 
docker-compose down
docker-compose down -v  # Remove containers e volumes antigos

# Iniciar containers
docker-compose up -d --build

# Verificar status
docker-compose ps
docker-compose logs -f app  <<< Ver o log 

# Build da aplicação para gerar o pacote
 mvn clean package              <<< Para rodar no docker precisa ter uma jar na pasta target
 mvn clean package -DskipTests  <<< Gerar o pacote pulando os testes 

# Parar rodar local
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