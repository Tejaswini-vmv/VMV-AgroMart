import { Component, OnInit } from '@angular/core';
import { SellRequestService } from '../../services/sell-request.service';
import { OrderService } from '../../services/order.service';
import { UserService } from '../../services/user.service';
import { DemandRequestService } from '../../services/demand-request.service';
import { SellRequest, SellRequestDashboard, Order, FarmerSummary, DemandRequest } from '../../models/models';

@Component({
  selector: 'app-super-admin',
  templateUrl: './super-admin.component.html'
})
export class SuperAdminComponent implements OnInit {
  dashboard: SellRequestDashboard | null = null;
  incoming: SellRequest[] = [];
  allRequests: SellRequest[] = [];
  statusFilter = '';

  orders: Order[] = [];
  loadingOrders = false;
  orderStatusOptions = ['PENDING', 'CONFIRMED', 'PROCESSING', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED', 'RETURNED'];

  loadingDashboard = false;
  loadingIncoming = false;
  loadingAll = false;
  error = '';
  success = '';

  farmers: FarmerSummary[] = [];
  loadingFarmers = false;

  demandRequests: DemandRequest[] = [];
  loadingDemandRequests = false;

  statusKeys(): string[] {
    return this.dashboard ? Object.keys(this.dashboard.sellRequestsByStatus) : [];
  }

  constructor(
    private sellRequestService: SellRequestService,
    private orderService: OrderService,
    private userService: UserService,
    private demandRequestService: DemandRequestService
  ) {}

  ngOnInit() {
    this.loadDashboard();
    this.loadIncoming();
    this.loadAll();
    this.loadOrders();
    this.loadFarmers();
    this.loadDemandRequests();
  }

  loadFarmers() {
    this.loadingFarmers = true;
    this.userService.farmers().subscribe({
      next: (res) => { this.farmers = res.data || []; this.loadingFarmers = false; },
      error: () => { this.loadingFarmers = false; }
    });
  }

  loadDemandRequests() {
    this.loadingDemandRequests = true;
    this.demandRequestService.all().subscribe({
      next: (res) => { this.demandRequests = res.data || []; this.loadingDemandRequests = false; },
      error: () => { this.loadingDemandRequests = false; }
    });
  }

  demandStatusColor(status: string): string {
    switch (status) {
      case 'OPEN': return '#8a8a00';
      case 'ACCEPTED': return '#2e7d32';
      case 'DECLINED': return '#c62828';
      case 'CANCELLED': return '#555';
      default: return '#555';
    }
  }

  // Traceability helper: farmer(s) and city admin(s) an order's produce was sourced from.
  orderFarmers(order: Order): string {
    const names = new Set<string>();
    (order.items || []).forEach((i: any) => { if (i.product?.farmer?.fullName) { names.add(i.product.farmer.fullName); } });
    return names.size ? Array.from(names).join(', ') : '—';
  }

  orderSourcedBy(order: Order): string {
    const names = new Set<string>();
    (order.items || []).forEach((i: any) => { if (i.product?.sourceCityAdmin?.fullName) { names.add(i.product.sourceCityAdmin.fullName); } });
    return names.size ? Array.from(names).join(', ') : '—';
  }

  loadOrders() {
    this.loadingOrders = true;
    this.orderService.allOrders().subscribe({
      next: (res) => { this.orders = res.data || []; this.loadingOrders = false; },
      error: () => { this.loadingOrders = false; }
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
        this.loadDashboard();
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

  loadDashboard() {
    this.loadingDashboard = true;
    this.sellRequestService.dashboard().subscribe({
      next: (res) => { this.dashboard = res.data; this.loadingDashboard = false; },
      error: () => { this.loadingDashboard = false; }
    });
  }

  loadIncoming() {
    this.loadingIncoming = true;
    this.sellRequestService.incoming().subscribe({
      next: (res) => { this.incoming = res.data || []; this.loadingIncoming = false; },
      error: () => { this.loadingIncoming = false; }
    });
  }

  loadAll() {
    this.loadingAll = true;
    this.sellRequestService.all(this.statusFilter || undefined).subscribe({
      next: (res) => { this.allRequests = res.data || []; this.loadingAll = false; },
      error: () => { this.loadingAll = false; }
    });
  }

  listToCatalog(id: number) {
    this.error = '';
    this.success = '';
    this.sellRequestService.listToCatalog(id).subscribe({
      next: () => {
        this.success = 'Product published to the public catalog';
        this.loadIncoming();
        this.loadAll();
        this.loadDashboard();
      },
      error: (err) => { this.error = err?.error?.message || 'Could not list product'; }
    });
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
