# Etapa 1: Compilar con MAVEN (Eclipse Temurin 17 Alpine)
FROM maven:3.9.11-eclipse-temurin-17-alpine AS builder

WORKDIR /workspace

# Copiamos POM Y código fuente
COPY pom.xml .
COPY src ./src

# Empaquetamos el JAR
RUN mvn --batch-mode clean package -DskipTests

# Etapa 2: runtime mínimo con Distroless Java 17
FROM gcr.io/distroless/java17-debian11

# Directorio de trabajo
WORKDIR /app

# Copiamos el JAR ya compilado

COPY --from=builder /workspace/target/horas_trabajo-0.0.1-SNAPSHOT.jar app.jar

# Abrimos el puerto 8080
ENV SERVER_PORT=8080
EXPOSE 8080

# Lanzamos la aplicación
ENTRYPOINT ["java","-jar","/app/app.jar"]
