import { Client, Intents, VoiceState } from "discord.js";
import { Command } from "./Command";


export class Bot {

    client: Client;

    token: string;
    intents: Intents[];

    constructor(token: string, intents: Intents[]) {
        this.token = token;
        this.intents = intents;
    }

    public listen(ready: (client: Client) => void) {
        this.client = new Client({ intents: this.intents });
        this.client.login(this.token);
        ready(this.client);
    }

    public interaction(decide: (command: string) => Command) {
        this.client.on("interactionCreate", async interaction => {
            if (!interaction.isCommand()) return;
            let command = decide(interaction.commandName);
            await command.run(interaction, null);
          })
    } 

    public vcUpdate(respond: (before: VoiceState, after: VoiceState) => void) {
        this.client.on("voiceStateUpdate", 
        (before: VoiceState, after: VoiceState) => 
        {respond(before, after);}) 
    }

}