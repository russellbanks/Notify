const Discord = require("discord.js"); // imports the discord library
require('dotenv').config();

const Core =  require('./core');
var core = new Core();

const People =  require('./people');
var people = new People();

const Plug =  require('./plug');
var plug = new Plug();

const client = new Discord.Client(); // creates a discord client
const token = process.env.TOKEN // gets your token from the file

client.once("ready", () => { // prints "Ready!" to the console once the bot is online
    console.log("Ready!");
    client.user.setActivity("with yo momma");
});

client.on("message", message => { // runs whenever a message is sent
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
    }
    if(message.content.startsWith("?del")){
        core.delete(message, message.content.split(" "))
    }
});


client.login(token); 