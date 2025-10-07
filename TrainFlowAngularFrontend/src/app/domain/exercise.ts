export interface Exercise{
    id: number;
    name: string;
    category: ExerciseCategory;
    description: string;
}

export enum ExerciseCategory{
    Cardio, Strength, Mobility, HIIT
}