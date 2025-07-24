# README-TESTE-DE-SAUDE.md

Testar Saúde da Aplicação:
powershell

curl http://localhost:8080/actuator/health  <<< USEI UMA PORTA DIFERENTE

Saída esperada:
json

{"status":"UP"}

2. Verificar Conexão com Kafka:
powershell


# Acessar o container do Kafka
docker exec -it kafka bash

# Listar tópicos
kafka-topics --bootstrap-server kafka:9092 --list

Você deve ver o tópico consulta-creditos-topic.
3. Testar Endpoint Principal:


powershell

# curl http://localhost:8080/api/creditos
# curl http://localhost:8080/api/creditos/nfse/12345
# curl http://localhost:8080/api/creditos/numero/CRD-2024-0001

4. Verificar Logs de Integração:
powershell

docker-compose logs -f app | Select-String -Pattern "kafka"

Procure por mensagens como:


Registered kafka listener
Successfully published message

Se Encontrar Problemas:

    Erros de Conexão com Banco:
    powershell

docker-compose logs -f db

Problemas com Kafka:
powershell

docker-compose logs -f kafka

Debug da Aplicação:
powershell

    docker logs api-creditos -f

## Aplicação Pronta para Uso!