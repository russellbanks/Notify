module.exports = class Core{

    // Speaks the truth
    hello(channel){
        channel.send("Hello")
    }

    // Sends a pic of BALLMER 
    ballmer(channel){
        channel.send("Get ballmer'd", {files: ["https://miro.medium.com/max/4408/1*KvhM-ArA5RkpYLi7L_Qtdw.jpeg"]})
    }
    
    // Sends a video of BALLMER saying developer * ∞
    developers(channel){
        channel.send("https://www.youtube.com/watch?v=KMU0tzLwhbE")
    }

    // Deletes 1000 messages
    delete(message){
        async () => {
            let fetched;
            do {
              fetched = await channel.messages.fetch({limit: 100})
              message.channel.bulkDelete(fetched);
            }
            while(fetched.size >= 2);
          }
          message.channel.send("Deleted 100 messages")
    }
}
