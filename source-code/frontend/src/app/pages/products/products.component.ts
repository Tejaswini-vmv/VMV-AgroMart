import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';
import { AuthService } from '../../services/auth.service';
import { Product } from '../../models/models';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit {
  products: Product[] = [];
  loading = false;
  error = '';
  quantities: { [id: number]: number } = {};

  // Fallback emoji shown when a product has no productImageUrl, keyed by category.
  private readonly categoryFallback: { [category: string]: string } = {
    VEGETABLES: '🥬',
    FRUITS: '🍎',
    GRAINS: '🌾',
    PULSES: '🫘',
    DAIRY: '🥛',
    SPICES: '🌶️',
    HERBS: '🌿',
    NUTS: '🥜'
  };

  constructor(private productService: ProductService, private cart: CartService, private auth: AuthService) {}

  get isSuperAdmin(): boolean {
    return this.auth.currentUserValue?.role === 'SUPER_ADMIN';
  }

  removeFromCatalog(product: Product) {
    if (!confirm(`Remove "${product.productName}" from the public catalog?`)) {
      return;
    }
    this.productService.delete(product.id).subscribe({
      next: () => this.load(),
      error: () => { this.error = 'Could not remove product'; }
    });
  }

  fallbackIcon(category: string): string {
    return this.categoryFallback[category] || '🛒';
  }

  onImageError(event: Event) {
    const img = event.target as HTMLImageElement;
    img.style.display = 'none';
    const placeholder = img.nextElementSibling as HTMLElement | null;
    if (placeholder) placeholder.style.display = 'flex';
  }

  ngOnInit() {
    this.load();
  }

  load() {
    this.loading = true;
    this.productService.list().subscribe({
      next: (res) => {
        this.products = res.data || [];
        this.loading = false;
      },
      error: () => {
        this.error = 'Could not load products';
        this.loading = false;
      }
    });
  }

  addToCart(product: Product) {
    const qty = this.quantities[product.id] || product.minimumOrderQuantity || 1;
    this.cart.add(product, qty);
  }
}
