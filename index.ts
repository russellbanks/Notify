import { Bot } from "./Bot";
import { Command } from "./Command";
import { Client, VoiceState } from "discord.js";
import { SetupCommand } from "./SetupCommand";
import { NotifyCommand } from "./NotifyCommand";
import { stateUpdate } from "./stateUpdate";

// Import environment variables.
require('dotenv').config();

// Import Discord.js library
const Discord = require("discord.js");

// Define the intents the bot
// will use.
const intents = [
  Discord.Intents.FLAGS.GUILDS,
  Discord.Intents.FLAGS.GUILD_VOICE_STATES,
  Discord.Intents.FLAGS.GUILD_MESSAGES
];

const commands: Command[] = [
  new NotifyCommand()
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