import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { FullTrainingPlan } from '../../domain/fullTrainingPlan';
import { AppService } from '../../services/app.service';
import { UserService } from '../../services/user.service';


@Component({
  selector: 'app-edit-plan',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './edit-plan.component.html',
  styleUrl: './edit-plan.component.css'
})
export class EditPlanComponent implements OnInit {
  step = 1;
  planForm!: FormGroup;
  planDays!: any[];
  daysOfWeek = ['Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday'];
  planId!: number;
  firstPlan!: FullTrainingPlan;
  finalPlan!: FullTrainingPlan;
  isAdmin!: boolean;

  constructor(
    private fb: FormBuilder,
    private api: AppService,
    private router: Router,
    private route: ActivatedRoute,
    private userApi: UserService
  ) {}

  ngOnInit(): void {
    this.userApi.isAdmin().subscribe({
      next: (data) => {
        this.isAdmin = data;
      },
      error: (err) => {
        console.error('Error checking admin status', err);
      }
    })

    this.planForm = this.fb.group({
      id: [''],
      name: ['', Validators.required],
      description: [''],
      goal: ['', Validators.required],
      difficulty: ['', Validators.required],
      isActive: ['']
    });

    this.planId = Number(this.route.snapshot.paramMap.get('planId'));
    console.log(this.planId);
    this.loadPlan();
  }

  loadPlan() {
    this.api.getFullTrainingPlan(this.planId).subscribe({
      next: (plan: FullTrainingPlan) => {
        this.firstPlan = plan;
        this.planForm.patchValue({
          id: plan.id,
          name: plan.name,
          description: plan.description,
          goal: plan.goal,
          difficulty: plan.difficulty,
          isActive: plan.isActive
        });
        this.planDays = plan.days.map(d => ({
          ...d,
          exercises: d.exercises.map(ex => ({ ...ex, isEditing: false }))
        }));
      },
      error: () => {
        Swal.fire('Error', 'Could not load training plan.', 'error');
        this.router.navigate(['/trainingPage']);
      }
    });
  }

  nextStep() {
    if (this.planForm.valid && this.step == 1) {
      this.step = 2;
    } else if (this.step == 2) {
      this.step = 3;
    } else {
      this.planForm.markAllAsTouched();
    }
  }

  prevStep() {
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
        ex.category && ex.sets >= 1 && ex.reps >= 1 && ex.restTime >= 0 && ex.description != null
      )
    );
  }

  updateTrainingPlan() {
    if(this.isAdmin && this.firstPlan.isSuggested == true){
      console.log("Suggested plan");
      this.finalPlan = {
        ...this.planForm.value,
        isSuggested: true,
        days: this.planDays
      };
      Swal.fire({
        title: 'Update this suggested plan?',
        text: 'Do you want to save your changes?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Yes, update it!',
        cancelButtonText: 'Cancel',
        confirmButtonColor: '#36ad79',
        cancelButtonColor: '#d33'
      }).then((result) => {
        if (result.isConfirmed) {
          this.api.updateTrainingPlan(this.finalPlan).subscribe({
            next: () => {
              Swal.fire('Success', 'Plan updated!', 'success');
              this.router.navigate(['/trainingPage/yourPlans']);
            },
            error: () => {
              Swal.fire('Error', 'Something went wrong', 'error');
            }
          });
        }
      })
    }else{
      console.log("Normal plan");
      this.finalPlan = {
        ...this.planForm.value,
        isSuggested: false,
        days: this.planDays
      };

      console.log("finalni: ",this.finalPlan);
      Swal.fire({
        title: 'Update this plan?',
        text: 'Do you want to save your changes?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Yes, update it!',
        cancelButtonText: 'Cancel',
        confirmButtonColor: '#36ad79',
        cancelButtonColor: '#d33'
      }).then((result) => {
        if (result.isConfirmed) {
          this.api.updateTrainingPlan(this.finalPlan).subscribe({
            next: () => {
              Swal.fire('Success', 'Plan updated!', 'success');
              this.router.navigate(['/trainingPage/yourPlans']);
            },
            error: () => {
              Swal.fire('Error', 'Something went wrong', 'error');
            }
          });
        }
      })
    }
  }
}
