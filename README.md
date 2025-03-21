# Teste Técnico Agrotis - Nelson Meduna

Este repositório contém a implementação do teste técnico para a Agrotis. A aplicação foi desenvolvida utilizando **Spring Boot** e **PostgreSQL**, e pode ser facilmente executada via **Docker**.

## Executando a aplicação com Docker

A aplicação e o banco de dados estão conteinerizados. Para rodar o sistema, siga os passos abaixo:

### **1. Build da imagem Docker**
Antes de subir os containers, é necessário criar a imagem da aplicação com o **Dockerfile** presente na raiz do projeto:
```sh
# Gerar a imagem da aplicação
docker build -t agrotis-app .
```

### **2. Subir os containers com Docker Compose**
Depois que a imagem for criada, utilize o **docker-compose.yml** para iniciar os containers:
```sh
# Subir a aplicação e o banco de dados
docker-compose up -d
```

### **3. Acessando a aplicação**
A aplicação rodará na porta **8080**. Para verificar se está funcionando, acesse:
```
http://localhost:8080
```

Para conferir os logs da aplicação:
```sh
docker logs -f agrotis-app
```

### **4. Parando os containers**
Caso precise parar a execução:
```sh
docker-compose down
```

## Tecnologias Utilizadas
- **Java 21** + **Spring Boot**
- **PostgreSQL**
- **Docker e Docker Compose**
- **JUnit e Mockito** para testes

## Collection do Postman
- A collection do Postman que eu utilizei para testes das rotas, está nos arquivos do repositório na pasta raíz.
- collection_agrotis.postman_collection.json

---
Agora a aplicação está pronta para execução e testes! 🚀

