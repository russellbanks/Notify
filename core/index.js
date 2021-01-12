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
    delete(message, params){
        var limits = params[1]
        if(limits >= 0){
            if (message.member.roles.cache.find(r => r.name === "delete")) {
                async function clear() {
                    message.delete()
                    const fetched = await message.channel.messages.fetch({limit: limits})
                    message.channel.bulkDelete(fetched)
                }
                clear()
                message.channel.send("Deleted " + limits + " message(s)")
            }else{
                message.channel.send("Invalid use, make sure you have the `delete` role before deleting messages.")
            }
        }else{
            message.channel.send("Invalid command, please use: ```?del n``` Where n is the ammount of messages to delete.")
        }
    }

    // Scary simon
    simon(channel){
        channel.send("Get ballmer'd", {files: ["https://lh3.googleusercontent.com/bfo1X3_VwELaUuNBFGd6aRy6OP91e4AWWi40uZ3NoD7jhIkImnAZZ--htzJ-2ynKtuzEYR4n4Y3pHNSmTsHPdp1pvSXbbDC-s9yWes7Dv3J2MfAEVN8G9knRvt7XirFeMjSPabHpY_mwzlzu0x-4DosDnmSTxZaCFm4mHI4ndb25WwlyYouD8lGbUs96XTN3Q1MKH7uziHk6l2X2bDdx751aEqi-ZnPfIbo93KSmpHvvoZCULakZ69uWn8iiNMXQKrJcT8NgESqt5uai7Q-SnUjh2rzw58dANidAq_dZXfxWoOfnJUCF-zYNrBkmdql3dveutNtk4XnVQB3IVKcYMwpBZ2FgaD1GwHSkpIBr5AS0AKnbLeJbsX0gTY0oPLxrXG1vFCzWYjDFFQYTosbqHBw4o5a5l1KVRHyDul4jK5b6MlhmVmJwYOj6wGCmFUsKhoj98Oqs8uWDGZMmH1DYa5IoddNsdddoxg1EO9JdgJVrkt8_53AnOXzQFbtJn3drpyOAk4kb-0nuOEZbk2tee0u_1PcsRs88LICQQ5ieTTzBRYZBe9Yvem5rwcOlgNf7ekyUU7HJ4n1KOTt766KQsnKOHmScoXc-zYLuu8vcHRVsQ5ae3CnNNvLPJf-4LYvOQPoXJPGhrIvetDul0m8jqkcgM1NPHH-IKYmgepaHJ1b5E_v1xjiG_u8U8bQk=w720-h416-no?authuser=0"]})
    }
}
