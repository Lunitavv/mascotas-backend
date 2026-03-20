# Etapa de construcción
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# 1. Instalamos dos2unix para limpiar el archivo mvnw
RUN apk add --no-cache dos2unix

COPY . .

# 2. Convertimos el formato de Windows a Linux y damos permisos
RUN dos2unix mvnw && chmod +x mvnw

# 3. Construimos el proyecto (Añadimos -B para modo no interactivo)
RUN ./mvnw clean package -DskipTests -B

# Etapa de ejecución
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el JAR generado (usamos un wildcard más específico si es posible)
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]