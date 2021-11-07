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

import { Bot } from "./src/Bot";
import { Command } from "./src/Command";
import { Client, VoiceState } from "discord.js";
import { SetupCommand } from "./src/SetupCommand";
import { NotifyCommand } from "./src/NotifyCommand";
import { stateUpdate } from "./src/stateUpdate";

// Import environment variables.
require('dotenv').config();

// Import Discord.js library
const Discord = require("discord.js");

// Define the intents the bot
// will use.
const intents = [
  Discord.Intents.FLAGS.GUILDS,
  Discord.Intents.FLAGS.GUILD_VOICE_STATES,
  Discord.Intents.FLAGS.GUILD_MESSAGES,
  Discord.Intents.FLAGS.DIRECT_MESSAGES
];

const commands: Command[] = [
  new NotifyCommand(Discord),
  new SetupCommand(Discord)
]

// Construct a custom Bot class
// to deal with discord js.
const bot = new Bot(process.env.TOKEN, intents, commands);

// Begin listening to commands
// and print information to the
// console.
bot.listen(function (client: Client) {
  console.log("NOTIFY BOT ONLINE");
  console.log("BanDev | 1.0.0");
  console.log("===");
  console.log("AWAITING COMMANDS...");
});

// When something changes in a
// Voice Channel.
bot.vcUpdate(function (before: VoiceState, after: VoiceState) {
  stateUpdate(Discord, before, after); 
});