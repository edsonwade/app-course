name: Build and Docker Image

on:
  push:
    branches:
      - main

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v2

      - name: Set up Java
        uses: actions/setup-java@v2
        with:
          java-version: '17'

      - name: Build the application
        run: mvn clean install

      - name: Build Docker image
        run: mvn spring-boot:build-image

      - name: Push Docker image to Docker Hub
        run: docker push yourusername/yourimage:tag
