export interface User {
  id?: number;         
  name: string;
  surname: string;
  email: string;
  password: string;
  age?: number;         
  height?: number;
  weight?: number;
  gender: string;
  roles?: RoleUser[];   
}

export interface RoleUser {
  id: number;
  name: string;
}
