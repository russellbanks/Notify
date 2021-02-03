module.exports = class Core{

    // Speaks the truth
    hello(channel){
        channel.send("Hello")
    }

    // Sends a pic of BALLMER 
    ballmer(channel){
        switch(Math.floor(Math.random() * 10)){
            case 0:
                channel.send("Get ballmer'd", {files: ["https://cdn.geekwire.com/wp-content/uploads/2019/10/0151-Summit-20191008-DD-630x420.jpg"]})
                break;
            case 1:
                channel.send("Get ballmer'd `Jack's pee pee is this long`", {files: ["https://assets.pando.com/uploads/2013/08/steve_ballmer_at_ces_2010.jpg"]})
                break;
            case 2:
                channel.send("Get ballmer'd", {files: ["data:image/jpeg;base64,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"]})
                break;
            case 3:
                channel.send("Get ballmer'd", {files: ["https://cached.imagescaler.hbpl.co.uk/resize/scaleWidth/743/cached.offlinehbpl.hbpl.co.uk/news/OMC/Ballmer-20130823031812891.jpg"]})
                break;
            case 4:
                channel.send("Get ballmer'd", {files: ["https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTPOzqjZz58M2nE87EV2WE6B68BlaTQi1O2dQ&usqp=CAU"]})
                break;
            default:
                channel.send("Get ballmer'd", {files: ["https://www.incimages.com/uploaded_files/image/1920x1080/Steve-Ballmer-commencement-address_37754.jpg"]})
                break;
        }
    }
    
    // Sends a video of BALLMER saying developer * ∞
    developers(channel){
        channel.send("https://www.youtube.com/watch?v=KMU0tzLwhbE")
    }

    // Deletes the messages
    delete(message, params){
        var limits = params[1] + 1 
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
        channel.send("Simon", {files: ["https://jackisa.ninja/Screenshot_20190729-023116.jpg"]})
    }

    // help me 
    help(channel, discord) {
        const exampleEmbed = new discord.MessageEmbed()
            .setColor('#0067f4')
            .setTitle('Help (music)')
            .setURL('https://github.com/jackdevey/byte/')
            .setThumbnail('https://cdn.discordapp.com/app-icons/791303709639442444/9c560fdb926ef2af0d0920ef412e618c.png')
            .addFields(
                { name: '?play [term]', value: 'Play a song in your vc, or add it to a queue', inline: true},
                { name: '?q, ?queue', value: 'List the songs in the queue', inline: true },
                { name: '?pause', value: 'Pause the song', inline: true },
                { name: '?resume', value: 'Resume the song', inline: true },
                { name: '?skip', value: 'Skip the song', inline: true },
                { name: '?loop', value: 'Loop the current song', inline: true },
                { name: '?shuffle', value: 'Shuffle the queue', inline: true },
                { name: '?clear', value: 'Stop playing the song & clear the queue', inline: true },
                { name: '?progress', value: 'Display a progress bar', inline: true }
            );

        channel.send(exampleEmbed);

        const exampleEmbed2 = new discord.MessageEmbed()
            .setColor('#0067f4')
            .setTitle('Help (general)')
            .setURL('https://github.com/jackdevey/byte/')
            .setThumbnail('https://cdn.discordapp.com/app-icons/791303709639442444/9c560fdb926ef2af0d0920ef412e618c.png')
            .addFields(
                { name: '?ballmer', value: 'STEVE BALLMER', inline: true},
                { name: '?russell, ?hannah, ?hollie', value: "'insults' them", inline: true },
                { name: '?jack', value: 'Talks about the seggsyman', inline: true },
                { name: '?simon', value: 'Im not just a pretty face', inline: true },
                { name: '?bigd', value: 'BIG D', inline: true },
                { name: '?hello', value: 'Just says hello', inline: true },
                { name: '?del [value]', value: 'Tries to delete the messages, but it probably wont', inline: true },
            );

        channel.send(exampleEmbed2);
    }
}
