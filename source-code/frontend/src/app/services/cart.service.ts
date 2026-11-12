import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { CartItem, Product } from '../models/models';

const CART_KEY = 'vmv_cart';

@Injectable({ providedIn: 'root' })
export class CartService {
  private itemsSubject = new BehaviorSubject<CartItem[]>(this.loadFromStorage());
  items$ = this.itemsSubject.asObservable();

  private loadFromStorage(): CartItem[] {
    try {
      const raw = localStorage.getItem(CART_KEY);
      return raw ? JSON.parse(raw) : [];
    } catch {
      return [];
    }
  }

  private persist(items: CartItem[]) {
    localStorage.setItem(CART_KEY, JSON.stringify(items));
  }

  add(product: Product, quantity: number) {
    const items = [...this.itemsSubject.value];
    const existing = items.find(i => i.product.id === product.id);
    if (existing) {
      existing.quantity += quantity;
    } else {
      items.push({ product, quantity });
    }
    this.itemsSubject.next(items);
    this.persist(items);
  }

  remove(productId: number) {
    const items = this.itemsSubject.value.filter(i => i.product.id !== productId);
    this.itemsSubject.next(items);
    this.persist(items);
  }

  clear() {
    this.itemsSubject.next([]);
    this.persist([]);
  }

  get items(): CartItem[] {
    return this.itemsSubject.value;
  }

  get total(): number {
    return this.itemsSubject.value.reduce((sum, i) => sum + i.product.pricePerUnit * i.quantity, 0);
  }
}
