export interface FullTrainingPlan{
    id: number;
    name: string;
    description: string;
    goal: string;
    difficulty: string;
    isSuggested: boolean;
    isActive: boolean;
    days: TrainingDayDTO[]; 
}

export interface TrainingDayDTO{
    id: number;
    name: string;
    day: string;
    exercises: TrainingDayExerciseDTO[];
}

export interface TrainingDayExerciseDTO{
    id: number;
    exerciseId: number;
    exerciseName: string;
    category: string;
    description: string;
    sets: number;
    reps: number;
    restTime: number;
}
