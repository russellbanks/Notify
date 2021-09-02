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

// Create a new client class and
// login with token.
const client = new Discord.Client({ intents: intents });
client.login(process.env.TOKEN);

client.on("ready", () => {
  console.log("NOTIFY BOT ONLINE");
  console.log("BanDev | 1.0.0");
  console.log("===");
  console.log("AWAITING COMMANDS...");
});

// When an interaction gets created
// by a user.
client.on('interactionCreate', async interaction => {
  // Ensure its a command
	if (!interaction.isCommand()) return;

  // Decide which interaction is 
  // needed.
  switch(interaction.commandName) {
    case 'notify': require("./notify.js")(interaction); break;
    case 'setup': require("./setup.js")(interaction); break;
  }  
});

// When someone leaves or joins 
// a VC.
client.on('voiceStateUpdate', (before, updated) => { 
  require("./stateUpdate.ts")(Discord, before, updated); 
})