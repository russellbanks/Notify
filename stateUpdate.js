"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.stateUpdate = void 0;
function stateUpdate(discord, before, updated) {
    // Find the channel to send the 
    // message to.
    let updateChannel = before.guild.channels.cache.find(channel => channel.name === "vcupdates");
    before.client.guilds.cache.get(before.guild.id).commands.create({
        name: 'notify',
        description: 'POOOOOOO HAHAHAHAHAHAH AMOGUS'
    });
    // Get the member in question.
    let member = before.member;
    // If they are a bot, ignore them.
    if (member.user.bot == true)
        return;
    // Decide on what the message should
    // say based on if they joined or left.
    var message;
    if (before.channel == undefined && updated.channel != undefined) {
        // User joined a VC
        message = `${member.displayName} just joined ${updated.channel.name}`;
        console.log(`${member.user.tag} JOINED ${updated.channel.name}`);
    }
    else if (before.channel != undefined && updated.channel == undefined) {
        // User left a VC
        message = `${member.displayName} just left ${before.channel.name}`;
        console.log(`${member.user.tag} LEFT ${before.channel.name}`);
    }
    else if (before.channel != undefined && updated.channel != undefined && before.channel != updated.channel) {
        // User switched VCs
        message = `${member.displayName} just switched to ${updated.channel.name}`;
        console.log(`${member.user.tag} SWITCHED TO ${updated.channel.name}`);
    }
    else
        return;
    // Build the embed.
    const embed = new discord.MessageEmbed()
        .setColor('#0067f4')
        .setTitle(message)
        .setAuthor(member.displayName, member.user.displayAvatarURL())
        .setTimestamp();
    // Send the embed in the special
    // channel.
    updateChannel.send({ embeds: [embed] });
}
exports.stateUpdate = stateUpdate;
//# sourceMappingURL=stateUpdate.js.map