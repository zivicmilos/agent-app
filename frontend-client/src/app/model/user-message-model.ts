import { User } from "./user-model";

export class UserMessage {
    constructor(
        public receiver: User = new User(),
        public sender: User = new User(),
        public date: Date = new Date(),
        public subject: String = '',
        public content: String = ''
    ) {}
  }