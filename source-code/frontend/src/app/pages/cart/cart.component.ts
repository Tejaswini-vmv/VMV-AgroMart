import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CartService } from '../../services/cart.service';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html'
})
export class CartComponent {
  deliveryAddress = '';
  deliveryCity = '';
  deliveryPincode = '';
  deliveryContactPhone = '';
  paymentMethod = 'COD';
  requestedDeliveryDate = '';
  requestedDeliveryTimeSlot = '';
  timeSlots = ['08:00-10:00', '10:00-12:00', '12:00-14:00', '14:00-16:00', '16:00-18:00', '18:00-20:00'];
  loading = false;
  error = '';
  success = '';

  constructor(public cart: CartService, private orderService: OrderService, private router: Router) {}

  remove(productId: number) {
    this.cart.remove(productId);
  }

  placeOrder() {
    if (this.cart.items.length === 0) {
      this.error = 'Your cart is empty';
      return;
    }
    if (!this.deliveryAddress) {
      this.error = 'Delivery address is required';
      return;
    }

    this.loading = true;
    this.error = '';

    const payload = {
      items: this.cart.items.map(i => ({ productId: i.product.id, quantity: i.quantity })),
      deliveryAddress: this.deliveryAddress,
      deliveryCity: this.deliveryCity,
      deliveryPincode: this.deliveryPincode,
      deliveryContactPhone: this.deliveryContactPhone,
      paymentMethod: this.paymentMethod,
      requestedDeliveryDate: this.requestedDeliveryDate || null,
      requestedDeliveryTimeSlot: this.requestedDeliveryTimeSlot || null
    };

    this.orderService.place(payload).subscribe({
      next: (res) => {
        this.loading = false;
        this.success = `Order ${res.data.orderNumber} placed!`;
        this.cart.clear();
        setTimeout(() => this.router.navigate(['/orders']), 1200);
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.message || 'Failed to place order';
      }
    });
  }
}
