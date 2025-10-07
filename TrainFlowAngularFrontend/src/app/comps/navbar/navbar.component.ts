import { Component, HostListener, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FullTrainingPlan, TrainingDayDTO } from '../../domain/fullTrainingPlan';
import { AppService } from '../../services/app.service';
import { RefreshService } from '../../services/refresh.service';
import { CreatePlanComponent } from '../../pages/trainings/create-plan/create-plan.component';
import { ModalService } from '../../services/modal.service';
  

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit{
  isMenuOpen = false;
  activePlan!: FullTrainingPlan;
  todayName!: string;
  today!: TrainingDayDTO | undefined;
  hideActivePlan!: boolean;

  constructor(public auth: AuthService, private router: Router, private api: AppService, private refreshService: RefreshService, private modal: ModalService) {}
  ngOnInit(): void {
    this.auth.isLoggedIn$.subscribe((loggedIn) => {
      if (loggedIn) {
        this.getActivePlan();
      } else {
        this.activePlan = undefined!;
        this.today = undefined!;
      }
    });
    
    this.refreshService.refresh$.subscribe(() => {
      this.getActivePlan();
    });

    this.modal.modalOpen$.subscribe((isOpen) => {
      this.hideActivePlan = isOpen;
    })
  }

  getActivePlan(){
    this.api.getActivePlan().subscribe({
      next: (data) => {
        
        this.activePlan = data;
        console.log("Active plan fetched:", this.activePlan);
        this.todayName = this.getTodayName();
        console.log(this.todayName);
        const todayData = this.activePlan.days.find((d: any) => d.day === this.todayName);
        this.today = todayData || { id: 0, day: this.todayName, name: 'Rest Day', exercises: [] };
        console.log(this.activePlan.days);
        console.log(this.today);
      },
      error: (err) => {
        console.error('Error fetching active plan:', err);
        this.activePlan = undefined!;
        this.today = undefined!;
      }
    })
    
  }

  getTodayName(): string {
    const daysMap = [
      'Sunday',
      'Monday',
      'Tuesday',
      'Wednesday',
      'Thursday',
      'Friday',
      'Saturday'
    ];
    const todayIndex = new Date().getDay();
    return daysMap[todayIndex];
  }

  goToPlan(){
    this.router.navigate(['workoutTracker']);
  }
  
  shouldHideNavbar(): boolean {
    return this.router.url === '/login' || this.router.url === '/register' || this.router.url === '/trainingPage/createPlan' || this.router.url.startsWith('/editPlan/');
  }

  logout(){
    this.auth.logout();
  }

  isDropdownOpen = false;

  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
      const target = event.target as HTMLElement;
      if (!target.closest('.dropdown')) {
        this.isDropdownOpen = false;
      }
    }



  
}
