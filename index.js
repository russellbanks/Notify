"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const Bot_1 = require("./Bot");
const stateUpdate_1 = require("./stateUpdate");
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
const commands = [];
// Construct a custom Bot class
// to deal with discord js.
const bot = new Bot_1.Bot(process.env.TOKEN, intents, commands);
// Begin listening to commands
// and print information to the
// console.
bot.listen(function (client) {
    console.log("NOTIFY BOT ONLINE");
    console.log("BanDev | 1.0.0");
    console.log("===");
    console.log("AWAITING COMMANDS...");
});
// When something changes in a
// Voice Channel.
bot.vcUpdate(function (before, after) {
    (0, stateUpdate_1.stateUpdate)(Discord, before, after);
});
//# sourceMappingURL=index.js.map