# assembly-voting

API REST para votação em assembleia


## Stack

- Java 8
- Spring
- MongoDB 
- ModelMapper
- Swagger
- JUnit
- Mockito

## Requisitos
- MongoDB [ informar se necessario a configuração do banco na application.properties ]

## Rodar a aplicação

Após clonar o projeto, navegue até o diretorio do projeto pelo cmd.

Execute os comandos abaixo para compilar e rodar os testes da aplicação:
> mvn clean  
> mvn install

Caso deseje rodar apenas os testes:
> mvn test

Navegue até o diretório onde foi gerado o arquivo .jar e rode o comando abaixo para subir a aplicação:
> java -jar assembly-voting-0.0.1-SNAPSHOT.jar


## Acessar a API

Após subir o projeto, a api ficará disponivel em:
- http://localhost:8080/api/v1/

Para visualizar a documentação da api:
- http://localhost:8080/swagger-ui.html

