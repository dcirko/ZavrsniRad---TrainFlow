import { CommonModule, DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AppService } from '../../services/app.service';
import { FullTrainingPlan, TrainingDayDTO } from '../../domain/fullTrainingPlan';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { WorkoutLog } from '../../domain/workoutLog';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';



@Component({
  selector: 'app-workout-tracker',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  providers:[DatePipe],
  templateUrl: './workout-tracker.component.html',
  styleUrl: './workout-tracker.component.css'
})
export class WorkoutTrackerComponent implements OnInit{
  activePlan!: FullTrainingPlan;
  todayName!: string;
  today!: TrainingDayDTO | undefined;
  workoutForm!: FormGroup;
  workoutLogs!: WorkoutLog[];
  previousLogs!: WorkoutLog[];
  exerciseNames: { [key: number]: string } = {};

  constructor(private api: AppService, private fb: FormBuilder, private router: Router){}

  ngOnInit(): void {
    this.getActivePlan();

    
  }

  initForm(){
    const logs = this.today?.exercises?.map(ex =>
      this.fb.group({
        plannedSets: [ex.sets, [Validators.required, Validators.min(1)]],
        plannedReps: [ex.reps, [Validators.required, Validators.min(1)]],
        plannedWeight: [0, [Validators.min(0)]],
        actualSets: [null, [Validators.min(1)]],
        actualReps: [null, [Validators.min(1)]],
        actualWeight: [null, [Validators.min(0)]],
        notes: ['']
      })
    ) || [];

    this.workoutForm = this.fb.group({
      logs: this.fb.array(logs)
    });
    console.log(logs);
    console.log(this.workoutForm.value);
  }

  get logs(): FormArray {
    return this.workoutForm.get('logs') as FormArray;
  }

  getActivePlan(){
    this.api.getActivePlan().subscribe({
      next: (data) => {
        this.activePlan = data;
        console.log("Active plan fetched:", this.activePlan);
        this.getWorkoutLogsForPlan(this.activePlan.id);
        this.todayName = this.getTodayName();
        console.log(this.todayName);
        const todayData = this.activePlan.days.find((d: any) => d.day === this.todayName);
        this.today = todayData || { id: 0, day: this.todayName, name: 'Rest Day', exercises: [] };
        console.log(this.activePlan.days);
        console.log(this.today);
        this.initForm();
        
      },
      error: (err) => {
        console.error('Error fetching active plan:', err);
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

  saveWorkout() {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to save this workout log?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, save it!',
      cancelButtonText: 'Cancel'
    }).then((result) => {
      if (result.isConfirmed) {
        const logs = this.workoutForm.value.logs.map((log: any, i: number) => {
          const ex = this.today!.exercises[i]; 

          return {
            trainingPlanId: this.activePlan.id,
            trainingDayId: this.today!.id,
            trainingDayExerciseId: ex.id,
            exerciseId: ex.exerciseId,
            logDate: new Date(),
            plannedSets: log.plannedSets,
            plannedReps: log.plannedReps,
            plannedWeight: log.plannedWeight,

            actualSets: log.actualSets,
            actualReps: log.actualReps,
            actualWeight: log.actualWeight,

            notes: log.notes
          };
        });

        this.workoutLogs = logs;

        this.api.saveWorkoutLogs(this.workoutLogs).subscribe({
          next: () => {
            Swal.fire('Saved!', 'Your workout has been saved.', 'success');
            this.router.navigate(['/workoutTracker']);
            this.getWorkoutLogsForPlan(this.workoutLogs[0].trainingPlanId);
          },
          error: (err) => {
            console.error('Error saving workout logs:', err);
            Swal.fire('Error', 'Failed to save workout logs.', 'error');
          }
        });
      }
    });
  }

  getWorkoutLogsForPlan(planid: number){
    this.api.getWorkoutLogsForPlan(planid).subscribe({
      next: (data) =>{this.previousLogs = data
        if (this.previousLogs) {
          this.previousLogs.forEach(log => {
            if (log.exerciseId /*&& !this.exerciseNames[log.exerciseId]*/) {
              this.api.getExerciseById(log.exerciseId).subscribe(ex => {
                this.exerciseNames[log.exerciseId] = ex.name; 
                console.log("Names: "+ this.exerciseNames);
              });
            }
          });
        }
      },
      error: (err) => {console.error('Error fetching workout logs:', err);}
    })
  }

 deleteLog(logId: number) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'This action cannot be undone!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#36ad79',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'Cancel'
    }).then((result) => {
      if (result.isConfirmed) {
        this.api.deleteLog(logId).subscribe({
          next: () => {
            Swal.fire('Deleted!', 'The log has been deleted.', 'success');
            this.previousLogs = this.previousLogs.filter(log => log.id !== logId);
          },
          error: (err) => {
            console.error('Error deleting log:', err);
            Swal.fire('Error', 'Failed to delete the log.', 'error');
          }
        });
      }
    });
  }

}
