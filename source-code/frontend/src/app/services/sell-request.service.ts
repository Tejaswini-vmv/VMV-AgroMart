import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse, SellRequest, SellRequestDashboard } from '../models/models';

@Injectable({ providedIn: 'root' })
export class SellRequestService {
  private base = `${environment.apiUrl}/sell-requests`;

  constructor(private http: HttpClient) {}

  // Farmer
  create(payload: any): Observable<ApiResponse<SellRequest>> {
    return this.http.post<ApiResponse<SellRequest>>(this.base, payload);
  }

  mine(): Observable<ApiResponse<SellRequest[]>> {
    return this.http.get<ApiResponse<SellRequest[]>>(`${this.base}/mine`);
  }

  // City Admin
  forCity(status?: string): Observable<ApiResponse<SellRequest[]>> {
    const url = status ? `${this.base}/city?status=${status}` : `${this.base}/city`;
    return this.http.get<ApiResponse<SellRequest[]>>(url);
  }

  scheduleVisit(id: number, payload: any): Observable<ApiResponse<SellRequest>> {
    return this.http.put<ApiResponse<SellRequest>>(`${this.base}/${id}/schedule-visit`, payload);
  }

  approve(id: number, payload: any): Observable<ApiResponse<SellRequest>> {
    return this.http.put<ApiResponse<SellRequest>>(`${this.base}/${id}/approve`, payload);
  }

  reject(id: number, payload: any): Observable<ApiResponse<SellRequest>> {
    return this.http.put<ApiResponse<SellRequest>>(`${this.base}/${id}/reject`, payload);
  }

  buy(id: number): Observable<ApiResponse<SellRequest>> {
    return this.http.put<ApiResponse<SellRequest>>(`${this.base}/${id}/buy`, {});
  }

  // Super Admin
  incoming(): Observable<ApiResponse<SellRequest[]>> {
    return this.http.get<ApiResponse<SellRequest[]>>(`${this.base}/incoming`);
  }

  listToCatalog(id: number): Observable<ApiResponse<SellRequest>> {
    return this.http.put<ApiResponse<SellRequest>>(`${this.base}/${id}/list`, {});
  }

  all(status?: string): Observable<ApiResponse<SellRequest[]>> {
    const url = status ? `${this.base}/all?status=${status}` : `${this.base}/all`;
    return this.http.get<ApiResponse<SellRequest[]>>(url);
  }

  dashboard(): Observable<ApiResponse<SellRequestDashboard>> {
    return this.http.get<ApiResponse<SellRequestDashboard>>(`${this.base}/dashboard`);
  }
}
