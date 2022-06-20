import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ACLMessage } from '../model/acl-message-model';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor(private http: HttpClient, private userService: UserService) { }

  private baseUrl = 'http://localhost:8080/Chat-war/api/messages/';

  getPerformatives() {
    const options = {
      headers: new HttpHeaders({
        'user-agent': this.userService.getCurrentUser().username
      })
    };
    
    return this.http.get(this.baseUrl, options).subscribe();
  }

  sendMessage(aclMessage: ACLMessage) {
    return this.http.post(this.baseUrl, aclMessage).subscribe();
  }
}
