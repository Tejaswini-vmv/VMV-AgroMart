import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../services/order.service';
import { AuthService } from '../../services/auth.service';
import { Order } from '../../models/models';

// Delivery Provider's "last mile" view: orders in their own city only, with a
// restricted set of actions (mark Out for Delivery / Delivered) instead of the
// full status dropdown Storage Admin / Super Admin get.
@Component({
  selector: 'app-delivery-provider',
  templateUrl: './delivery-provider.component.html'
})
export class DeliveryProviderComponent implements OnInit {
  city = '';
  orders: Order[] = [];
  loading = false;
  error = '';
  success = '';

  constructor(private orderService: OrderService, private auth: AuthService) {}

  ngOnInit() {
    this.city = this.auth.currentUserValue?.city || '';
    this.loadOrders();
  }

  loadOrders() {
    this.loading = true;
    this.error = '';
    this.orderService.ordersForCity().subscribe({
      next: (res) => { this.orders = res.data || []; this.loading = false; },
      error: (err) => { this.loading = false; this.error = err?.error?.message || 'Could not load orders'; }
    });
  }

  markOutForDelivery(order: Order) {
    this.setStatus(order, 'OUT_FOR_DELIVERY');
  }

  markDelivered(order: Order) {
    this.setStatus(order, 'DELIVERED');
  }

  private setStatus(order: Order, status: string) {
    this.error = '';
    this.success = '';
    this.orderService.updateStatus(order.id, status).subscribe({
      next: (res) => {
        order.orderStatus = res.data.orderStatus;
        this.success = `Order ${order.orderNumber} marked ${res.data.orderStatus}`;
      },
      error: (err) => { this.error = err?.error?.message || 'Could not update order status'; }
    });
  }

  orderStatusColor(status: string): string {
    switch (status) {
      case 'PENDING': return '#8a8a00';
      case 'CONFIRMED': return '#1565c0';
      case 'PROCESSING': return '#ef6c00';
      case 'OUT_FOR_DELIVERY': return '#6a1b9a';
      case 'DELIVERED': return '#2e7d32';
      case 'CANCELLED': return '#c62828';
      case 'RETURNED': return '#795548';
      default: return '#555';
    }
  }
}
