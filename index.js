const Discord = require("discord.js"); // imports the discord library
require('dotenv').config();
const { Player }  = require("discord-music-player");

const client = new Discord.Client(); // creates a discord client
const token = process.env.TOKEN // gets your token from the file
const server = process.env.SERVER
const prefix = process.env.PREFIX
const name = process.env.NAME

const player = new Player(client, {
    leaveOnEnd: true,
    leaveOnStop: true,
    leaveOnEmpty: true,
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
var games = new Games(client);

client.once("ready", () => { // prints "Ready!" to the console once the bot is online
    console.log("Ready!" + name);
    if(name == "Byte2") {
        client.user.setActivity("with yo momma too");
    }else {
        client.user.setActivity("with yo momma");
    }
    
});

client.on("message", message => { // runs whenever a message is sent
    if(message.content.startsWith(prefix+"del")){
        core.delete(message, message.content.split(" "))
    }else if(message.content.startsWith(prefix+"hangman")){
        games.startHangman(message, message.content.split(" "))
    }else if(message.content.startsWith(prefix+"playlist")){
        music.playlist(message, message.content, player, Discord, server)
    }else if(message.content.startsWith(prefix+"play")){
        music.request(message, message.content, player, Discord, server)
    }else if(message.content.startsWith(prefix+"analyse")){
        core.toxic(message, message.content.split(" "), Discord)
    }else{
        switch(message.content){
            case prefix+"hello":
                core.hello(message.channel)
                break; 
            case prefix+"ballmer":
                core.ballmer(message.channel)
                break;
            case prefix+"developers":
                core.developers(message.channel)
                break;
            case prefix+"russell":
                people.russell(message.channel)
                break;
            case prefix+"hannah":
                people.hannah(message.channel)
                break;
            case prefix+"bandev":
                plug.bandev(message.channel)
                break;
            case prefix+"kickrus":
                people.kick(message.channel, "russell")
                core.ballmer(message.channel)
                break;
            case prefix+"hollie":
                people.hollie(message.channel)
                break;
            case prefix+"jack":
                people.jack(message.channel)
                break;
            case prefix+"simon":
                core.simon(message.channel)
                break;
            case prefix+"bigd":
                people.bigd(message.channel)
                break;
            case prefix+"skip":
                music.skip(message, player)
                break;
            case prefix+"clear":
                music.clear(message, player)
                break;
            case prefix+"shuffle":
                music.shuffle(message, player)
                break;
            case prefix+"q":
                music.queue(message, player)
                break;
            case prefix+"queue":
                music.queue(message, player)
                break;
            case prefix+"loop":
                music.loop(message, player)
                break;
            case prefix+"pause":
                music.pause(message, player)
                break;
            case prefix+"resume":
                music.resume(message, player)
                break;
            case prefix+"progress":
                music.progress(message, player)
                break;
            case prefix+"help":
                core.help(message.channel, Discord)
                break;
        }
    }
});


client.login(token); 