import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AgentCenter } from 'src/app/model/agent-center-model';
import { AgentType } from 'src/app/model/agent-type-model';
import { AID } from 'src/app/model/aid-model';
import { User } from 'src/app/model/user-model';
import { AgentService } from 'src/app/services/agent.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  connection: WebSocket = new WebSocket("ws://localhost:8080/Chat-war/ws/chat");
  currentUser: User = new User();
  agentTypes: AgentType[] = [];
  runningAgents: AID[] = [];
  name: String = '';
  type: String = '';

  constructor(private userService: UserService, private agentService: AgentService,
    private toastr: ToastrService, private router: Router) { }

  ngOnInit(): void {
    this.currentUser = this.userService.getCurrentUser();
    this.connection = new WebSocket("ws://localhost:8080/Chat-war/ws/" + this.currentUser.username);
    console.log("ws://localhost:8080/Chat-war/ws/" + this.currentUser.username);
    this.initSocket();
  }

  initSocket() {
    //let connection = new WebSocket("ws://localhost:8080/Chat-war/ws/chat");
    this.connection.onopen = function () {
      console.log("Socket is open");
    }

    this.connection.onclose = function () {
      //connection = null;
    }

    this.connection.onmessage = (msg) => {
      const data = msg.data.split("!");
      if (data[0] === "AGENT_TYPES") {
        this.agentTypes = [];
        data[1].split("|").forEach((type: string) => {
          if (type) {
            let typeData = type.split(",");
            this.agentTypes.push(new AgentType(typeData[0]));
          }
        });
      }
      else if (data[0] === "RUNNING_AGENTS") {
        this.runningAgents = [];
        data[1].split("|").forEach((agents: string) => {
          if (agents) {
            let agentsData = agents.split(",");
            this.runningAgents.push(new AID(agentsData[0], new AgentCenter(agentsData[2], agentsData[1]), new AgentType(agentsData[3])));
          }
        });
      }
      else {
        this.toastr.success(data[1]);
      }
    }
  }

  logout() {
    this.toastr.success('Logged out: Yes!');
    this.currentUser = new User();
    this.userService.setCurrentUser(this.currentUser);
    this.connection = new WebSocket("ws://localhost:8080/Chat-war/ws/chat");
    this.router.navigate(['/home']);
  }

  getAgentTypes() {
    this.agentService.getAgentTypes();
  }

  getRunningAgents() {
    this.agentService.getRunningAgents();
  }

  runAgent() {
    this.agentService.runAgent(this.name, this.type);
  }

  stopAgent(aid: AID) {
    this.agentService.stopAgent(aid);
  }
}
