const Discord = require("discord.js"); // imports the discord library
require('dotenv').config();
const { Player }  = require("discord-music-player");

const client = new Discord.Client(); // creates a discord client
const token = process.env.TOKEN // gets your token from the file

const player = new Player(client, {
    leaveOnEnd: true,
    leaveOnStop: false,
    leaveOnEmpty: false,
    timeout: 0,
    volume: 100,
    quality: 'high',
});

client.player = player;

const Core = require('./core');
var core = new Core();

const People = require('./people');
var people = new People();

const Plug = require('./plug');
var plug = new Plug();

const Music = require('./music');
var music = new Music();

const Games = require('./games');
const MusicPlayerError = require("discord-music-player/src/MusicPlayerError");
var games = new Games(client);

client.once("ready", () => { // prints "Ready!" to the console once the bot is online
    console.log("Ready!");
    client.user.setActivity("with yo momma");
});

client.on("message", message => { // runs whenever a message is sent
    if(message.content.startsWith("?del")){
        core.delete(message, message.content.split(" "))
    }else if(message.content.startsWith("?hangman")){
        games.startHangman(message, message.content.split(" "))
    }else if(message.content.startsWith("?playlist")){
        music.playlist(message, message.content, player)
    }else if(message.content.startsWith("?play")){
        music.request(message, message.content, player)
    }else if(message.content.startsWith("?analyse")){
        core.toxic(message, message.content.split(" "), Discord)
    }else{
        switch(message.content){
            case "?hello":
                core.hello(message.channel)
                break; 
            case "?ballmer":
                core.ballmer(message.channel)
                break;
            case "?developers":
                core.developers(message.channel)
                break;
            case "?russell":
                people.russell(message.channel)
                break;
            case "?hannah":
                people.hannah(message.channel)
                break;
            case "?bandev":
                plug.bandev(message.channel)
                break;
            case "?kickrus":
                people.kick(message.channel, "russell")
                core.ballmer(message.channel)
                break;
            case "?hollie":
                people.hollie(message.channel)
                break;
            case "?jack":
                people.jack(message.channel)
                break;
            case "?simon":
                core.simon(message.channel)
                break;
            case "?bigd":
                people.bigd(message.channel)
                break;
            case "?skip":
                music.skip(message, player)
                break;
            case "?clear":
                music.clear(message, player)
                break;
            case "?shuffle":
                music.shuffle(message, player)
                break;
            case "?q":
                music.queue(message, player)
                break;
            case "?queue":
                music.queue(message, player)
                break;
            case "?loop":
                music.loop(message, player)
                break;
            case "?pause":
                music.pause(message, player)
                break;
            case "?resume":
                music.resume(message, player)
                break;
            case "?progress":
                music.progress(message, player)
                break;
            case "?help":
                core.help(message.channel, Discord)
                break;
            case "?leave":
                music.leave(message)
                break;
        }
    }
});


client.login(token); 