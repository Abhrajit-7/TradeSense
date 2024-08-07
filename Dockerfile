# Use an official OpenJDK runtime with Java 21 as a parent image
FROM openjdk:21-jdk-slim 

# Set the working directory in the container
WORKDIR /app

# Copy the jar file from the host machine to the container
COPY target/FunWithStocks-0.0.1-SNAPSHOT.jar /app/FunWithStocks-0.0.1-SNAPSHOT.jar

# Expose the port your application runs on
EXPOSE 8082

# Run the jar file
ENTRYPOINT ["java", "-jar", "FunWithStocks-0.0.1-SNAPSHOT.jar"]
