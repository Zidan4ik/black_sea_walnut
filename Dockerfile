FROM openjdk:17-jdk-alpine
COPY target/*.jar black_sea_walnut-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "black_sea_walnut-0.0.1-SNAPSHOT.jar"]