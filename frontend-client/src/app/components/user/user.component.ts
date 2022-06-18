import { DatePipe } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Host } from 'src/app/model/host-model';
import { UserMessage } from 'src/app/model/user-message-model';
import { User } from 'src/app/model/user-model';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  currentUser: User = new User;
  users: User[] = [];
  loggedinUsers: User[] = [];
  connection: WebSocket = new WebSocket("ws://localhost:8080/Chat-war/ws/chat");
  messages: UserMessage[] = [];
  pipe = new DatePipe('en-US');
  message: UserMessage = new UserMessage;

  constructor(private userService: UserService, private toastr: ToastrService, private router: Router) { }

  ngOnInit(): void {
    this.currentUser = this.userService.getCurrentUser();
    this.connection = new WebSocket("ws://localhost:8080/Chat-war/ws/"+this.currentUser.username);
    console.log("ws://localhost:8080/Chat-war/ws/"+this.currentUser.username);
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
      if (data[0] === "REGISTERED") {
        this.users = [];
        data[1].split("|").forEach((user: string) => {
          if (user) {
            let userData = user.split(",");
            this.users.push(new User(userData[0], userData[1], new Host(userData[2], userData[3])));
          }
        });
      }
      else if (data[0] === "LOGGEDIN") {
        this.loggedinUsers = [];
        data[1].split("|").forEach((user: string) => {
          if (user) {
            let userData = user.split(",");
            this.loggedinUsers.push(new User(userData[0], userData[1], new Host(userData[2], userData[3])));
          }
        });
      }
      else if (data[0] === "LOGGEDOUT") {
        this.toastr.success(data[1]);
        if (data[1].split(":")[1].trim() === "Yes") {
          this.currentUser = new User();
          this.userService.setCurrentUser(this.currentUser);
          this.connection = new WebSocket("ws://localhost:8080/Chat-war/ws/chat");
          this.router.navigate(['/home']);
        }
      }
      else if (data[0] === "MESSAGES") {
        this.messages = [];
        data[1].split("|").forEach((userMessage: string) => {
          if (userMessage) {
            let userMessageData = userMessage.split(",");
            this.messages.push(new UserMessage(new User(userMessageData[0], ''), new User(userMessageData[1], ''), 
              new Date(Date.parse(userMessageData[2])), userMessageData[3], userMessageData[4]));
          }
        });
      }
      else {
        this.toastr.success(data[1]);
      }
    }

  }

  getRegisteredUsers() {
    this.userService.getRegisteredUsers();
  }

  getLoggedinUsers() {
    this.userService.getLoggedinUsers();
  }

  logout() {
    this.userService.logout(this.currentUser);
    //this.router.navigate(['/home']);
    //this.connection.close();
  }

  getMessages() {
    this.userService.getMessages(this.currentUser);
  }

  send() {
    this.message.sender.username = this.currentUser.username;
    this.message.date = new Date(Date.now());
    this.userService.send(this.message);
  }

  sendToAll() {
    this.message.sender.username = this.currentUser.username;
    this.message.date = new Date(Date.now());
    this.userService.sendToAll(this.message);
  }

}
