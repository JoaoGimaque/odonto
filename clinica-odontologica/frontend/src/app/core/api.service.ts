import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PageResult<T> { content: T[]; totalElements: number; totalPages: number; number: number; size: number; }
export interface Patient { id: number; name: string; cpf: string; birthDate?: string; gender?: string; phone?: string; email?: string; address?: string; notes?: string; active: boolean; createdAt?: string; }
export interface Dentist { id: number; name: string; cro: string; specialty: string; phone?: string; email: string; active: boolean; }
export interface Appointment { id: number; patientId: number; patientName: string; dentistId: number; dentistName: string; startsAt: string; endsAt: string; type: string; notes?: string; status: string; }
export interface Procedure { id: number; name: string; description?: string; price: number; durationMinutes: number; active: boolean; }

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private readonly http: HttpClient) {}
  get<T>(path: string, params?: Record<string, string | number>): Observable<T> {
    let httpParams = new HttpParams();
    Object.entries(params ?? {}).forEach(([key, value]) => httpParams = httpParams.set(key, value));
    return this.http.get<T>(`/api/${path}`, { params: httpParams });
  }
  post<T>(path: string, body: unknown): Observable<T> { return this.http.post<T>(`/api/${path}`, body); }
  put<T>(path: string, body: unknown): Observable<T> { return this.http.put<T>(`/api/${path}`, body); }
  delete(path: string): Observable<void> { return this.http.delete<void>(`/api/${path}`); }
}
