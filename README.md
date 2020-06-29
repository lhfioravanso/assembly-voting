# assembly-voting

API REST para votação em assembleia

Este projeto é um desafio técnico, descrição:

No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias,
por votação. Imagine que você deve criar uma solução backend para gerenciar essas sessões de
votação.

Essa solução deve ser executada na nuvem e promover as seguintes funcionalidades através de
uma API REST:

- Cadastrar uma nova pauta
- Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por um tempo determinado na chamada de abertura ou 1 minuto por default)
- Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado é identificado por um id único e pode votar apenas uma vez por pauta)
- Contabilizar os votos e dar o resultado da votação na pauta

## Stack utilizada

- Java 8
- Spring
- Maven
- MongoDB
- ModelMapper
- Swagger
- JUnit
- Mockito
- Kafka
- Docker

## Requisitos
- Maven 3.6+
- Java 8
- Docker instalado na máquina

## Rodar a aplicação

O Docker deve estar rodando na maquina.

Clone o projeto e navegue até a pasta root do mesmo por um terminal.

Então, execute os comandos na sequencia abaixo para compilar, rodar os testes unitarios da aplicação e gerar as imagens docker:

- mvn clean
- mvn package
- docker-compose up

Caso deseje rodar apenas os testes:
- mvn test

## Acessar a API

Após subir os containers do docker, a api ficará disponivel em:
- http://localhost:8080/api/v1/

Para visualizar a documentação da api:
- http://localhost:8080/swagger-ui.html

OBS: dependendo de como esta configurado o docker, o localhost poderá ser outro IP, então para acessar a aplicação deverá ir pelo ip do docker.
