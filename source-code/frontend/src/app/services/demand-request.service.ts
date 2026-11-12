import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse, DemandRequest } from '../models/models';

@Injectable({ providedIn: 'root' })
export class DemandRequestService {
  private base = `${environment.apiUrl}/demand-requests`;

  constructor(private http: HttpClient) {}

  // City Admin / Super Admin
  create(payload: any): Observable<ApiResponse<DemandRequest>> {
    return this.http.post<ApiResponse<DemandRequest>>(this.base, payload);
  }

  createdByMe(): Observable<ApiResponse<DemandRequest[]>> {
    return this.http.get<ApiResponse<DemandRequest[]>>(`${this.base}/created`);
  }

  cancel(id: number): Observable<ApiResponse<DemandRequest>> {
    return this.http.put<ApiResponse<DemandRequest>>(`${this.base}/${id}/cancel`, {});
  }

  // Super Admin
  all(): Observable<ApiResponse<DemandRequest[]>> {
    return this.http.get<ApiResponse<DemandRequest[]>>(`${this.base}/all`);
  }

  // Farmer
  mine(): Observable<ApiResponse<DemandRequest[]>> {
    return this.http.get<ApiResponse<DemandRequest[]>>(`${this.base}/mine`);
  }

  respond(id: number, payload: any): Observable<ApiResponse<DemandRequest>> {
    return this.http.put<ApiResponse<DemandRequest>>(`${this.base}/${id}/respond`, payload);
  }
}
