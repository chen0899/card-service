FROM maven AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:21
WORKDIR /app
COPY --from=build /app/target/card-service*.jar /app/card-service.jar
ENTRYPOINT ["java", "-jar", "card-service.jar"]