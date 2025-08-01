README-TESTE-FINAIS.md

# Consulta por NFSe
curl -u devuser:devpass http://localhost:8080/api/creditos/nfse/1122334

# Consulta por número de crédito
curl -u devuser:devpass http://localhost:8080/api/creditos/numero/123456


sequência de comandos para testar todos os endpoints usando Git Bash (Windows) e via localhost:
Para executar rodei apliação no powershell com os comandos:

No POWERSHEL 

mvn clean install -DSkiptest

docker-compose down -v
docker-compose up -d --build
docker logs -f api-creditos

E aberto o git bash teste os comandos abaixo: 

# Conecte ao banco

* Teste a conecção
docker-compose logs -f db

docker exec -it creditos-db psql -U postgres -d CreditoDB
* Liste as tabelas
\dt

* Veja o conteúdo do banco de dados
docker exec -it creditos-db psql -U postgres -d CreditoDB -c 'SELECT * FROM credito'


*** Sequencia completa
#!/bin/bash

# A aplicação tem uma segurança basica.
  Uma classe SecurityConfig.java que busca a senha no application-docker.properties
  Para executar o test com esta seguranção precisa chamar da seguinte forma
  
  curl -u admin:admin123 http://localhost:8080/api/creditos/nfse/7891011
          usuario:senha
  curl -u admin:admin123 http://localhost:8080/api/creditos/nfse/1122334       
  curl -u admin:admin123 http://localhost:8080/api/creditos/numero/123456 
  curl -u admin:admin123 http://localhost:8080/api/creditos/numero/789012 
  curl -u admin:admin123 http://localhost:8080/api/creditos/numero/000000 

  curl -u admin:admin123 http://localhost:8080/actuator/health/kafka 

# Health Checks / SCRIPT Completo
echo "=== HEALTH CHECKS ==="
curl -s http://localhost:8080/actuator/health 
curl -s http://localhost:8080/actuator/health/db 
curl -s http://localhost:8080/actuator/health/kafka 

echo -e "\n=== CRÉDITOS ==="
curl -s http://localhost:8080/api/creditos 

echo -e "\n=== CRÉDITOS POR NFSe ==="
curl -s http://localhost:8080/api/creditos/nfse/7891011 
curl -s http://localhost:8080/api/creditos/nfse/1122334 

echo -e "\n=== CRÉDITOS POR NÚMERO ==="
curl -s http://localhost:8080/api/creditos/numero/123456 
curl -s http://localhost:8080/api/creditos/numero/789012 
curl -s http://localhost:8080/api/creditos/numero/000000 

echo -e "\n=== KAFKA MESSAGES ==="
docker exec kafka kafka-console-consumer \
  --bootstrap-server kafka:9092 \
  --topic consulta-credito \
  --from-beginning \
  --timeout-ms 3000
  
# Verificar saúde do banco
docker inspect creditos-db --format='{{.State.Health.Status}}'

# Verificar saúde do Kafka
docker inspect kafka --format='{{.State.Health.Status}}'

# Verificar saúde da aplicação
docker inspect api-creditos --format='{{.State.Health.Status}}'

# Acessar logs da aplicação
docker logs api-creditos


# Teste os endpoint manualmente
Teste os endpoints manualmente:


# Consulta por NFSE
curl -v "http://localhost:8080/api/creditos/nfse/7891011"
# Consulta por número de crédito
curl -v "http://localhost:8080/api/creditos/numero/123456"
# Health check completo
curl -s "http://localhost:8080/actuator/health" 


1. Verificar saúde da aplicação (Health Checks)
bash

# Health geral
curl -s http://localhost:8080/actuator/health 

# Health do banco de dados
curl -s http://localhost:8080/actuator/health/db 

# Health do Kafka
curl -s http://localhost:8080/actuator/health/kafka 

2. Testar endpoints de créditos
bash

# Listar todos os créditos
curl -s http://localhost:8080/api/creditos 

# Buscar por NFSe (substitua 12345 por um número real)
curl -s http://localhost:8080/api/creditos/nfse/12345 

# Buscar por número de crédito (substitua CREDITO001 por um número real)
curl -s http://localhost:8080/api/creditos/numero/CREDITO001 

3. Verificar mensagens no Kafka (opcional)
bash

# Listar tópicos Kafka
docker exec kafka kafka-topics --bootstrap-server kafka:9092 --list

# Consumir mensagens do tópico (substitua consulta-credito pelo nome real do tópico)
docker exec kafka kafka-console-consumer \
  --bootstrap-server kafka:9092 \
  --topic consulta-credito \
  --from-beginning

Para testar via navegador (localhost):

Cole estas URLs no navegador:

    Health Checks:

        Saúde geral: http://localhost:8080/actuator/health

        Saúde do banco: http://localhost:8080/actuator/health/db

        Saúde do Kafka: http://localhost:8080/actuator/health/kafka

    Endpoints de créditos:

        Todos os créditos: http://localhost:8080/api/creditos

        Por NFSe: http://localhost:8080/api/creditos/nfse/12345 (substitua 12345)

        Por número: http://localhost:8080/api/creditos/numero/CREDITO001 (substitua CREDITO001)

Dicas importantes:

    Pré-requisitos:

        Instalar jq para formatação JSON:
        choco install jq (via Chocolatey) ou
        Baixar binário: https://stedolan.github.io/jq/download/

        Docker rodando com os containers

        Aplicação em execução (docker-compose up)

    Sequência recomendada:

bash

# 1. Iniciar containers
docker-compose up -d

# 2. Verificar saúde
curl -s http://localhost:8080/actuator/health 

# 3. Testar endpoints de créditos
curl -s http://localhost:8080/api/creditos 

# 4. Testar busca específica
curl -s http://localhost:8080/api/creditos/numero/CREDITO001 

# 5. Verificar mensagens no Kafka
docker exec kafka kafka-console-consumer \
  --bootstrap-server kafka:9092 \
  --topic consulta-credito \
  --from-beginning \
  --timeout-ms 5000

Exemplos de saída esperada:

Health Check do Banco:
json

{
  "timestamp": "2025-07-29T15:30:45.123Z",
  "status": "UP",
  "database": "PostgreSQL",
  "version": "15.3",
  "driver": "PostgreSQL JDBC Driver",
  "driverVersion": "42.6.0"
}

Listagem de Créditos:
json

[
  {
    "numeroCredito": "CREDITO001",
    "numeroNfse": "NF12345",
    "valorCredito": 15000.00,
    "situacao": "APROVADO"
  },
  {
    "numeroCredito": "CREDITO002",
    "numeroNfse": "NF67890",
    "valorCredito": 25000.00,
    "situacao": "PENDENTE"
  }
]

Busca por Número de Crédito:
json

{
  "numeroCredito": "CREDITO001",
  "numeroNfse": "NF12345",
  "valorCredito": 15000.00,
  "situacao": "APROVADO"
}

Mensagem no Kafka:
json

{
  "numeroCredito": "CREDITO001",
  "numeroNfse": "NF12345",
  "valorCredito": 15000.00,
  "situacao": "APROVADO",
  "timestamp": "2025-07-29T15:32:10.456Z"
}

Para problemas comuns:

    Aplicação não responde:
    bash

docker logs api-creditos

Kafka não está recebendo mensagens:
bash

docker logs kafka

Banco de dados sem dados:
bash

docker exec -it creditos-db psql -U postgres -d CreditoDB -c "SELECT * FROM creditos;"