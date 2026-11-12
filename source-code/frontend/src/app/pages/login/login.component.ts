import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent {
  phoneNumber = '';
  otp = '';
  fullName = '';
  role = 'CUSTOMER';
  city = '';
  step: 'phone' | 'otp' = 'phone';
  loading = false;
  error = '';
  devOtp = '';

  roles = ['CUSTOMER', 'FARMER', 'RETAILER', 'SUPER_ADMIN', 'CITY_ADMIN', 'STORAGE_ADMIN', 'DELIVERY_PROVIDER'];

  // Pre-seeded demo accounts, one per role, so testers can switch roles with a single
  // click instead of retyping a phone number (which would otherwise keep logging them
  // into whichever role that phone number was originally registered with).
  testAccounts = [
    { role: 'SUPER_ADMIN', phone: '+919000000001', label: 'Super Admin — Test Admin' },
    { role: 'FARMER', phone: '+919000000002', label: 'Farmer — Test Farmer' },
    { role: 'RETAILER', phone: '+919000000003', label: 'Retailer — Test Retailer' },
    { role: 'CUSTOMER', phone: '+919000000004', label: 'Customer — Test Customer' },
    { role: 'CITY_ADMIN', phone: '+919000000005', label: 'City Admin — Test City Admin' },
    { role: 'STORAGE_ADMIN', phone: '+919000000006', label: 'Storage Admin — Test Storage Admin' },
    { role: 'DELIVERY_PROVIDER', phone: '+919000000007', label: 'Delivery Provider — Test Delivery Provider' }
  ];

  constructor(private auth: AuthService, private router: Router) {}

  quickLogin(phone: string) {
    if (!phone) { return; }
    this.phoneNumber = phone;
    this.sendOtp();
  }

  sendOtp() {
    this.error = '';
    this.loading = true;
    this.auth.sendOtp(this.phoneNumber).subscribe({
      next: (res) => {
        this.loading = false;
        this.step = 'otp';
        this.devOtp = res.data?.devOtp || '';
        // Dev mode only: auto-fill the OTP since it's already known, saving a step.
        if (this.devOtp) {
          this.otp = this.devOtp;
        }
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.message || 'Failed to send OTP';
      }
    });
  }

  verifyOtp() {
    this.error = '';
    this.loading = true;
    this.auth.verifyOtp(this.phoneNumber, this.otp, this.fullName, this.role, this.city).subscribe({
      next: (res) => {
        this.loading = false;
        const role = res.data?.role;
        if (role === 'SUPER_ADMIN') {
          this.router.navigate(['/admin/dashboard']);
        } else if (role === 'CITY_ADMIN') {
          this.router.navigate(['/city-admin/requests']);
        } else if (role === 'STORAGE_ADMIN') {
          this.router.navigate(['/storage-admin/dashboard']);
        } else if (role === 'DELIVERY_PROVIDER') {
          this.router.navigate(['/delivery/orders']);
        } else {
          this.router.navigate(['/products']);
        }
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.message || 'Verification failed';
      }
    });
  }
}
