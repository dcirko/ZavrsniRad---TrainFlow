import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { User } from '../domain/user';
import { Observable, tap } from 'rxjs';
import { SuggestedPlans } from '../domain/suggestedPlansMocked';
import { FullTrainingPlan } from '../domain/fullTrainingPlan';
import { WorkoutLog } from '../domain/workoutLog';
import { Exercise } from '../domain/exercise';

@Injectable({
  providedIn: 'root'
})
export class AppService {
  private trainingUrl = 'http://localhost:8080/api/training';

  suggestedPlans!: SuggestedPlans[];

  constructor(private http: HttpClient, private auth: AuthService) { }


  getSuggestedPlans(): Observable<SuggestedPlans[]>{
    return this.http.get<SuggestedPlans[]>(`${this.trainingUrl}/getSuggestedPlans`).pipe(
      tap((data: SuggestedPlans[]) => {
        this.suggestedPlans = data;
        console.log("Suggested plans fetched:", this.suggestedPlans);
      })
    )
  }

  getFullTrainingPlan(id: number): Observable<FullTrainingPlan>{
    return this.http.get<FullTrainingPlan>(`${this.trainingUrl}/getFullTrainingPlan/${id}`).pipe(
      tap((data: FullTrainingPlan) => {
        console.log("Full training plan fetched:", data);
      }
    ))
  }

  savePlanForUser(planId: number){
    return this.http.post(`${this.trainingUrl}/savePlanForUser`, {planId}).pipe(
      tap(() => {
        console.log(`Plan with ID ${planId} saved for user.`);
      }));
  }

  removeSavedPlan(planId: number){
    return this.http.delete(`${this.trainingUrl}/deleteSavedPlan/${planId}`).pipe(
      tap(() => {
        console.log(`Plan with ID ${planId} removed from saved plans.`);
      }));
  }

  deleteYourPlan(planId: number){
    return this.http.delete(`${this.trainingUrl}/deleteTrainingPlan/${planId}`).pipe(
      tap(() => {
        console.log(`Plan with ID ${planId} deleted from user plans.`);
      }));
  }

  getUserCreatedPlans(): Observable<FullTrainingPlan[]>{
    return this.http.get<FullTrainingPlan[]>(`${this.trainingUrl}/getUserTrainingPlans`).pipe(
      tap((data: FullTrainingPlan[]) => {
        console.log("User created plans fetched:", data);
      }));
  }

  getSavedPlansForUser(): Observable<FullTrainingPlan[]>{
    return this.http.get<FullTrainingPlan[]>(`${this.trainingUrl}/getSavedPlansForUser`).pipe(
      tap((data: FullTrainingPlan[]) => {
        console.log("Saved plans for user fetched:", data);
      }));
  }
  
  checkIfAlreadySavedPlan(planId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.trainingUrl}/checkIfAlreadySavedPlan/${planId}`);
  }

  checkIfSavedPlanIsActive(planId: number): Observable<boolean>{
    return this.http.get<boolean>(`${this.trainingUrl}/checkIfSavedPlanIsActive/${planId}`);
  }


  createTrainingPlan(newTrainingPlan: FullTrainingPlan){
    return this.http.post(`${this.trainingUrl}/addNewTrainingPlan`, newTrainingPlan).pipe(
      tap(() => {
        console.log("New training plan created:", newTrainingPlan);
      }));
  }

  makePlanActive(planid: number){
    console.log(planid);
    return this.http.post(`${this.trainingUrl}/makePlanActive`, {planid}).pipe(
      tap(() => {
        console.log(`Plan with ID ${planid} made active.`);
      }));
  }

  makePlanInactive(planid: number){
    console.log(planid);
    return this.http.post(`${this.trainingUrl}/makePlanInactive`, {planid}).pipe(
      tap(() => {
        console.log(`Plan with ID ${planid} made inactive.`);
      }));
  }

  getActivePlan(): Observable<FullTrainingPlan>{
    return this.http.get<FullTrainingPlan>(`${this.trainingUrl}/getActivePlan`).pipe(
      tap((data: FullTrainingPlan) => {
        console.log("Active training plan fetched:", data);
      }
    ));
  }

  saveWorkoutLogs(workoutLogs: WorkoutLog[]){
    return this.http.post(`${this.trainingUrl}/saveWorkoutLogs`, workoutLogs).pipe(
      tap(
        () => {console.log("Workout logs saved!");}
      )
    )
  }

  getWorkoutLogsForPlan(planId: number): Observable<WorkoutLog[]>{
    return this.http.get<WorkoutLog[]>(`${this.trainingUrl}/getWorkoutLogsForPlan/${planId}`).pipe(
      tap((data: WorkoutLog[]) => {
        console.log("Workout logs fetched:", data);
    }));
  }

  getExerciseById(id: number): Observable<Exercise>{
    return this.http.get<Exercise>(`${this.trainingUrl}/getExerciseById/${id}`).pipe(
      tap((data: Exercise) => {
        console.log("Exercise fetched:", data);
    }));
  }

  deleteLog(logId: number){
    return this.http.delete(`${this.trainingUrl}/deleteLog/${logId}`).pipe(
      tap(() => {
        console.log(`Log with ID ${logId} deleted.`);})
    );
  }

  deleteSuggestedPlan(planId: number){
    return this.http.delete(`${this.trainingUrl}/deleteSuggestedPlan/${planId}`).pipe(
      tap(() => {
        console.log(`Suggested plan with ID ${planId} deleted.`);}
    ))
  }

  updateTrainingPlan(updatedPlan: FullTrainingPlan){
    console.log(updatedPlan.id)
    return this.http.put(`${this.trainingUrl}/updateTrainingPlan`, updatedPlan).pipe(
      tap(() => {
        console.log("Training plan updated:", updatedPlan);
      })
    )
  }
}
