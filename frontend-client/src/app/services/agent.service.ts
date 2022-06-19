import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AgentService {

  constructor(private http: HttpClient) { }

  private baseUrl = 'http://localhost:8080/Chat-war/api/agents/';

  getAgentTypes() {
    return this.http.get(this.baseUrl + 'classes').subscribe();
  }
}
