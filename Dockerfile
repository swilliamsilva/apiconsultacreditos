# Use a imagem oficial do Eclipse Temurin para Java 21
FROM eclipse-temurin:21-jdk-alpine

# Variável para o JAR
ARG JAR_FILE=target/*.jar

# Copie o JAR da aplicação
COPY ${JAR_FILE} app.jar

# Defina variáveis de ambiente para otimização JVM (ajustáveis conforme necessidade)
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75 -XX:+UseG1GC"

# Porta exposta
EXPOSE 8080

# Comando de execução
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]