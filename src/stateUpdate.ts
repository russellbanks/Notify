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

export function stateUpdate(discord, before, updated) {
  // Find the channel to send the 
    // message to.
    let updateChannel = before.guild.channels.cache.find(channel => channel.name === "vcupdates");

    // Get the member in question.
    let member = before.member;

    // If they are a bot, ignore them.
    if(member.user.bot == true) return;

    // Decide on what the message should
    // say based on if they joined or left
    // or something else.

    if(before.channel == undefined && updated.channel != undefined) {

      // User joined a VC
      console.log(`${member.user.tag} JOINED ${updated.channel.name}`);
      return send(discord, `${member.displayName} just joined ${updated.channel.name}`, `🎧`, member, updateChannel);

    }
    
    if(before.channel != undefined && updated.channel == undefined) {

      // User left a VC
      console.log(`${member.user.tag} LEFT ${before.channel.name}`);
      return send(discord, `${member.user.tag} just left ${before.channel.name}`, `🚪`, member, updateChannel);

    } 
    
    if(before.channel != undefined && updated.channel != undefined && before.channel != updated.channel) {

      // User switched VCs
      console.log(`${member.user.tag} SWITCHED TO ${updated.channel.name}`);
      return send(discord, `${member.displayName} just switched to ${updated.channel.name}`, `🔄️`, member, updateChannel);

    }

    if(updated.streaming) {

      // User is streaming
      console.log(`${member.user.tag} IS LIVE IN ${updated.channel.name}`);
      return send(discord, `${member.displayName} is live in ${updated.channel.name}`, `🔴`, member, updateChannel);

    }

}

/**
 * Send the embed
 * 
 * @param discord Discord.js instance
 * @param message The message
 * @param emoji The emoji
 * @param member The member
 * @param updateChannel The channel to send updates too
 */

function send(discord, message: String, emoji: String, member, updateChannel) {

  // Build the embed.
  const embed = new discord.MessageEmbed()
    .setColor('#0067f4')
    .setTitle(message)
    .setAuthor(member.displayName, member.user.displayAvatarURL())
    .setFooter(emoji)
    .setTimestamp();

  // Send the embed in the special
  // channel.
  updateChannel.send({ embeds: [embed] });
}