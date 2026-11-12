import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../services/order.service';
import { RatingService } from '../../services/rating.service';
import { Order } from '../../models/models';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html'
})
export class OrdersComponent implements OnInit {
  orders: Order[] = [];
  loading = false;
  error = '';

  // Feedback form state, keyed by order id
  feedbackOpenFor: number | null = null;
  feedbackValue: { [orderId: number]: number } = {};
  feedbackText: { [orderId: number]: string } = {};
  feedbackSubmitted: { [orderId: number]: boolean } = {};
  feedbackError = '';

  constructor(private orderService: OrderService, private ratingService: RatingService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.loading = true;
    this.orderService.myOrders().subscribe({
      next: (res) => {
        this.orders = res.data || [];
        this.loading = false;
      },
      error: () => {
        this.error = 'Could not load orders';
        this.loading = false;
      }
    });
  }

  cancel(id: number) {
    this.orderService.cancel(id).subscribe({ next: () => this.load() });
  }

  openFeedback(orderId: number) {
    this.feedbackError = '';
    this.feedbackOpenFor = this.feedbackOpenFor === orderId ? null : orderId;
    if (!this.feedbackValue[orderId]) {
      this.feedbackValue[orderId] = 5;
    }
  }

  submitFeedback(orderId: number) {
    this.feedbackError = '';
    this.ratingService.create({
      orderId,
      ratingValue: this.feedbackValue[orderId] || 5,
      feedbackText: this.feedbackText[orderId] || ''
    }).subscribe({
      next: () => {
        this.feedbackSubmitted[orderId] = true;
        this.feedbackOpenFor = null;
      },
      error: (err) => {
        this.feedbackError = err?.error?.message || 'Could not submit feedback';
      }
    });
  }
}
