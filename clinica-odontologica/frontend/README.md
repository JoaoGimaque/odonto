# Frontend

Angular 18 standalone com TypeScript, SCSS, Angular Material, Reactive Forms, HttpClient e Router. Guards protegem as rotas privadas; o interceptor anexa JWT e encerra a sessão quando o token expira.

## Executar

Requer Node.js 20.11+ e npm. Na pasta `frontend/`:

```powershell
npm install
npx ng serve
```

Acesse `http://localhost:4200`. O proxy de desenvolvimento encaminha `/api` e `/actuator` para `http://localhost:8080`; inicie o backend para usar login e dados.

## Validar e compilar

```powershell
npm run build
npm test -- --watch=false --browsers=ChromeHeadless
```

## Estrutura

- `src/app/core/`: sessão, guardas, interceptor e cliente HTTP.
- `src/app/features/auth/`: formulário reativo de login.
- `src/app/features/workspace/`: shell responsivo e seções do sistema.
- `src/app/shared/`: feedback de sucesso/erro.
- `proxy.conf.json`: proxy local da API.
