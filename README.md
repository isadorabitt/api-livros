# 📚 API de Livros - Projeto de Portfólio

![Java](https://img.shields.io/badge/Java-17-%23ED8B00?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.5-%236DB33F?logo=spring)
![Swagger](https://img.shields.io/badge/Swagger-2.3.0-%2385EA2D?logo=swagger)

API REST para gerenciamento de acervo literário com integração ao Google Books, desenvolvida como projeto de portfólio para demonstrar habilidades Java Backend.

## ✨ Funcionalidades Principais

- **CRUD completo** de livros
- **Integração com Google Books API** para busca de livros
- **Validações customizadas** com Bean Validation
- **Documentação automática** com Swagger/OpenAPI 3.0
- **Banco de dados em memória** (H2) com console web
- **Tratamento de erros** padronizado
- **Configuração profissional** para desenvolvimento

## 🛠 Tecnologias Utilizadas

- **Linguagem**: Java 17
- **Framework**: Spring Boot 3.2.5
- **Banco de Dados**: H2 (desenvolvimento)
- **Documentação**: SpringDoc OpenAPI 2.3.0
- **Testes**: JUnit 5, Mockito
- **Build**: Maven
- **Outras**: Lombok, ModelMapper, WebClient

## 🚀 Como Executar o Projeto

### Pré-requisitos
- Java 17 JDK instalado
- Maven 3.8+
- Conexão com internet (para integração Google Books)

### Passo a Passo
1. Clone o repositório:
```bash
git clone https://github.com/isadorabitt/api-livros.git
cd api-livros
```

2. Execute o projeto:
```bash
mvn spring-boot:run
```

3. Acesse os endpoints:

| Recurso | URL |
|---------|-----|
| Swagger UI | http://localhost:8080/docs |
| Console H2 | http://localhost:8080/h2-console |
| API Docs | http://localhost:8080/v3/api-docs |

## 🔍 Endpoints Principais

### Livros
- `GET /livros` - Lista todos os livros
- `POST /livros` - Cadastra um novo livro
- `GET /livros/{id}` - Busca livro por ID
- `PUT /livros/{id}` - Atualiza livro
- `DELETE /livros/{id}` - Remove livro

### Google Books Integration
- `GET /livros/buscar?titulo={titulo}` - Busca livros no Google Books
- `POST /livros/buscar?titulo={titulo}` - Busca e salva livro do Google Books

## 🖥️ Acesso ao Banco de Dados

Credenciais para o console H2:
- **URL**: `jdbc:h2:mem:livrosdb`
- **Usuário**: `sa`
- **Senha**: (vazia)

## 📊 Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── isadora/
│   │           └── api_livros/
│   │               ├── config/       # Configurações
│   │               ├── controller/   # Controladores
│   │               ├── dto/          # Objetos de transferência
│   │               ├── model/        # Entidades
│   │               ├── repository/   # Repositórios
│   │               ├── service/      # Lógica de negócio
│   │               └── ApiLivrosApplication.java
│   └── resources/
│       ├── static/
│       └── application.properties    # Configurações
└── test/                            # Testes
```

## 🌟 Destaques Técnicos

1. **Documentação Profissional**
    - Swagger UI customizado
    - Exemplos de requisições/respostas
    - Descrição detalhada dos endpoints

2. **Boas Práticas**
    - Padrão DTO para transferência de dados
    - Tratamento global de exceções
    - Validações de entrada
    - Separação clara de responsabilidades

3. **Configurações Avançadas**
    - Logs detalhados de SQL
    - Hibernate otimizado
    - CORS configurado

## 📝 Licença

Este projeto está licenciado sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

---

**Desenvolvido por Isadora Bittencourt**  
📧 isadorabittencourt2@gmail.com  
🔗 [GitHub](https://github.com/isadorabitt)  
🚀 Em busca de oportunidades como Desenvolvedora Java Junior