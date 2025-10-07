import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../domain/user';
import { AuthService } from './auth.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userUrl = 'http://localhost:8080/api/user';
  constructor(private http: HttpClient, private auth: AuthService) { }

  getUser(){
    const token = this.auth.getToken();
    console.log("Token je:", token);
    return this.http.get<any>(`${this.userUrl}/getUserByEmail`);
  }

  updateProfile(user : User){
    const token = this.auth.getToken();
    console.log("Token je:", token);
    return this.http.put<any>(`${this.userUrl}/updateProfile`, user);
  }

  isAdmin(): Observable<boolean>{
    return this.http.get<boolean>(`${this.userUrl}/isAdmin`);
  }
}
