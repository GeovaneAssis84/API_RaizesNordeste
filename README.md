#API Raízes do Nordeste
Desenvolvido para Atividade Prática de Projeto Multidisciplinar - Trilha Back-End  
Curso Superior em Análise e Desenvolvimento de Sitemas - Uninter

API REST para rede de lanchonetes **Raízes do Nordeste**. 
O projeto permite o gerenciamento de usuários, unidades, produtos, estoque, pedidos e pagamentos simulados.


## Requisitos

- Java 17;
- MySQL;
- Maven;
- Dependências gerenciadas pelo Maven.

### Configuração

Abra o arquivo:
**src/main/resources/application.properties**
Altere as configurações de acesso ao MySQL conforme o seu ambiente:


spring.datasource.url=jdbc:mysql://localhost:3306/bd_mvc?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=sua_senha_mysql

Caso desejado, altere também a chave JWT:

jwt.secret=sua_chave_jwt_base64


###Instalação das dependências

No Windows, execute na pasta do projeto:

````powershell
.\mvnw.cmd clean install
````

### Banco de dados e migrations

Crie o banco de dados no MySQL:

```sql
CREATE DATABASE bd_mvc;
```
As tabelas são criadas ou atualizadas automaticamente pelo Hibernate ao iniciar a aplicação, por meio da configuração:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Não há seed de dados configurado.

### Execução da API

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

### Documentação da API

Após iniciar a aplicação, acesse:

```text
http://localhost:8080/swagger-ui/index.html
```

### Testes
A coleção Postman com testes manuais está disponível na pasta `Documentacao` do repositório.