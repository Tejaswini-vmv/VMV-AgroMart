import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse, FarmerSummary } from '../models/models';

@Injectable({ providedIn: 'root' })
export class UserService {
  constructor(private http: HttpClient) {}

  farmers(): Observable<ApiResponse<FarmerSummary[]>> {
    return this.http.get<ApiResponse<FarmerSummary[]>>(`${environment.apiUrl}/users/farmers`);
  }
}
