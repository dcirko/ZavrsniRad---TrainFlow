import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { SuggestedPlans } from '../../../domain/suggestedPlansMocked';
import { AppService } from '../../../services/app.service';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FullTrainingPlan } from '../../../domain/fullTrainingPlan';
import Swal from 'sweetalert2';
import { AuthService } from '../../../services/auth.service';
import { UserService } from '../../../services/user.service';
import { ModalService } from '../../../services/modal.service';


@Component({
  selector: 'app-training-homepage',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './training-homepage.component.html',
  styleUrl: './training-homepage.component.css'
})
export class TrainingHomepageComponent implements OnInit{
  @ViewChild('scrollBox', { static: false }) scrollBox!: ElementRef;
  suggestedPlans!: SuggestedPlans[];
  fullTrainingPlan!: FullTrainingPlan | null;
  checkIfAdmin!: boolean;

  constructor(private appService: AppService, private router: Router, private auth: AuthService, private userApi: UserService, private modal: ModalService) {}

  ngOnInit(): void {
    this.getSuggestedPlans();
    this.isAdmin();
  }

  getSuggestedPlans(){
    this.appService.getSuggestedPlans().subscribe(data => {
      this.suggestedPlans = data;});
  }

  getFullTrainingPlan(planId: number){
    this.appService.getFullTrainingPlan(planId).subscribe(data => {this.fullTrainingPlan = data});
  }

  selectedPlan: any = null;

  openPlanModal(planId: number) {
    this.getFullTrainingPlan(planId);
    console.log('Selected plan: ', this.fullTrainingPlan);
    this.modal.setOpen(true);
  }

  closeModal() {
    this.fullTrainingPlan = null;
    this.modal.setOpen(false);
  }

  savePlan(planId: number) {
    console.log('Plan id: ', planId);
    if(this.auth.isLoggedIn()) {
      this.appService.checkIfAlreadySavedPlan(planId).subscribe((alreadySaved: boolean) => {
          if (alreadySaved) {
            Swal.fire('Error', 'You have already saved this plan.', 'error');
            this.closeModal();
          } else {
            Swal.fire({
              title: 'Save this plan?',
              text: 'Do you want to save this plan to your profile?',
              icon: 'question',
              showCancelButton: true,
              confirmButtonText: 'Yes, save it!',
              cancelButtonText: 'Cancel',
              confirmButtonColor: '#3085d6',
              cancelButtonColor: '#d33'
            }).then((result) => {
              if (result.isConfirmed) {
                this.appService.savePlanForUser(planId).subscribe({
                  next: () => {
                    Swal.fire('Saved!', 'The plan has been successfully saved.', 'success');
                    this.router.navigate(['/trainingPage']);
                    this.closeModal();
                  },
                  error: () => {
                    Swal.fire('Error!', 'An error occurred while saving the plan.', 'error');
                    this.router.navigate(['/trainingPage']);
                  }
                });
              }
            });
          }
        });
    }else{
      Swal.fire({
            icon: 'warning',
            title: 'Access Denied',
            text: 'Please log in or register to save plans.',
            confirmButtonText: 'Go to Login',
            confirmButtonColor: '#3085d6',
          }).then(() => {
            this.router.navigate(['/login']);
          });
    }
    
  }



  scrollLeft() {
    this.scrollBox.nativeElement.scrollBy({ left: -300, behavior: 'smooth' });
  }

  scrollRight() {
    this.scrollBox.nativeElement.scrollBy({ left: 300, behavior: 'smooth' });
  }


  /*ADMIN*/
  isAdmin(){
    this.userApi.isAdmin().subscribe((isAdmin: boolean) => {
      this.checkIfAdmin = isAdmin;
      console.log(isAdmin);
    });
  }

  addNewSuggestedPlan(){
    this.router.navigate(['/trainingPage/createPlan']);
  }

  editSuggestedPlan(planId: number){
    this.router.navigate(['editPlan', planId])
  }

  deleteSuggestedPlan(planId: number){
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to delete this suggested plan?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'Cancel'
    }).then((result) => {
      if (result.isConfirmed) {
        this.appService.deleteSuggestedPlan(planId).subscribe({
          next: () => {
            Swal.fire('Deleted!', 'The suggested plan has been deleted.', 'success');
            this.getSuggestedPlans();
          },
          error: () => {
            Swal.fire('Error', 'Failed to delete the suggested plan.', 'error');
          }
        })
      }
    })
  }

}
