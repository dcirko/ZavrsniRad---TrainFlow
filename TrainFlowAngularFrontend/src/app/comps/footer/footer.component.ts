import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.css'
})
export class FooterComponent {
  constructor(private router: Router) {}
    
    shouldHideFooter(): boolean {
      
      return this.router.url === '/login' || this.router.url === '/register' || this.router.url === '/trainingPage/createPlan' || this.router.url.startsWith('/editPlan/');
    }
}
