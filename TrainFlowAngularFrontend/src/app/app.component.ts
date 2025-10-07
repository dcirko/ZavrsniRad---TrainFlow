import { Component } from '@angular/core';
import { NavigationEnd, RouterOutlet } from '@angular/router';
import { NavbarComponent } from "./comps/navbar/navbar.component";
import { FooterComponent } from "./comps/footer/footer.component";
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';



@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent, FooterComponent, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'TrainFlow';
  showLayout = true;

  constructor(private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.showLayout = !['/login', '/register'].includes(event.urlAfterRedirects);
      }
    });
  }
}
