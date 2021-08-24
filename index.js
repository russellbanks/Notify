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
  }  
});

// NEEDS REWRITE!!!!
// When someone leaves or joins 
// a VC.
client.on('voiceStateUpdate', (oldMember, newMember) => {
    let newUserChannel = newMember.channelId
    let oldUserChannel = oldMember.channelId
    
    console.log(newUserChannel)
    if(oldUserChannel == undefined && newUserChannel != undefined && newMember.member.user.bot != true) {

        // User Joins a voice channel
        const channel = newMember.guild.channels.cache.find(channel => channel.name === "vcupdates");

        let guild = newMember.guild;
        let member = guild.members.fetch(newMember.member.id);

        let nickname = member ? member.nickname : null;

        if(typeof nickname === "undefined") {
            nickname = newMember.member.user.username;
        }

        const exampleEmbed = new Discord.MessageEmbed()
            .setColor('#0067f4')
            .setTitle(nickname + ' just joined ' + newMember.channel.name)
            .setAuthor(nickname, newMember.member.user.displayAvatarURL())
            .setTimestamp()

        channel.send({ embeds: [exampleEmbed] })
   
     } else if(newUserChannel === undefined && oldUserChannel != undefined){
     
     }
})