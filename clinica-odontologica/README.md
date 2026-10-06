# Sorriso&Co. | Gestão Odontológica

Sistema full stack para operação de clínica odontológica, com frontend Angular, API Spring Boot e persistência MySQL versionada por Flyway.

## Pré-requisitos

- Java JDK 17.
- Node.js 20.11 ou superior e npm. O scaffold funciona com Angular CLI 18; versões anteriores do Node podem emitir avisos de compatibilidade.
- MySQL Server 8 em execução.
- Navegador atualizado.
- Acesso à internet na primeira execução para baixar dependências Maven/npm.

O backend inclui Maven Wrapper (`mvnw.cmd`), portanto Maven global é opcional. Para usar exatamente `mvn spring-boot:run`, instale Maven 3.9+ e execute dentro de `backend/`.

## Banco de dados

Siga [`database/README.md`](database/README.md) para criar banco e usuário local. Flyway cria as tabelas ao iniciar a API. Não carregue dados clínicos reais neste ambiente.

## Backend

Abra um PowerShell em `clinica-odontologica/backend` e defina as configurações para a sessão atual:

```powershell
$env:DB_URL = 'jdbc:mysql://localhost:3306/clinica_odontologica?createDatabaseIfNotExist=true&serverTimezone=America/Sao_Paulo'
$env:DB_USERNAME = 'clinica_app'
$env:DB_PASSWORD = 'defina-sua-senha-local-do-mysql'
$env:ADMIN_EMAIL = 'admin@clinica.local'
$env:ADMIN_PASSWORD = 'defina-uma-senha-local-para-o-admin'
$env:JWT_SECRET = 'defina-uma-chave-local-com-pelo-menos-32-caracteres'
```

Substitua os valores de exemplo localmente. Não versionamos credenciais. Inicie a API com Maven global ou com o wrapper incluído:

```powershell
.\mvnw.cmd spring-boot:run
```

API: `http://localhost:8080`; health check: `http://localhost:8080/actuator/health`.

Para validar o backend:

```powershell
.\mvnw.cmd test
.\mvnw.cmd package
```

## Frontend

Em outro terminal, na pasta `clinica-odontologica/frontend`:

```powershell
npm install
npx ng serve
```

Acesse `http://localhost:4200`. O proxy de desenvolvimento encaminha `/api` e `/actuator` para `localhost:8080`. Build: `npm run build`.

Teste unitário do frontend: `npm test -- --watch=false --browsers=ChromeHeadless`.

## Usuário de teste

- E-mail: `admin@clinica.local`
- Senha: o valor definido em `ADMIN_PASSWORD` antes da primeira inicialização.
- Dentista: `marina@clinica.local`
- Senha do dentista: o valor definido em `DENTIST_PASSWORD` (padrão local `1234`).

Essas contas são criadas somente pelo seed de desenvolvimento, se ainda não existirem. Configure senhas próprias em ambientes compartilhados. A seed também inclui dois dentistas, cinco pacientes fictícios, procedimentos de exemplo, consultas e um pagamento.

## Perfis

- `ADMIN`: acesso administrativo completo.
- `DENTISTA`: pacientes, agenda, prontuário e odontograma; leitura de dentistas/procedimentos para apoio aos atendimentos.
- `RECEPCIONISTA`: pacientes, agenda, orçamentos e pagamentos; leitura de dentistas/procedimentos para seleção.

As permissões são aplicadas no backend e na navegação do frontend. Para criar outros usuários, por ora insira registros em `app_user` usando hashes BCrypt; não existe uma tela de gestão de contas.

## Principais endpoints

- `POST /api/auth/login`
- `GET|POST|PUT|DELETE /api/pacientes` e `GET /api/pacientes/{id}`
- `GET|POST|PUT|DELETE /api/dentistas`
- `GET|POST|PUT|DELETE /api/consultas` (DELETE cancela sem remover histórico)
- `GET /api/dashboard`
- `GET /api/prontuarios/paciente/{patientId}`, `POST|PUT /api/prontuarios`
- `GET /api/odontograma/paciente/{patientId}`, `POST /api/odontograma`
- `GET|POST|PUT|DELETE /api/procedimentos` (DELETE inativa)
- `GET|POST /api/orcamentos`, `PUT /api/orcamentos/{id}/status`
- `GET|POST /api/pagamentos`

Paginação de pacientes/dentistas: `?q=termo&page=0&size=20`. Agenda: `?from=AAAA-MM-DD&to=AAAA-MM-DD`.

## Estrutura

```text
clinica-odontologica/
├── backend/       API Java 17, Spring Boot, Maven, JWT, JPA e Flyway
├── frontend/      Angular 18, Material, Reactive Forms, guards e interceptor
├── database/      instruções para banco MySQL local
└── README.md
```

Detalhes adicionais: [`backend/README.md`](backend/README.md), [`frontend/README.md`](frontend/README.md) e [`database/README.md`](database/README.md).

## Notas de escopo e segurança

- Exclusão de pacientes e dentistas é feita por inativação para preservar vínculos clínicos e financeiros; exclusão de consultas cancela o evento.
- JWT é stateless; logout remove o token da sessão do navegador. Configure segredo forte fora do código em qualquer ambiente compartilhado.
- O odontograma inicial utiliza numeração FDI e registra condição/observação por dente.
- A lista de endpoints está implementada; testes unitários cobrem sobreposição de agenda e cálculo/validação de orçamento. A inicialização integrada requer credenciais válidas do MySQL local.
