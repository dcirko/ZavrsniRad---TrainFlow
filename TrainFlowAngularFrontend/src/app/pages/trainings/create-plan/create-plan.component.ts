import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import Swal from 'sweetalert2';
import { FullTrainingPlan } from '../../../domain/fullTrainingPlan';
import { AppService } from '../../../services/app.service';
import { UserService } from '../../../services/user.service';


@Component({
  selector: 'app-create-plan',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './create-plan.component.html',
  styleUrl: './create-plan.component.css'
})
export class CreatePlanComponent implements OnInit{
  step = 1;
  planForm!: FormGroup;
  planDays!: any[];  
  daysOfWeek = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
  finalPlan!: FullTrainingPlan;
  isAdmin!: boolean;

  constructor(private fb: FormBuilder, private app: AppService, private router: Router, private userApi: UserService){}

  ngOnInit(): void {
    this.userApi.isAdmin().subscribe((admin: boolean) => {
      this.isAdmin = admin;
      console.log(this.isAdmin);
    })
    this.planForm = this.fb.group({
      name: ['', Validators.required],
      description: [''],
      goal: ['', Validators.required],
      difficulty: ['', Validators.required],
    });

    this.planDays = this.daysOfWeek.map(day => ({
      day,
      name: '',
      exercises: []
    }));
    console.log(this.router.url);
  }

  createTrainingPlan() {
    if (this.isAdmin) {
      this.finalPlan = {
        ...this.planForm.value,
        isSuggested: true,
        isActive: false,
        days: this.planDays
      };
    } else {
      this.finalPlan = {
        ...this.planForm.value,
        isSuggested: false,
        isActive: false,
        days: this.planDays
      };
    }

    console.log(this.finalPlan);

    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to create this training plan?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Yes, create it!',
      cancelButtonText: 'Cancel',
      confirmButtonColor: '#36ad79',
      cancelButtonColor: '#d33'
    }).then((result) => {
      if (result.isConfirmed) {
        this.app.createTrainingPlan(this.finalPlan).subscribe({
          next: () => {
            Swal.fire('Success', 'Plan successfully created!', 'success');
            this.router.navigate(['/trainingPage']);
          },
          error: () => {
            Swal.fire('Error', 'Something went wrong', 'error');
          }
        });
      }
    });
  }


  nextStep(){
    if (this.planForm.valid && this.step == 1) {
      this.step = 2;
    } else if(this.step == 2){
      this.step = 3;
    }else{
      this.planForm.markAllAsTouched();
    }
  }

  prevStep(){
    if (this.step > 1) this.step--;
  }

  removeExercise(dayIndex:number, exIndex:number){
    this.planDays[dayIndex].exercises.splice(exIndex, 1);     
  }

  addExercise(dayIndex:number){
    this.planDays[dayIndex].exercises.push({
      exerciseName: '',
      category: '',
      description: '',
      sets: 3,
      reps: 10,
      restTime: 60,
      isEditing: true
    });

  }

  saveExercise(dayIndex: number, exIndex: number) {
    this.planDays[dayIndex].exercises[exIndex].isEditing = false;
  }

  editExercise(dayIndex: number, exIndex: number) {
    this.planDays[dayIndex].exercises[exIndex].isEditing = true;
  }

  areDayNamesValid(): boolean {
    return this.planDays.every(day => day.name && day.name.trim().length >= 3);
  }
  areExercisesValid(): boolean {
    return this.planDays.every(day => 
      day.exercises.every((ex: any) => 
        ex.exerciseName && ex.exerciseName.trim().length >= 3 &&
        ex.category &&
        ex.sets >= 1 &&
        ex.reps >= 1 &&
        ex.restTime >= 0
      )
    );
  }




}
