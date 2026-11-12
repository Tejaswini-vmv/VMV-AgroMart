import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { LoginComponent } from './pages/login/login.component';
import { ProductsComponent } from './pages/products/products.component';
import { CartComponent } from './pages/cart/cart.component';
import { OrdersComponent } from './pages/orders/orders.component';
import { SellRequestComponent } from './pages/sell-request/sell-request.component';
import { CityAdminComponent } from './pages/city-admin/city-admin.component';
import { SuperAdminComponent } from './pages/super-admin/super-admin.component';
import { StorageAdminComponent } from './pages/storage-admin/storage-admin.component';
import { DeliveryProviderComponent } from './pages/delivery-provider/delivery-provider.component';

import { AuthInterceptor } from './interceptors/auth.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ProductsComponent,
    CartComponent,
    OrdersComponent,
    SellRequestComponent,
    CityAdminComponent,
    SuperAdminComponent,
    StorageAdminComponent,
    DeliveryProviderComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
