import { Component, computed, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { DatePipe } from '@angular/common';
import { BreakpointObserver } from '@angular/cdk/layout';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { AuthService } from '../../core/auth.service';

interface NavItem { label: string; path: string; icon: string; roles: string[]; }
@Component({ selector: 'app-shell', standalone: true, imports: [DatePipe, RouterLink, RouterLinkActive, RouterOutlet, MatSidenavModule, MatIconModule, MatButtonModule, MatTooltipModule], templateUrl: './shell.component.html', styleUrl: './shell.component.scss' })
export class ShellComponent {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly breakpointObserver = inject(BreakpointObserver);
  readonly user = this.auth.user;
  readonly today = new Date();
  readonly isMobile = signal(false);
  readonly mobileOpen = signal(false);
  readonly drawerMode = computed(() => this.isMobile() ? 'over' as const : 'side' as const);
  readonly navItems: NavItem[] = [
    { label:'Visão geral', path:'/', icon:'grid_view', roles:['ADMIN','DENTISTA','RECEPCIONISTA'] },
    { label:'Agenda', path:'/agenda', icon:'calendar_month', roles:['ADMIN','DENTISTA','RECEPCIONISTA'] },
    { label:'Pacientes', path:'/pacientes', icon:'group', roles:['ADMIN','DENTISTA','RECEPCIONISTA'] },
    { label:'Dentistas', path:'/dentistas', icon:'medical_services', roles:['ADMIN'] },
    { label:'Prontuários', path:'/prontuarios', icon:'description', roles:['ADMIN','DENTISTA'] },
    { label:'Odontograma', path:'/odontograma', icon:'dentistry', roles:['ADMIN','DENTISTA'] },
    { label:'Procedimentos', path:'/procedimentos', icon:'healing', roles:['ADMIN'] },
    { label:'Orçamentos', path:'/orcamentos', icon:'request_quote', roles:['ADMIN','RECEPCIONISTA'] },
    { label:'Financeiro', path:'/financeiro', icon:'payments', roles:['ADMIN','RECEPCIONISTA'] },
    { label:'Configurações', path:'/configuracoes', icon:'settings', roles:['ADMIN'] }
  ];
  readonly visibleItems = computed(() => this.navItems.filter(item => this.auth.hasRole(item.roles)));
  constructor() {
    this.breakpointObserver.observe('(max-width: 760px)')
      .pipe(takeUntilDestroyed())
      .subscribe(({ matches }) => {
        this.isMobile.set(matches);
        if (!matches) this.mobileOpen.set(false);
      });
  }
  get sectionName(): string {
    const path = this.router.url.split('?')[0].split('/')[1] || '';
    return this.navItems.find(item => item.path.replace('/','') === path)?.label ?? 'Visão geral';
  }
  logout(): void { this.auth.logout(); this.router.navigateByUrl('/login'); }
}
