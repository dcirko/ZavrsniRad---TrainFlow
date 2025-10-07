import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FullTrainingPlan } from '../../../domain/fullTrainingPlan';
import Swal from 'sweetalert2';
import { AppService } from '../../../services/app.service';
import { SuggestedPlans } from '../../../domain/suggestedPlansMocked';
import { Router, RouterLink } from '@angular/router';
import { identity } from 'rxjs';
import { UserService } from '../../../services/user.service';
import { RefreshService } from '../../../services/refresh.service';
import { ModalService } from '../../../services/modal.service';



@Component({
  selector: 'app-your-plans',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './your-plans.component.html',
  styleUrl: './your-plans.component.css'
})
export class YourPlansComponent implements OnInit {
  userPlans!: FullTrainingPlan[];
  savedPlans!: FullTrainingPlan[];
  selectedPlan!: FullTrainingPlan | null;
  activePlans: { [planId: number]: boolean } = {};
  modalSource: 'user' | 'saved' | null = null;

  ngOnInit(): void {
    this.getUserCreatedPlans();
    this.getSavedPlans();
  }

  constructor(private app: AppService, private router: Router, private refresh: RefreshService, private modal: ModalService){}

  getFullTrainingPlan(planId: number){
    this.app.getFullTrainingPlan(planId).subscribe({
      next: (data) => { this.selectedPlan = data,
        console.log(this.selectedPlan.isActive);
      },
      error: (err) => {
        console.error('Error fetching full training plan:', err);
      }
    })
  }

  /*USER SAVED PLANS*/
  getSavedPlans(){
    this.app.getSavedPlansForUser().subscribe({
      next: (data) => {
        this.savedPlans = data
        this.savedPlans?.forEach(plan => {
          this.app.checkIfSavedPlanIsActive(plan.id).subscribe({
            next: (isActive) => {
              this.activePlans[plan.id] = isActive;
            },
            error: (err) => {
              console.error('Error checking if plan is active:', err);
              this.activePlans[plan.id] = false;
            }
          });
        });
      },
      error: (err) => {
        console.error('Error loading saved plans:', err);
      }
    })
  }

  makeActivePlan(planId: number){
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to make this training plan currently active?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, make it active!',
      cancelButtonText: 'Cancel'
    }).then((result) => {
      if (result.isConfirmed) {
        this.app.makePlanActive(planId).subscribe({
          next: () => {
            Swal.fire('Active!', 'The plan has is now active.', 'success');
            this.refresh.triggerRefresh();
            this.getSavedPlans();
            this.getUserCreatedPlans();
            this.closeSavedModal();
      
          },
          error: () => {
            Swal.fire('Error', 'Failed to make it active. There already is an active training plan!', 'error');
            this.getSavedPlans();
            this.getUserCreatedPlans();
            this.closeSavedModal();
          }
        });
      }
    });
  }

  makeInactivePlan(planId: number){
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to make this training plan inactive?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, make it inactive!',
      cancelButtonText: 'Cancel'
    }).then((result) => {
      if (result.isConfirmed) {
        this.app.makePlanInactive(planId).subscribe({
          next: () => {
            Swal.fire('Inactive!', 'The plan has is no longer active.', 'success');
            this.refresh.triggerRefresh();
            this.getSavedPlans();
            this.getUserCreatedPlans();
            this.closeSavedModal();
          },
          error: () => {
            Swal.fire('Error', 'Failed to make it inactive.', 'error');
            this.getSavedPlans();
            this.getUserCreatedPlans();
            this.closeSavedModal();
          }
        });
      }
    });
  }

  removeSavedPlan(planId: number){
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to remove this saved plan?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, remove it!',
      cancelButtonText: 'Cancel'
    }).then((result) => {
      if (result.isConfirmed) {
        this.app.removeSavedPlan(planId).subscribe({
          next: () => {
            Swal.fire('Removed!', 'The plan has been removed.', 'success');
            this.refresh.triggerRefresh();
            this.getSavedPlans();
            this.closeSavedModal();
          },
          error: () => {
            Swal.fire('Error', 'Failed to remove the plan.', 'error');
          }
        });
      }
    });
  }

  openSavedModal(plan: FullTrainingPlan, source: 'user' | 'saved' = 'saved'){
    this.modalSource = source;
    this.getFullTrainingPlan(plan.id);
    this.modal.setOpen(true);
  }

  closeSavedModal(){
    this.selectedPlan = null;
    this.modalSource = null;
    this.modal.setOpen(false);
  }

  @ViewChild('scrollBox', { static: false }) scrollBox!: ElementRef;

  scrollLeft() {
    this.scrollBox.nativeElement.scrollBy({ left: -300, behavior: 'smooth' });
  }

  scrollRight() {
    this.scrollBox.nativeElement.scrollBy({ left: 300, behavior: 'smooth' });
  }



  /*USER CREATED PLANS*/
  getUserCreatedPlans(){
    this.app.getUserCreatedPlans().subscribe({
      next: (data) => { this.userPlans = data; },
      error: (err) => {
        console.error('Error loading user created plans:', err);
      }
    })
  }


  editPlan(planId: number){
    this.router.navigate(['editPlan', planId]);
  }

  deletePlan(planId: number){
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to remove this plan?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, remove it!',
      cancelButtonText: 'Cancel'
    }).then((result) => {
      if (result.isConfirmed) {
        this.app.deleteYourPlan(planId).subscribe({
          next: () => {
            Swal.fire('Removed!', 'The plan has been removed.', 'success');
            this.getSavedPlans();
            this.getUserCreatedPlans();
            this.closeSavedModal();
          },
          error: () => {
            Swal.fire('Error', 'Failed to remove the plan.', 'error');
          }
        });
      }
    });
  }

}
