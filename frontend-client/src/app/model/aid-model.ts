import { AgentCenter } from "./agent-center-model";
import { AgentType } from "./agent-type-model";

export class AID {
    constructor(
        public name: string = '',
        public agentCenter: AgentCenter = new AgentCenter(),
        public type: AgentType = new AgentType()
    ) { }
}