import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse, Rating } from '../models/models';

@Injectable({ providedIn: 'root' })
export class RatingService {
  constructor(private http: HttpClient) {}

  create(payload: any): Observable<ApiResponse<Rating>> {
    return this.http.post<ApiResponse<Rating>>(`${environment.apiUrl}/ratings`, payload);
  }

  forProduct(productId: number): Observable<ApiResponse<Rating[]>> {
    return this.http.get<ApiResponse<Rating[]>>(`${environment.apiUrl}/ratings/product/${productId}`);
  }
}
