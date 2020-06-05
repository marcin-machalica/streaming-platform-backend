import { Injectable } from '@angular/core';
import {environment} from '../../../../environments/environment';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserCreate} from './AccountDto';
import {UserDto} from '../keycloak-admin-api/UserDto';

type UserDtoResponseType = HttpResponse<UserDto>;

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  resourceUrl = `${environment.serverUrl}api/v1/users/create`;

  constructor(private http: HttpClient) { }

  createUser(userCreate: UserCreate): Observable<UserDtoResponseType> {
    return this.http.post<UserDto>(this.resourceUrl, userCreate, { observe: 'response' });
  }
}
