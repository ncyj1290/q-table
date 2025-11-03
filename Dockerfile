FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY build/libs/*.war app.war

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.war"]