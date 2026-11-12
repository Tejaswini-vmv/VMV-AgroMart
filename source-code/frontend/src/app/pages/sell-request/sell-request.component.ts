import { Component, OnInit } from '@angular/core';
import { SellRequestService } from '../../services/sell-request.service';
import { DemandRequestService } from '../../services/demand-request.service';
import { SellRequest, DemandRequest } from '../../models/models';

@Component({
  selector: 'app-sell-request',
  templateUrl: './sell-request.component.html'
})
export class SellRequestComponent implements OnInit {
  requests: SellRequest[] = [];
  loading = false;
  error = '';
  success = '';
  submitting = false;
  showForm = false;

  demandRequests: DemandRequest[] = [];
  loadingDemandRequests = false;
  respondingPrice: { [id: number]: number | null } = {};

  categories = ['VEGETABLES', 'FRUITS', 'GRAINS', 'PULSES', 'DAIRY', 'SPICES', 'HERBS', 'NUTS'];
  units = ['KG', 'GRAM', 'PIECE', 'DOZEN', 'LITER', 'QUINTAL'];

  form = {
    productName: '',
    category: 'VEGETABLES',
    unitOfMeasure: 'KG',
    description: '',
    quantity: null as number | null,
    proposedPricePerUnit: null as number | null,
    productImageUrl: '',
    isOrganic: false,
    harvestDate: ''
  };

  constructor(private sellRequestService: SellRequestService, private demandRequestService: DemandRequestService) {}

  ngOnInit() {
    this.load();
    this.loadDemandRequests();
  }

  loadDemandRequests() {
    this.loadingDemandRequests = true;
    this.demandRequestService.mine().subscribe({
      next: (res) => { this.demandRequests = res.data || []; this.loadingDemandRequests = false; },
      error: () => { this.loadingDemandRequests = false; }
    });
  }

  respondToDemand(id: number, accept: boolean) {
    this.error = '';
    this.success = '';
    const payload: any = { accept };
    if (accept && this.respondingPrice[id]) {
      payload.proposedPricePerUnit = this.respondingPrice[id];
    }
    this.demandRequestService.respond(id, payload).subscribe({
      next: () => {
        this.success = accept ? 'Accepted — a sell request was created for your city admin to review' : 'Declined';
        this.loadDemandRequests();
        this.load();
      },
      error: (err) => { this.error = err?.error?.message || 'Could not respond to this request'; }
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

  load() {
    this.loading = true;
    this.sellRequestService.mine().subscribe({
      next: (res) => {
        this.requests = res.data || [];
        this.loading = false;
      },
      error: () => {
        this.error = 'Could not load your sell requests';
        this.loading = false;
      }
    });
  }

  submit() {
    this.error = '';
    this.success = '';
    if (!this.form.productName || !this.form.quantity || !this.form.proposedPricePerUnit) {
      this.error = 'Please fill in product name, quantity and price';
      return;
    }
    this.submitting = true;
    this.sellRequestService.create(this.form).subscribe({
      next: () => {
        this.submitting = false;
        this.success = 'Sell request submitted to your city admin';
        this.showForm = false;
        this.resetForm();
        this.load();
      },
      error: (err) => {
        this.submitting = false;
        this.error = err?.error?.message || 'Could not submit sell request';
      }
    });
  }

  resetForm() {
    this.form = {
      productName: '', category: 'VEGETABLES', unitOfMeasure: 'KG', description: '',
      quantity: null, proposedPricePerUnit: null, productImageUrl: '', isOrganic: false, harvestDate: ''
    };
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
