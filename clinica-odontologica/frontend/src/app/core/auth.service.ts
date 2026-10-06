import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';

export interface SessionUser { id: number; name: string; email: string; role: 'ADMIN' | 'DENTISTA' | 'RECEPCIONISTA'; }
interface LoginResponse extends SessionUser { token: string; type: string; }

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly storageKey = 'odonto.session';
  readonly user = signal<SessionUser | null>(this.restoreUser());
  constructor(private readonly http: HttpClient) {}
  get token(): string | null { return this.readSession()?.token ?? null; }
  login(email: string, password: string) {
    return this.http.post<LoginResponse>('/api/auth/login', { email, password }).pipe(tap(session => {
      sessionStorage.setItem(this.storageKey, JSON.stringify(session));
      this.user.set({ id: session.id, name: session.name, email: session.email, role: session.role });
    }));
  }
  logout(): void { sessionStorage.removeItem(this.storageKey); this.user.set(null); }
  hasRole(roles: string[]): boolean { return !!this.user() && roles.includes(this.user()!.role); }
  private readSession(): LoginResponse | null {
    try { return JSON.parse(sessionStorage.getItem(this.storageKey) ?? 'null') as LoginResponse | null; }
    catch { sessionStorage.removeItem(this.storageKey); return null; }
  }
  private restoreUser(): SessionUser | null {
    const session = this.readSession();
    if (!session?.token) return null;
    return { id: session.id, name: session.name, email: session.email, role: session.role };
  }
}
