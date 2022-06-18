import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserMessage } from '../model/user-message-model';
import { User } from '../model/user-model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  private baseUrl = 'http://localhost:8080/Chat-war/api/chat/';

  register(user: User) {
    return this.http.post<any>(this.baseUrl + 'users/register', user).subscribe();
  }

  login(user: User) {
    return this.http.post(this.baseUrl + 'users/login', user).subscribe();
  }

  getRegisteredUsers() {
    return this.http.get(this.baseUrl + 'users/registered').subscribe();
  }

  getLoggedinUsers() {
    return this.http.get(this.baseUrl + 'users/loggedIn').subscribe();
  }

  logout(user: User) {
    return this.http.delete(this.baseUrl + 'users/loggedIn/'+user.username).subscribe();
  }

  getMessages(user: User) {
    return this.http.get(this.baseUrl + 'messages/'+user.username).subscribe();
  }
  
  send(message: UserMessage) {
    return this.http.post(this.baseUrl + 'messages/user', {sender: message.sender.username, receiver: message.receiver.username,
      subject: message.subject, content: message.content}).subscribe();
  }

  sendToAll(message: UserMessage) {
    return this.http.post(this.baseUrl + 'messages/all', {sender: message.sender.username, receiver: message.receiver.username,
      subject: message.subject, content: message.content}).subscribe();
  }

  setCurrentUser(user: User) {
    sessionStorage.setItem('user', JSON.stringify(user));
  }

  getCurrentUser() {
    let s = sessionStorage.getItem('user');
    let ss: string = '';
    if (s !== null) {
      ss = s;
    }
    return JSON.parse(ss);
  }

}
