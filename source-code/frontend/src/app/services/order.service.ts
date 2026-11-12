import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse, Order } from '../models/models';

@Injectable({ providedIn: 'root' })
export class OrderService {
  constructor(private http: HttpClient) {}

  myOrders(): Observable<ApiResponse<Order[]>> {
    return this.http.get<ApiResponse<Order[]>>(`${environment.apiUrl}/orders`);
  }

  allOrders(): Observable<ApiResponse<Order[]>> {
    return this.http.get<ApiResponse<Order[]>>(`${environment.apiUrl}/orders/all`);
  }

  // Storage Admin / Delivery Provider: orders scoped to the caller's own city.
  ordersForCity(): Observable<ApiResponse<Order[]>> {
    return this.http.get<ApiResponse<Order[]>>(`${environment.apiUrl}/orders/city`);
  }

  place(payload: any): Observable<ApiResponse<Order>> {
    return this.http.post<ApiResponse<Order>>(`${environment.apiUrl}/orders`, payload);
  }

  updateStatus(id: number, status: string): Observable<ApiResponse<Order>> {
    return this.http.put<ApiResponse<Order>>(`${environment.apiUrl}/orders/${id}/status`, { status });
  }

  cancel(id: number): Observable<ApiResponse<Order>> {
    return this.http.delete<ApiResponse<Order>>(`${environment.apiUrl}/orders/${id}`);
  }
}
