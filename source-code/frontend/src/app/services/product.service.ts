import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse, Product } from '../models/models';

@Injectable({ providedIn: 'root' })
export class ProductService {
  constructor(private http: HttpClient) {}

  list(category?: string): Observable<ApiResponse<Product[]>> {
    const url = category ? `${environment.apiUrl}/products?category=${category}` : `${environment.apiUrl}/products`;
    return this.http.get<ApiResponse<Product[]>>(url);
  }

  search(query: string): Observable<ApiResponse<Product[]>> {
    return this.http.get<ApiResponse<Product[]>>(`${environment.apiUrl}/products/search?q=${encodeURIComponent(query)}`);
  }

  get(id: number): Observable<ApiResponse<Product>> {
    return this.http.get<ApiResponse<Product>>(`${environment.apiUrl}/products/${id}`);
  }

  create(payload: any): Observable<ApiResponse<Product>> {
    return this.http.post<ApiResponse<Product>>(`${environment.apiUrl}/products`, payload);
  }

  delete(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${environment.apiUrl}/products/${id}`);
  }
}
