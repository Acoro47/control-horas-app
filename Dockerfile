# Imagen base con Java 17 JRE
FROM eclipse-temurin:17-jre

# Establecer JAVA_HOME automáticamente
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Directorio donde vivirá la app
WORKDIR /app

# Copiamos el JAR generado por Maven
COPY target/horas_trabajo-0.0.1-SNAPSHOT.jar app.jar

# Abrimos el puerto 8080
EXPOSE 8080

# Comando que lanza la app
CMD ["java", "-jar", "app.jar"]
