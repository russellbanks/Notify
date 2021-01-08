const Discord = require("discord.js"); // imports the discord library
require('dotenv').config()

const client = new Discord.Client(); // creates a discord client
const token = process.env.TOKEN // gets your token from the file

client.once("ready", () => { // prints "Ready!" to the console once the bot is online
	console.log("Ready!");
});

client.on("message", message => { // runs whenever a message is sent
    if (message.content === "?hello") { // checks if the message says "?random"
        message.channel.send("Jack William Devey is soo seggsy!!"); // sends a message to the channel with the number
    }else if(message.content === "?ballmer"){
        message.channel.send("Get ballmer'd", {files: ["https://miro.medium.com/max/4408/1*KvhM-ArA5RkpYLi7L_Qtdw.jpeg"]});
    }else if(message.content === "?developers"){
        message.channel.send("https://www.youtube.com/watch?v=KMU0tzLwhbE")
    }else if(message.content === "?hannah"){
        message.channel.send("Hannah smells")
    }else if(message.content === "?russell"){
        var num = Math.floor(Math.random() * 10);  
        if(num == 0){
            message.channel.send("russell stinks")
        }else if(num == 1){
            mmessage.channel.send("russell is bad")
        }else if(num == 2){
            message.channel.send("russell is not as cool as jack")
        }else if(num == 3){
            message.channel.send("russell is stupid")
        }else{
            message.channel.send("russell is bad at coding")
        }
    }
    else if(message.content === "?bandev"){
        message.channel.send("BanDev is really cool https://bandev.uk")
    }
});


client.login(token); 