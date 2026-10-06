import { Injectable, inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({ providedIn: 'root' })
export class ToastService {
  private readonly snack = inject(MatSnackBar);
  success(message: string): void { this.snack.open(message, 'Fechar', { duration: 2800, panelClass: ['toast-success'] }); }
  error(message: string): void { this.snack.open(message, 'Fechar', { duration: 4200, panelClass: ['toast-error'] }); }
}
