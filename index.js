//Discord.js library
const Discord = require("discord.js");

//Dotenv library
require('dotenv').config();

//Discord music player library
const { Player }  = require("discord-music-player");

//Core commands library
const Core = require('./core');
var core = new Core();

//Space commands library
const Space = require('./space');
var space = new Space();

//Music commands library
const Music = require('./music');
var music = new Music();

//Get setting variables
const token = process.env.TOKEN;
const server = process.env.SERVER;
const prefix = process.env.PREFIX;
const name = process.env.NAME;
const pfp = process.env.PFP;
const color = process.env.COLOR;
const url = process.env.URL;

//New discord client instance
const client = new Discord.Client();

//Define music player settings
const player = new Player(client, {
    leaveOnEnd: true,
    leaveOnStop: true,
    leaveOnEmpty: true,
    timeout: 0,
    volume: 100,
    quality: 'high',
});

//Set player as music player
client.player = player;

//When client logs successfully in
client.once("ready", () => {
    //Set the game based on how many servers the bot is in
    client.user.setPresence({
        status: 'online',
        activity: {
            name: "in " + client.guilds.cache.size + " servers | " + url,
            type: "PLAYING",
        }
    })

    //Discord / commands to help people
    client.api.applications(client.user.id).commands.post({data: {
        name: name+'Help',
        description: 'Get help and commands for ' + name + ' bot.'
    }})

    //Show debug information in the console for turn on
    console.log("Build Successful");
    console.log("=========================");
    console.log("Configured variables:");
    console.log("=========================");
    console.log("server: " + server);
    console.log("server_count: " + client.guilds.cache.size);
    console.log("prefix: " + prefix);
    console.log("name: " + name);
    console.log("pfp: " + pfp);
    console.log("color: " + color);
    console.log("url: " + url);
    console.log("=========================");
    console.log("Waiting for commands:");
    console.log("=========================");
});

client.on("message", message => {
    //If message is not meant for us, ignore it
    if (!message.content.startsWith(prefix) || message.author.bot) return;

    //Work out the command that was sent
    const args = message.content.slice(prefix.length).trim().split(/ +/);
	const command = args.shift().toLowerCase();

    //Debug things
    console.log("command recieved: " + command + " " + args);

    switch(command) {
        case "play":
            //Play song: play [name/url]
            music.play(message, args, player, Discord, server, prefix);
            break;
        case "playlist":
            //Play playlist: playlist [name/url]
            music.playlist(message, args, player, Discord, server, prefix);
            break;
        case "byteplaylist":
            //Play a byte playlist [extID]
            music.bytepl(message, Discord, args, player);
            break;
        case "skip":
            //Skips the current song: skip
            music.skip(message, player);
            break;
        case "clear":
            //Clears the queue and leaves: clear
            music.clear(message, player);
            break;
        case "shuffle":
            //Shuffles the queue: shuffle
            music.shuffle(message, player);
            break;
        case "q":
            //Shows the queue to the user: q
            music.queue(message, player);
            break;
        case "queue":
            //Shows the queue to the user: queue
            music.queue(message, player);
            break;
        case "loop":
            //Loops the current song: loop
            music.loop(message, player);
            break;
        case "pause":
            //Pauses the current song: pause
            music.pause(message, player);
            break;
        case "resume":
            //Resumes the current song: resume
            music.resume(message, player);
            break;
        case "progress":
            //Shows a progress bar for the current song: progress
            music.progress(message, player);
            break;
        case "rover":
            //Shows a mars rover picture: rover
            space.rover(message, Discord, args);
            break;
        case "help":
            //Shows a help embed for new users: help
            core.help(message, Discord);
            break;
        case "debug":
            //Shows all the debug info for the sever
            core.debug(message, Discord, client);
            break;
        default:
            //Shows an error message
            core.unknown(message, Discord, command);
            break;
        }
    });

//Log the bot in with the token provided
client.login(token); 