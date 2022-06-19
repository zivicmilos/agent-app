import { AgentCenter } from "./agent-center-model";

export class User {
    constructor(
      public username: string='',
      public password: string='',
      public agentCenter: AgentCenter = new AgentCenter()
    ) {}
  }