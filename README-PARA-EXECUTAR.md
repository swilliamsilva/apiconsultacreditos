 README-PARA-EXECUTAR.md
###  Execute o sistema:

powershell

# Build da aplicação
mvn clean package

# Parar de executar a aplicação / reiniciar
docker-compose down

# Iniciar containers
docker-compose up -d --build

# Verificar status
docker-compose ps

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