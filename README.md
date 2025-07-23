# apiconsultacreditos

API RESTful desenvolvida com Spring Boot para consultar créditos constituídos vinculados a NFS-e.  
Conta com mensageria Kafka, testes automatizados e suporte a PostgreSQL e MariaDB.

---

## Tecnologias Utilizadas

- Java 21 (LTS)
- Spring Boot 3.5.3
- Spring Data JPA + Hibernate
- PostgreSQL / MariaDB / H2 (testes)
- Kafka (Apache Kafka + Spring Kafka)
- JUnit 5 + Mockito
- Docker / Docker Compose

---

## Executando o Projeto

###  Pré-requisitos

- Java 21
- Maven 3.8+
- Docker + Docker Compose
- PostgreSQL instalado **ou** rodando via Docker

### Comandos de execução 

- Executar via Docker Compose (recomendado)
  
     docker-compose up -d   
     
   Isso iniciará:
    
    PostgreSQL (localhost:5432)
    
    MariaDB (localhost:3306)

- Rodar a aplicação local

    mvn spring-boot:run
  
- Rodar testes unitários e de integração

    mvn test
  
- Testes incluídos  
    
    CreditoServiceTest → Testes unitários com Mockito    
    
    CreditoControllerIntegrationTest → Testes de integração com MockMvc
    
    KafkaProducerTest → Testes com Embedded Kafka
    
    
- Endpoints
         Método  	Endpoint                                       Descrição
      
       GET   	/api/creditos/{numeroNfse}	          Lista créditos por número NFS-e
       GET	    /api/creditos/credito/{numeroCredito}


- Build & Deploy

  Gerar JAR:
  
      mvn clean package
  Executar JAR:
  
     java -jar target/apiconsultacreditos-0.0.1-SNAPSHOT.jar


###  Criar banco de dados PostgreSQL
 *  A aplicação já esta configurada no docker-compose.yml para executar o postgress, porém se precisar de algo diferente
    siga as instruções abaixo.
 *  Para ajustar a configuração que precisar pode alterar application.properties
    Dados pré-configurados para testes você encontra no arquivo dados.sql na pasta resources.
   
   
Execute os comandos abaixo em seu terminal, CLI do PostgreSQL ou via PgAdmin:

sql

CREATE DATABASE creditosdb;
-- (opcional) Criação de usuário

-- CREATE USER postgres WITH PASSWORD 'senha';

-- Atribuição de permissões (se necessário)

-- GRANT ALL PRIVILEGES ON DATABASE creditosdb TO postgres;

 

Veja o [Script das tabelas do backend](README-SCRIPT-SQL.md)
