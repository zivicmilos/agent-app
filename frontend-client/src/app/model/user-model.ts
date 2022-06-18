import { Host } from "./host-model";

export class User {
    constructor(
      public username: string='',
      public password: string='',
      public host: Host = new Host()
    ) {}
  }