import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AID } from '../model/aid-model';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class AgentService {

  constructor(private http: HttpClient, private userService: UserService) { }

  private baseUrl = 'http://localhost:8080/Chat-war/api/agents/';

  getAgentTypes() {
    return this.http.get(this.baseUrl + 'classes').subscribe();
  }

  getRunningAgents() {
    return this.http.get(this.baseUrl + 'running').subscribe();
  }

  runAgent(name: String, type: String) {
    return this.http.put(this.baseUrl + 'running/'+type+'/'+name, this.userService.getCurrentUser().username).subscribe();
  }

  stopAgent(aid: AID) {
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      body: aid
    };
    return this.http.delete(this.baseUrl + 'running', options).subscribe();
  }
}
