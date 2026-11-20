# ============
# Dockerfile for ms-usuarios service
# ============

FROM maven:3.9-eclipse-temurin-21 AS compilacion
WORKDIR /fuentes


# Copiado solo  el POM para guardar en cache las dependencias
COPY pom.xml ./
RUN mvn -B -q dependency:go-offline

# Luego se copia el código fuente completo
COPY src ./src
RUN mvn -B -q -DskipTests package


# Ejecución
FROM eclipse-temurin:21-jre
WORKDIR /app


# Usuarios sin privilegios
RUN groupadd --system bequian && useradd --system --gid bequian bequian

COPY --from=compilacion /fuentes/target/*.jar app.jar
RUN chown bequian:bequian app.jar
USER bequian

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

