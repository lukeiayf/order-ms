<h1 align="center">
  Order Management API with Microservices
</h1>

This is an application made to manage orders in a microservices RESTfull environment,
I used [this vídeo](https://www.youtube.com/watch?v=e_WgAB0Th_I) as an inspiration.

## Technologies

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [RabbitMQ](https://www.rabbitmq.com/)
- [MongoDB](https://www.mongodb.com/)

## How to run
- Clone git repo:
```
git clone https://github.com/lukeiayf/order-ms
```

- Build project
```
./mvnw clean package
```

- Run:
```
java -jar ./target/order-ms-0.0.1-SNAPSHOT.jar
```