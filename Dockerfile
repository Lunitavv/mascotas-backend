# Etapa de construcción
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

COPY . .

# Dar permisos al wrapper de Maven
RUN chmod +x mvnw

# Construir el proyecto sin tests
RUN ./mvnw clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el JAR generado
COPY --from=build /app/target/*.jar app.jar

# Ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]