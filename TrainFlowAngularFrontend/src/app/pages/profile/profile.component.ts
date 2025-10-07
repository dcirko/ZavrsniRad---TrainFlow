import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import {jwtDecode} from 'jwt-decode';
import { AppService } from '../../services/app.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { User } from '../../domain/user';
import { FullTrainingPlan } from '../../domain/fullTrainingPlan';
import Swal from 'sweetalert2';
import { UserService } from '../../services/user.service';
import { ModalService } from '../../services/modal.service';
import { RefreshService } from '../../services/refresh.service';


@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit{
  token !: string | null;
  userId!:number;
  user !: User;
  newAge!:number;
  newHeight!: number;
  newWeight!: number;
  editingAge = false;
  editingHeight = false;
  editingWeight = false;
  bmi!:number;
  savedPlans!: FullTrainingPlan[];
  selectedSavedPlan!:FullTrainingPlan | null;
  fullTrainingPlan!: FullTrainingPlan;
  activePlans: { [planId: number]: boolean } = {};

  constructor(private auth: AuthService, private app: AppService, private userApi: UserService, private modal: ModalService, private refresh: RefreshService) {}

  ngOnInit(): void {
    this.getUser();
    this.getSavedPlans();
  }

  getUser(){
    this.userApi.getUser().subscribe( {
      next: (data) => {
        this.user = data;
        console.log('User data:', this.user);
      },
      error: (err) => {
        console.error('Error loading profile:', err);
      }
    });
  }

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

  saveDetails(){
    this.user.age = this.editingAge ? this.newAge : this.user.age;
    this.user.height = this.editingHeight ? this.newHeight : this.user.height;
    this.user.weight = this.editingWeight ? this.newWeight : this.user.weight;
  
    this.userApi.updateProfile(this.user).subscribe({
      next: (response) => {
        console.log('Profile updated successfully:', response);
        this.editingAge = false;
        this.editingHeight = false;
        this.editingWeight = false;
      },
      error: (error) => {
        console.error('Error updating profile:', error);
      }
    });
  }
  
  calculateBMI(height: number, weight: number): number{
    const heightMeters = height/100;
    return +((weight / (heightMeters * heightMeters)).toFixed(1));
  }

  getBMICategory(bmi: number): string{
    switch(true){
      case bmi < 18.5:
        return "You are in the underweight range";
      case bmi >= 18.5 && bmi <= 24.9:
        return "You are in the healthy weight range";
      case bmi >= 25 && bmi <= 29.9:
        return "You are in the overweight range";
      case bmi >= 30 && bmi <= 39.9:
        return "You are in the obese range";
      case bmi >= 40:
        return "You are in the severely obese range";
      default:
        return "BMI category could not be determined";
    }
  }

  getFullTrainingPlan(planId: number){
    this.app.getFullTrainingPlan(planId).subscribe(data => {this.selectedSavedPlan = data});
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
        },
        error: () => {
          Swal.fire('Error', 'Failed to remove the plan.', 'error');
        }
      });
    }
  });
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
              this.closeSavedModal();
            },
            error: () => {
              Swal.fire('Error', 'Failed to make it active. There already is an active training plan!', 'error');
              this.getSavedPlans();
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
              this.closeSavedModal();
            },
            error: () => {
              Swal.fire('Error', 'Failed to make it inactive.', 'error');
              this.getSavedPlans();
              this.closeSavedModal();
            }
          });
        }
      });
    }

  openSavedModal(plan: FullTrainingPlan){
    this.getFullTrainingPlan(plan.id);
    this.modal.setOpen(true);
  }

  closeSavedModal(){
    this.selectedSavedPlan = null;
    this.modal.setOpen(false);
  }

  @ViewChild('scrollBox', { static: false }) scrollBox!: ElementRef;

  scrollLeft() {
    this.scrollBox.nativeElement.scrollBy({ left: -300, behavior: 'smooth' });
  }

  scrollRight() {
    this.scrollBox.nativeElement.scrollBy({ left: 300, behavior: 'smooth' });
  }

}
