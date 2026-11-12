import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { ProductsComponent } from './pages/products/products.component';
import { CartComponent } from './pages/cart/cart.component';
import { OrdersComponent } from './pages/orders/orders.component';
import { SellRequestComponent } from './pages/sell-request/sell-request.component';
import { CityAdminComponent } from './pages/city-admin/city-admin.component';
import { SuperAdminComponent } from './pages/super-admin/super-admin.component';
import { StorageAdminComponent } from './pages/storage-admin/storage-admin.component';
import { DeliveryProviderComponent } from './pages/delivery-provider/delivery-provider.component';
import { AuthGuard } from './guards/auth.guard';
import { RoleGuard } from './guards/role.guard';

const routes: Routes = [
  { path: '', redirectTo: '/products', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'products', component: ProductsComponent, canActivate: [AuthGuard] },
  { path: 'cart', component: CartComponent, canActivate: [AuthGuard] },
  { path: 'orders', component: OrdersComponent, canActivate: [AuthGuard] },
  {
    path: 'sell-requests', component: SellRequestComponent,
    canActivate: [AuthGuard, RoleGuard], data: { roles: ['FARMER'] }
  },
  {
    path: 'city-admin/requests', component: CityAdminComponent,
    canActivate: [AuthGuard, RoleGuard], data: { roles: ['CITY_ADMIN'] }
  },
  {
    path: 'admin/dashboard', component: SuperAdminComponent,
    canActivate: [AuthGuard, RoleGuard], data: { roles: ['SUPER_ADMIN'] }
  },
  {
    path: 'storage-admin/dashboard', component: StorageAdminComponent,
    canActivate: [AuthGuard, RoleGuard], data: { roles: ['STORAGE_ADMIN'] }
  },
  {
    path: 'delivery/orders', component: DeliveryProviderComponent,
    canActivate: [AuthGuard, RoleGuard], data: { roles: ['DELIVERY_PROVIDER'] }
  },
  { path: '**', redirectTo: '/products' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
