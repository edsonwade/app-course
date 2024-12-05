# Por que usar **build packages** é melhor para **docker build image**? 🤔

## Vantagens de usar **build packages** para construir imagens Docker:

1. **Automação e Consistência** 🔄:
    - Ao usar pacotes de construção como **Maven** ou **Gradle** no processo de construção da imagem Docker, você
      garante que o mesmo processo de compilação será executado de forma consistente em qualquer ambiente.
    - Isso reduz a possibilidade de erros de configuração manual e torna o fluxo de trabalho mais robusto e fácil de
      automatizar.

2. **Eficiência no Build** ⚡:
    - Usar ferramentas como **Maven** ou **Gradle** para construir o projeto e empacotar em arquivos JAR ou WAR antes de
      criar a imagem Docker melhora a eficiência. Ao invés de construir a aplicação dentro do Dockerfile (o que pode ser
      mais lento), o empacotamento prévio garante que o código já esteja pronto para ser containerizado.
    - Esse processo de "build separado" pode resultar em imagens menores e mais rápidas para rodar.

3. **Imagens Menores e Mais Rápidas** 🚀:
    - Se você construir a aplicação dentro do Docker, o Dockerfile tende a criar camadas de imagem grandes, com muitos
      arquivos temporários (por exemplo, arquivos de compilação). Quando você faz o build da aplicação fora do Docker (
      no seu ambiente de desenvolvimento ou CI/CD) e apenas copia o artefato pronto (por exemplo, um arquivo JAR ou WAR)
      para a imagem Docker, você está garantindo que a imagem seja mais limpa e compacta.
    - Isso reduz o tempo de construção e o tamanho da imagem final.

4. **Controle e Escalabilidade** 🛠️:
    - Usar pacotes de construção também permite que você tenha maior controle sobre o processo de build. Ferramentas
      como **Maven** ou **Gradle** oferecem flexibilidade para adicionar dependências, definir perfis de construção,
      configurar o gerenciamento de versões, entre outras funcionalidades avançadas.
    - Em ambientes de microserviços, onde várias imagens Docker podem ser construídas, a construção fora do Docker
      proporciona um controle melhor do ciclo de vida dos artefatos.

5. **Desempenho em Ambientes CI/CD** 🌐:
    - Em pipelines de integração contínua (CI), ao usar ferramentas de build, você pode integrar o processo de
      construção e testes antes de criar a imagem Docker. Isso permite que você teste e valide o código antes de criar a
      imagem, o que resulta em imagens mais confiáveis e testadas.

---

## Por que é importante usar **build packages** no Docker?

1. **Separação de Responsabilidades** 🔹:
    - O Docker deve ser usado para containerizar e executar a aplicação, enquanto o processo de construção deve ser
      realizado por ferramentas específicas (Maven, Gradle, etc). Manter essas responsabilidades separadas torna o
      processo mais limpo e fácil de gerenciar.

2. **Reusabilidade de Código** 🔁:
    - Quando você constrói o código e o empacota como artefato (JAR, WAR), você pode reutilizar esse artefato em vários
      ambientes, como desenvolvimento, teste, staging e produção. Isso evita ter que recompilar ou reconstruir a
      aplicação várias vezes para diferentes ambientes.

3. **Facilidade de Atualizações e Manutenção** 🔄:
    - Se você decidir atualizar uma dependência ou modificar o código, você pode facilmente reconstruir e empacotar o
      artefato sem ter que modificar o Dockerfile ou outros componentes de infraestrutura.

4. **Segurança** 🔒:
    - Ao construir a aplicação fora do Docker, você pode ter mais controle sobre as dependências e versões do código
      antes de empacotá-lo. Isso ajuda a evitar a inclusão de pacotes desnecessários ou vulneráveis no contêiner Docker.

---

## Exemplo de processo de construção

1. **Primeiro, construa o artefato com Maven**:
   ```bash
   mvn clean package
   ```
2. **Depois, construa a imagem Docker, copiando o artefato para a imagem**:

  ```Dockerfile
     FROM openjdk:17
     COPY target/event-service-1.0-SNAPSHOT.jar event-service.jar
     EXPOSE 8081
     ENTRYPOINT ["java", "-jar", "event-service.jar"]
  ```

3. **Construa a imagem Docker:**:
   ```
   docker build -t event-service:0.0.1-SNAPSHOT .
   ```

```bash
./mvnw spring-boot:build-image
or
mvn spring-boot:build-image
```

- **Gerar o JAR primeiro**:

```bash
mvn clean package
mvn spring-boot:build-image
````

- **Sempre que fazer algumas mudanças na aplicação e agora deseja criar uma nova imagem Docker**:

1. **Execute a limpeza e instalação**:

  ```bash
    mvn clean install
   ```

2. **Crie a nova imagem Docker:**:

 ```bash
   ./mvnw spring-boot:build-image
```

3. **Verifique as imagens Docker::**:
 ```bash
   docker images
```