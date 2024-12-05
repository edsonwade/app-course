#!/bin/bash

# Limpar e compilar a aplicação
mvn clean install

# Gerar a imagem Docker
./mvnw spring-boot:build-image

#or mvn spring-boot:build-image

echo "Imagem Docker gerada com sucesso!"

# run docker compose
docker-compose up -d