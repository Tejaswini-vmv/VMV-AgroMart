import { Component, OnInit } from '@angular/core';
import { SellRequestService } from '../../services/sell-request.service';
import { OrderService } from '../../services/order.service';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';
import { SellRequest, Order, FarmerSummary } from '../../models/models';

// Storage Admin gets the same kind of view as Super Admin (sell requests, farmers,
// orders with status control) but scoped to their own city only — one Storage Admin
// per city, seeing only the requests/orders/farmers relevant to that city.
@Component({
  selector: 'app-storage-admin',
  templateUrl: './storage-admin.component.html'
})
export class StorageAdminComponent implements OnInit {
  city = '';

  allRequests: SellRequest[] = [];
  statusFilter = '';
  loadingAll = false;

  farmers: FarmerSummary[] = [];
  loadingFarmers = false;

  orders: Order[] = [];
  loadingOrders = false;
  orderStatusOptions = ['PENDING', 'CONFIRMED', 'PROCESSING', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED', 'RETURNED'];

  error = '';
  success = '';

  constructor(
    private sellRequestService: SellRequestService,
    private orderService: OrderService,
    private userService: UserService,
    private auth: AuthService
  ) {}

  ngOnInit() {
    this.city = this.auth.currentUserValue?.city || '';
    this.loadAll();
    this.loadFarmers();
    this.loadOrders();
  }

  get totalSellRequests(): number { return this.allRequests.length; }
  get totalOrders(): number { return this.orders.length; }
  get totalOrderRevenue(): number {
    return this.orders.reduce((sum, o: any) => sum + (Number(o.totalAmount) || 0), 0);
  }
  get sellRequestsByStatus(): { [key: string]: number } {
    const counts: { [key: string]: number } = {};
    for (const r of this.allRequests) {
      counts[r.status] = (counts[r.status] || 0) + 1;
    }
    return counts;
  }
  statusKeys(): string[] { return Object.keys(this.sellRequestsByStatus); }

  loadAll() {
    this.loadingAll = true;
    this.error = '';
    this.sellRequestService.forCity(this.statusFilter || undefined).subscribe({
      next: (res) => { this.allRequests = res.data || []; this.loadingAll = false; },
      error: (err) => { this.loadingAll = false; this.error = err?.error?.message || 'Could not load sell requests'; }
    });
  }

  loadFarmers() {
    this.loadingFarmers = true;
    this.userService.farmers().subscribe({
      next: (res) => { this.farmers = res.data || []; this.loadingFarmers = false; },
      error: () => { this.loadingFarmers = false; }
    });
  }

  loadOrders() {
    this.loadingOrders = true;
    this.orderService.ordersForCity().subscribe({
      next: (res) => { this.orders = res.data || []; this.loadingOrders = false; },
      error: (err) => { this.loadingOrders = false; this.error = err?.error?.message || 'Could not load orders'; }
    });
  }

  changeOrderStatus(order: Order, newStatus: string) {
    if (!newStatus || newStatus === order.orderStatus) { return; }
    this.error = '';
    this.success = '';
    this.orderService.updateStatus(order.id, newStatus).subscribe({
      next: (res) => {
        order.orderStatus = res.data.orderStatus;
        this.success = `Order ${order.orderNumber} updated to ${res.data.orderStatus}`;
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

  statusColor(status: string): string {
    switch (status) {
      case 'SUBMITTED': return '#8a8a00';
      case 'VISIT_SCHEDULED': return '#1565c0';
      case 'APPROVED': return '#2e7d32';
      case 'REJECTED': return '#c62828';
      case 'PURCHASED': return '#6a1b9a';
      case 'LISTED': return '#00838f';
      default: return '#555';
    }
  }
}
