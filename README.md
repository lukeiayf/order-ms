<h1 align="center">
  Order Management API with Microservices
</h1>

This is an application made to manage orders in a microservices RESTful environment,
I used [this vídeo](https://www.youtube.com/watch?v=e_WgAB0Th_I), and [this challenge](https://github.com/buildrun-tech/buildrun-desafio-backend-btg-pactual/blob/main/problem.md) as an inspiration.

## Technologies

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [RabbitMQ](https://www.rabbitmq.com/)
- [MongoDB](https://www.mongodb.com/)
- [Docker](https://www.docker.com/)

#### Testing suite
- [JUnit](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)

## How to run
- Clone git repo:
```
git clone https://github.com/lukeiayf/order-ms
```

- Build project
```
./mvnw clean package
```

- Run docker containers
```
cd local
docker-compose up
```

- Run:
```
java -jar ./target/order-ms-0.0.1-SNAPSHOT.jar
```

## Microservices 

This application uses a listener on a RabbitMQ container server running on port 15672 to save the messages in a MongoDb database running on port 27017. 
If you want to mock a message directly on the RMQ dashboard you can publish a message in this format:

```
   {
       "orderCode": 1001,
       "clientCode":1,
       "items": [
           {
               "product": "pencil",
               "quantity": 100,
               "price": 1.10
           },
           {
               "product": "notebook",
               "quantity": 10,
               "price": 1.00
           }
       ]
   }
```