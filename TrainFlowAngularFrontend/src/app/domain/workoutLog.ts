export interface WorkoutLog{
  id?:number;
  userId?: number;
  trainingPlanId: number;
  trainingDayId: number;
  trainingDayExerciseId: number;
  exerciseId: number;
  logDate: Date; 
  plannedSets: number;
  plannedReps: number;
  plannedWeight?: number;
  actualSets?: number;
  actualReps?: number;
  actualWeight?: number;
  notes?: string;

}