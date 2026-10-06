# Backend

API REST Spring Boot 3.3 / Java 17, com camadas `controller`, `service`, `repository`, `dto` e `domain`, autenticação JWT, Bean Validation, Spring Data JPA e migrations Flyway.

## Configuração

Configure `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `ADMIN_PASSWORD` e `JWT_SECRET` no ambiente antes de iniciar. Consulte o README raiz e [`../database/README.md`](../database/README.md). O seed é ligado por padrão para desenvolvimento e só cria o ADMIN se `ADMIN_PASSWORD` estiver configurada.

Para testar o perfil clínico local: `marina@clinica.local` com `DENTIST_PASSWORD` (padrão de desenvolvimento `1234`). O usuário só é semeado no perfil `dev` e se ainda não existir.

## Executar

```powershell
.\mvnw.cmd spring-boot:run
```

Com Maven instalado, `mvn spring-boot:run` também funciona. A API escuta `http://localhost:8080`; a primeira execução aplica migrations Flyway.

## Validar

```powershell
.\mvnw.cmd test
.\mvnw.cmd package
```

Os testes focam nas regras de conflito de agenda e orçamento. O datasource é MySQL local e precisa estar acessível para iniciar a aplicação.

## Organização

- `controller/`: endpoints REST e autorização por perfil.
- `service/`: validações e regras de negócio.
- `repository/`: consultas JPA.
- `dto/`: contratos de entrada e saída.
- `domain/`: entidades e enums persistidos.
- `security/`: login, JWT e filtro stateless.
- `config/`: segurança, CORS e seed de desenvolvimento.
- `exception/`: tratamento HTTP global.
- `src/main/resources/db/migration/`: schema versionado.
