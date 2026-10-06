import { Routes } from '@angular/router';
import { authGuard, guestGuard } from './core/auth.guards';

export const routes: Routes = [
	{ path: 'login', loadComponent: () => import('./features/auth/login.component').then(m => m.LoginComponent), canActivate: [guestGuard] },
	{ path: '', loadComponent: () => import('./features/workspace/shell.component').then(m => m.ShellComponent), canActivate: [authGuard], children: [
		{ path: '', loadComponent: () => import('./features/workspace/workspace.component').then(m => m.WorkspaceComponent), data: { section: 'dashboard' } },
		{ path: ':section', loadComponent: () => import('./features/workspace/workspace.component').then(m => m.WorkspaceComponent) }
	] },
	{ path: '**', redirectTo: '' }
];
