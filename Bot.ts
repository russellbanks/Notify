import { Client, Intents, VoiceState } from "discord.js";
import { Command } from "./Command";

export class Bot {

    client: Client;
    token: string;
    intents: Intents[];
    commands: Command[];

    constructor(token: string, intents: Intents[], commands: Command[]) {
        this.token = token;
        this.intents = intents;
        this.commands = commands;
    }

    public listen(ready: (client: Client) => void) {
        this.client = new Client({ intents: this.intents });
        this.client.login(this.token);
        this.onInteraction();
        ready(this.client);
    }

    public vcUpdate(respond: (before: VoiceState, after: VoiceState) => void) {
        this.client.on("voiceStateUpdate", 
        (before: VoiceState, after: VoiceState) => 
        {respond(before, after);}) 
    }

    /**
     * When an interaction is created, try to 
     * match the name of the command to a command
     * passed to the commands array of this class.
     */

    private onInteraction() {
        this.client.on("interactionCreate", async interaction => {
            if (!interaction.isCommand()) return;
            let command = this.commands.find(it => it.name === interaction.commandName);
            await command.run(interaction, null);
        });
    }

}