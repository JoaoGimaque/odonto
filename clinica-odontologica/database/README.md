# Banco de dados MySQL

Use MySQL Server 8 local. Crie um banco e um usuário de aplicação com permissões restritas:

```sql
CREATE DATABASE clinica_odontologica CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'clinica_app'@'localhost' IDENTIFIED BY 'defina-uma-senha-local';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX, REFERENCES ON clinica_odontologica.* TO 'clinica_app'@'localhost';
FLUSH PRIVILEGES;
```

Configure `DB_URL`, `DB_USERNAME=clinica_app` e `DB_PASSWORD` na sessão que inicia o backend. A URL padrão permite criar o schema quando a conta possuir essa permissão. Flyway executa `V1__schema.sql` automaticamente; Hibernate valida o schema (`ddl-auto=validate`).

O seed de desenvolvimento cria registros demonstrativos fictícios, nunca dados clínicos reais. O administrador de teste só é criado se `ADMIN_PASSWORD` não estiver vazia.
