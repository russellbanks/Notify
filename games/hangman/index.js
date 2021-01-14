module.exports = class Hangman{

    // This is the constructor of the class
    constructor(client){
        this.client = client
    }

    start(message){
        // Send a pm to the user asking for the phrase
        message.author.send("Please reply with the phrase for people to guess")
        // Await their reply
        this.client.on('message', dm => {
            if (dm.channel.type == "dm" && dm.author == message.author) {
                console.log(dm.message.content)
                dm.author.send("Ok, the game will start now")
              return;
            }
          });
    }
}
