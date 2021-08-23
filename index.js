

const Discord = require("discord.js");

const intents = [
    Discord.Intents.FLAGS.GUILDS,
    Discord.Intents.FLAGS.GUILD_MEMBERS,
    Discord.Intents.FLAGS.GUILD_BANS,
    Discord.Intents.FLAGS.GUILD_EMOJIS_AND_STICKERS,
    Discord.Intents.FLAGS.GUILD_INTEGRATIONS,
    Discord.Intents.FLAGS.GUILD_WEBHOOKS,
    Discord.Intents.FLAGS.GUILD_INVITES,
    Discord.Intents.FLAGS.GUILD_VOICE_STATES,
    Discord.Intents.FLAGS.GUILD_PRESENCES,
    Discord.Intents.FLAGS.GUILD_MESSAGES,
    Discord.Intents.FLAGS.GUILD_MESSAGE_REACTIONS,
    Discord.Intents.FLAGS.GUILD_MESSAGE_TYPING,
    Discord.Intents.FLAGS.DIRECT_MESSAGES,
    Discord.Intents.FLAGS.DIRECT_MESSAGE_REACTIONS,
    Discord.Intents.FLAGS.DIRECT_MESSAGE_TYPING
];

const bot = new Discord.Client({ intents: intents });

require('dotenv').config();

bot.login(process.env.TOKEN)

bot.on('ready', () => {
  bot.user.setStatus('available')
  bot.user.setPresence({
    status: 'online',
    activity: {
        name: "you ;)",
        type: "LISTENING",
    }
  })
});

bot.on('voiceStateUpdate', (oldMember, newMember) => {
    let newUserChannel = newMember.channelId
    let oldUserChannel = oldMember.channelId
    
    console.log(newUserChannel)
    if(oldUserChannel == undefined && newUserChannel != undefined && newMember.member.user.bot != true) {

        // User Joins a voice channel
        const channel = newMember.guild.channels.cache.find(channel => channel.name === "vcupdates");

        let guild = newMember.guild;
        let member = guild.members.fetch(newMember.member.id);

        let nickname = member ? member.nickname : null;

        console.log(nickname);

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
   
       // User leaves a voice channel
   
     }


})

bot.on('message', message => {
  if (message.content == '/notify') {
    if(message.member.voice.channel) {
      const channel = bot.channels.cache.find(channel => channel.name === "vcupdates")

      let member = message.member
      let nickname = member ? member.displayName : null;
      var members = member.voice.channel.members;
      var otherMembers = []
      members.forEach(member => {
        if(member.displayName != nickname && !member.user.bot) {
          otherMembers.push(member.displayName)
        }
      });

      var numbers = members.size - otherMembers.length - 1
      if(numbers == 1) {
        var other = "other"
      }else{
        var other = "others"
      }
      var membersOut;

      if(otherMembers.length == 1) {
        membersOut = "with " + otherMembers[0];
      }else if(otherMembers == 0) {
        membersOut = "";
      }else {
        var final = otherMembers[otherMembers.length - 1];
        otherMembers.pop();
        membersOut = "with " + otherMembers.join(", ") + " and " + final
      }
      message.lineReplyNoMention("@everyone, " + nickname + " is in **" + member.voice.channel.name + "** " + membersOut)
    }else {
      message.reply("you must be in a voice channel to run this command")
    }
  }
});