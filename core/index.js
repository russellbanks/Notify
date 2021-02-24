//Dotenv library
require('dotenv').config();

//Get setting variables
const server = process.env.SERVER;
const prefix = process.env.PREFIX;
const name = process.env.NAME;
const pfp = process.env.PFP;
const color = process.env.COLOR;
const game = process.env.GAME;
const repo = process.env.REPO;

//The class that will be accessed by index.js
module.exports = class Core{

    //When someone uses the help command
    help(message, discord) {
        //Send a text message first for people with embeds disabled
        message.channel.send("Here is all the commands I know:");
        //Create a nice embed to send back to tell the user the commands it knows
        const embed = new discord.MessageEmbed()
            .setColor(color)
            .setTitle('Help')
            .setURL('https://github.com/jackdevey/byte/')
            .setThumbnail(pfp)
            .addFields(
                { name: prefix+'play [term]', value: 'Play a song in your vc, or add it to a queue', inline: true},
                { name: prefix+'playlist [url]', value: "Play a whole playlist cus you're too lazy to do it one by one", inline: true },
                { name: prefix+'q, '+prefix+'queue', value: 'List the songs in the queue', inline: true },
                { name: prefix+'pause', value: 'Pause the song', inline: true },
                { name: prefix+'resume', value: 'Resume the song', inline: true },
                { name: prefix+'skip', value: 'Skip the song', inline: true },
                { name: prefix+'loop', value: 'Loop the current song', inline: true },
                { name: prefix+'shuffle', value: 'Shuffle the queue', inline: true },
                { name: prefix+'clear', value: 'Stop playing the song & clear the queue', inline: true },
                { name: prefix+'progress', value: 'Display a progress bar', inline: true }
            );
        //Send the command to the channel where the message was recieved
        message.channel.send(embed);
    }

    //When an unknown command is sent with the bot's prefix
    unknown(message, discord, command) {
        //Send a text message first for people with embeds disabled
        message.channel.send("Unknown command **" + command + "**:");
        //Create a nice embed to send back to tell the user the command is unknown
        const embed = new discord.MessageEmbed()
            .setColor(color)
            .setTitle("Error!")
            .setDescription("The command **" + command + "** couldn't be found here!")
            .setFooter('Handled by '+name+', ' + server, pfp);

        //Send the command to the channel where the message was recieved
        message.channel.send(embed);
    }

    //Debug info
    debug(message, discord, client) {
        //Send a text message first for people with embeds disabled
        message.channel.send("Debug info:");
        //Create a nice embed to send back to tell the user all the debug info
        const embed = new discord.MessageEmbed()
            .setColor(color)
            .setTitle("Debug Information")
            .addFields("Prefix", prefix, true)
            .addFields("Name", name, true)
            .addFields("Colour", color, true)
            .addFields("Game", game, true)
            .addFields("Hosting Server", server, true)
            .addFields("Repository", repo, true)
            .addFields("Server Count", client.guilds.size, true)
            .setFooter('Handled by '+name+', ' + server, pfp);

        //Send the command to the channel where the message was recieved
        message.channel.send(embed);
    }
}
