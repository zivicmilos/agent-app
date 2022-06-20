import { AID } from "./aid-model";

export class ACLMessage {
    constructor(
      public performative: string = '',
      public receivers: AID[] = [],
      public sender: AID = new AID(),
      public userArgs: {[k: string]: string} = {}
    ) { }
  }