/*
 * Notify
 * Copyright (c) 2021 BanDev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import { Client, Intents, VoiceState } from "discord.js";
import { Command } from "./Command";

export class Bot {

    client: Client;
    token: string;
    intents: Intents[];
    commands: Command[];
    ready: (client: Client) => void;

    constructor(token: string, intents: Intents[], commands: Command[]) {
        this.token = token;
        this.intents = intents;
        this.commands = commands;
    }

    public listen(ready: (client: Client) => void) {
        this.client = new Client({ intents: this.intents });
        this.ready = ready;
        this.client.login(this.token);
        this.onInteraction();
        this.onMessage();
        this.onReady();
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

    private onReady() {
        this.client.on("ready", client => {
            client.user.setActivity('bandev.uk/notify', { type: 'PLAYING' });
            this.ready(client);
        });
    }

    private onMessage() {
        this.client.on("messageCreate", async message => {
            console.log(message)
        });
    }

}