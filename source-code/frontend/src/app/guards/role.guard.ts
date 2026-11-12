import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const allowedRoles: string[] = route.data['roles'] || [];
    const user = this.auth.currentUserValue;

    if (!user) {
      this.router.navigate(['/login']);
      return false;
    }

    if (allowedRoles.length === 0 || allowedRoles.includes(user.role)) {
      return true;
    }

    this.router.navigate(['/products']);
    return false;
  }
}
