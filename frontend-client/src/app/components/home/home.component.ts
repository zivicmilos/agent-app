import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/model/user-model';
import { UserService } from 'src/app/services/user.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from "@angular/router"

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  user: User = new User();
  users: User[] = [];
  connection: WebSocket = new WebSocket("ws://localhost:8080/Chat-war/ws/chat");

  constructor(private userService: UserService, private toastr: ToastrService, private router: Router) { }

  ngOnInit(): void {
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
            this.users.push(new User(userData[0], userData[1]));
          }
        });
      }
      else if (data[0] === "REGISTER") {
        this.toastr.success(data[1]);
      }
      else if (data[0] === "LOG_IN") {
        this.toastr.success(data[1]);
        if (data[1].split(":")[1].trim() === "Yes") {
          this.userService.setCurrentUser(this.user);
          this.router.navigate(['/user']);
        }
      }
      else {
        this.toastr.success(data[1]);
      }
    }

  }

  register() {
    //this.connection.close();
    this.connection = new WebSocket("ws://localhost:8080/Chat-war/ws/"+this.user.username);
    this.initSocket();
    this.userService.register(this.user);
  }

  login() {
    //this.connection.close();
    this.connection = new WebSocket("ws://localhost:8080/Chat-war/ws/"+this.user.username);
    this.initSocket();
    this.userService.login(this.user);
  }

}
