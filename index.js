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
    // Find the channel to send the 
    // message to.
    let updateChannel = before.guild.channels.cache.find(channel => channel.name === "vcupdates");

    // Get the member in question.
    let member = before.member;

    // If they are a bot, ignore them.
    if(member.user.bot == true) return;

    // Decide on what the message should
    // say based on if they joined or left.
    var message;
    if(before.channel == undefined && updated.channel != undefined) {
      // User joined a VC
      message = `${member.displayName} just joined ${updated.channel.name}`
    } else if(before.channel != undefined && updated.channel == undefined) {
      // User left a VC
      message = `${member.displayName} just left ${before.channel.name}`
    } else if(before.channel != undefined && updated.channel != undefined && before.channel != updated.channel) {
      // User switched VCs
      message = `${member.displayName} just switched from ${before.channel.name} to ${updated.channel.name}`
    }

    // Build the embed.
    const embed = new Discord.MessageEmbed()
            .setColor('#0067f4')
            .setTitle(message)
            .setAuthor(member.displayName, member.user.displayAvatarURL())
            .setTimestamp()

    // Send the embed in the special
    // channel.
    updateChannel.send({ embeds: [embed] })
})