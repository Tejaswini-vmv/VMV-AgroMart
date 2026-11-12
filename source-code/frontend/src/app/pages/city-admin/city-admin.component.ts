import { Component, OnInit } from '@angular/core';
import { SellRequestService } from '../../services/sell-request.service';
import { UserService } from '../../services/user.service';
import { DemandRequestService } from '../../services/demand-request.service';
import { SellRequest, FarmerSummary, DemandRequest } from '../../models/models';

type ActionType = 'visit' | 'approve' | 'reject';

@Component({
  selector: 'app-city-admin',
  templateUrl: './city-admin.component.html'
})
export class CityAdminComponent implements OnInit {
  requests: SellRequest[] = [];
  loading = false;
  error = '';
  success = '';
  statusFilter = '';

  activeAction: { id: number; type: ActionType } | null = null;
  visitDate = '';
  visitNotes = '';
  purchaseDate = '';
  agreedPrice: number | null = null;
  rejectReason = '';

  // Farmers directory
  farmers: FarmerSummary[] = [];
  loadingFarmers = false;

  // Raise demand request to a farmer
  showDemandForm = false;
  demandForm = {
    targetFarmerId: null as number | null,
    productName: '',
    category: 'VEGETABLES',
    unitOfMeasure: 'KG',
    quantity: null as number | null,
    offeredPricePerUnit: null as number | null,
    neededByDate: '',
    notes: ''
  };
  categories = ['VEGETABLES', 'FRUITS', 'GRAINS', 'PULSES', 'DAIRY', 'SPICES', 'HERBS', 'NUTS'];
  units = ['KG', 'GRAM', 'PIECE', 'DOZEN', 'LITER', 'QUINTAL'];

  myDemandRequests: DemandRequest[] = [];
  loadingDemandRequests = false;

  constructor(
    private sellRequestService: SellRequestService,
    private userService: UserService,
    private demandRequestService: DemandRequestService
  ) {}

  ngOnInit() {
    this.load();
    this.loadFarmers();
    this.loadMyDemandRequests();
  }

  load() {
    this.loading = true;
    this.error = '';
    this.sellRequestService.forCity(this.statusFilter || undefined).subscribe({
      next: (res) => {
        this.requests = res.data || [];
        this.loading = false;
      },
      error: (err) => {
        this.error = err?.error?.message || 'Could not load requests for your city';
        this.loading = false;
      }
    });
  }

  loadFarmers() {
    this.loadingFarmers = true;
    this.userService.farmers().subscribe({
      next: (res) => { this.farmers = res.data || []; this.loadingFarmers = false; },
      error: () => { this.loadingFarmers = false; }
    });
  }

  loadMyDemandRequests() {
    this.loadingDemandRequests = true;
    this.demandRequestService.createdByMe().subscribe({
      next: (res) => { this.myDemandRequests = res.data || []; this.loadingDemandRequests = false; },
      error: () => { this.loadingDemandRequests = false; }
    });
  }

  toggleDemandForm() {
    this.showDemandForm = !this.showDemandForm;
    this.error = '';
    this.success = '';
  }

  raiseDemandRequest() {
    const f = this.demandForm;
    if (!f.productName || !f.quantity || !f.neededByDate) {
      this.error = 'Product name, quantity and needed-by date are required';
      return;
    }
    this.error = '';
    this.success = '';
    const payload: any = {
      productName: f.productName,
      category: f.category,
      unitOfMeasure: f.unitOfMeasure,
      quantity: f.quantity,
      offeredPricePerUnit: f.offeredPricePerUnit,
      neededByDate: f.neededByDate,
      notes: f.notes
    };
    if (f.targetFarmerId) { payload.targetFarmerId = f.targetFarmerId; }
    this.demandRequestService.create(payload).subscribe({
      next: () => {
        this.success = f.targetFarmerId
          ? 'Demand request sent to the selected farmer'
          : 'Demand request broadcast to all farmers in your city';
        this.showDemandForm = false;
        this.demandForm = { targetFarmerId: null, productName: '', category: 'VEGETABLES', unitOfMeasure: 'KG', quantity: null, offeredPricePerUnit: null, neededByDate: '', notes: '' };
        this.loadMyDemandRequests();
      },
      error: (err) => { this.error = err?.error?.message || 'Could not raise demand request'; }
    });
  }

  cancelDemandRequest(id: number) {
    this.demandRequestService.cancel(id).subscribe({
      next: () => { this.success = 'Demand request cancelled'; this.loadMyDemandRequests(); },
      error: (err) => { this.error = err?.error?.message || 'Could not cancel demand request'; }
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

  openAction(id: number, type: ActionType) {
    this.success = '';
    this.error = '';
    this.activeAction = this.activeAction?.id === id && this.activeAction.type === type ? null : { id, type };
    this.visitDate = '';
    this.visitNotes = '';
    this.purchaseDate = '';
    this.agreedPrice = null;
    this.rejectReason = '';
  }

  scheduleVisit(id: number) {
    if (!this.visitDate) { this.error = 'Pick a visit date'; return; }
    this.sellRequestService.scheduleVisit(id, { visitDate: this.visitDate, visitNotes: this.visitNotes }).subscribe({
      next: () => { this.success = 'Visit scheduled'; this.activeAction = null; this.load(); },
      error: (err) => { this.error = err?.error?.message || 'Could not schedule visit'; }
    });
  }

  approve(id: number) {
    if (!this.purchaseDate || !this.agreedPrice) { this.error = 'Set purchase date and agreed price'; return; }
    this.sellRequestService.approve(id, { purchaseDate: this.purchaseDate, agreedPricePerUnit: this.agreedPrice }).subscribe({
      next: () => { this.success = 'Request approved'; this.activeAction = null; this.load(); },
      error: (err) => { this.error = err?.error?.message || 'Could not approve request'; }
    });
  }

  reject(id: number) {
    if (!this.rejectReason) { this.error = 'Provide a rejection reason'; return; }
    this.sellRequestService.reject(id, { reason: this.rejectReason }).subscribe({
      next: () => { this.success = 'Request rejected'; this.activeAction = null; this.load(); },
      error: (err) => { this.error = err?.error?.message || 'Could not reject request'; }
    });
  }

  buy(id: number) {
    this.sellRequestService.buy(id).subscribe({
      next: () => { this.success = 'Purchased and forwarded to super admin — request is now closed on your end'; this.load(); },
      error: (err) => { this.error = err?.error?.message || 'Could not complete purchase'; }
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
