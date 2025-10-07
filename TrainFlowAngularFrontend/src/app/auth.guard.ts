import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './services/auth.service';
import { inject } from '@angular/core';
import Swal from 'sweetalert2';

export const authGuard: CanActivateFn = (route, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  

  if (auth.isLoggedIn()) {
    return true;
  } else {
    router.navigate(['/']);
    Swal.fire({
      icon: 'warning',
      title: 'Access Denied',
      text: 'Please log in or register to access this page.',
      confirmButtonText: 'Go to Login',
      confirmButtonColor: '#3085d6',
    }).then(() => {
      router.navigate(['/login']);
    });

    return false;
  }

};
