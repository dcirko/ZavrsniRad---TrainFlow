import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './authPages/login/login.component';
import { RegisterComponent } from './authPages/register/register.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { authGuard } from './auth.guard';
import { TrainingPageComponent } from './pages/training-page/training-page.component';
import { YourPlansComponent } from './pages/trainings/your-plans/your-plans.component';
import { CreatePlanComponent } from './pages/trainings/create-plan/create-plan.component';
import { TrainingHomepageComponent } from './pages/trainings/training-homepage/training-homepage.component';
import { WorkoutTrackerComponent } from './pages/workout-tracker/workout-tracker.component';
import { EditPlanComponent } from './pages/edit-plan/edit-plan.component';



export const routes: Routes = [
    {path: '', redirectTo: 'home', pathMatch: 'full'},
    {path: 'home', component: HomeComponent},
    {path: 'login', component: LoginComponent},
    {path: 'register', component: RegisterComponent},
    {path: 'profile', component: ProfileComponent, canActivate: [authGuard]},
    {path: 'trainingPage',component: TrainingPageComponent,
        children: [
          { path: '', component: TrainingHomepageComponent},
          { path: 'yourPlans', component: YourPlansComponent , canActivate: [authGuard]},
          { path: 'createPlan', component: CreatePlanComponent, canActivate: [authGuard]},
        ]
    },
    {path: 'workoutTracker', component: WorkoutTrackerComponent, canActivate: [authGuard]},
    {path: 'editPlan/:planId', component: EditPlanComponent, canActivate: [authGuard]}
];
