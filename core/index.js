require('@tensorflow/tfjs');
require('dotenv').config();
const toxicity = require('@tensorflow-models/toxicity');

module.exports = class Core{

    // Speaks the truth
    hello(channel){
        channel.send("Hello, this is " + process.env.NAME + " running on " + process.env.SERVER)
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
                channel.send("Get ballmer'd", {files: ["https://cached.imagescaler.hbpl.co.uk/resize/scaleWidth/743/cached.offlinehbpl.hbpl.co.uk/news/OMC/Ballmer-20130823031812891.jpg"]})
                break;
            case 3:
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
        const pre = process.env.PREFIX
        const imgUrl = process.env.PFP
        const color = process.env.COLOR
        const exampleEmbed = new discord.MessageEmbed()
            .setColor(color)
            .setTitle('Help (music)')
            .setURL('https://github.com/jackdevey/byte/')
            .setThumbnail(imgUrl)
            .addFields(
                { name: pre+'play [term]', value: 'Play a song in your vc, or add it to a queue', inline: true},
                { name: pre+'playlist [url]', value: "Play a whole playlist cus you're too lazy to do it one by one", inline: true },
                { name: pre+'q, '+pre+'queue', value: 'List the songs in the queue', inline: true },
                { name: pre+'pause', value: 'Pause the song', inline: true },
                { name: pre+'resume', value: 'Resume the song', inline: true },
                { name: pre+'skip', value: 'Skip the song', inline: true },
                { name: pre+'loop', value: 'Loop the current song', inline: true },
                { name: pre+'shuffle', value: 'Shuffle the queue', inline: true },
                { name: pre+'clear', value: 'Stop playing the song & clear the queue', inline: true },
                { name: pre+'progress', value: 'Display a progress bar', inline: true }
            );

        channel.send(exampleEmbed);


        const exampleEmbed2 = new discord.MessageEmbed()
            .setColor(color)
            .setTitle('Help (general)')
            .setURL('https://github.com/jackdevey/byte/')
            .setThumbnail(imgUrl)
            .addFields(
                { name: pre+'ballmer', value: 'STEVE BALLMER', inline: true},
                { name: pre+'russell, '+pre+'hannah, '+pre+'hollie', value: "'insults' them", inline: true },
                { name: pre+'jack', value: 'Talks about the seggsyman', inline: true },
                { name: pre+'simon', value: 'Im not just a pretty face', inline: true },
                { name: pre+'bigd', value: 'BIG D', inline: true },
                { name: pre+'hello', value: 'Just says hello', inline: true },
                { name: pre+'del [value]', value: 'Tries to delete the messages, but it probably wont', inline: true },
            );

        channel.send(exampleEmbed2);
    }

    toxic(message, params, discord) {
        params.splice(0, 1);
        const threshold = 0.9;
        
        if (params != null) {
            message.reply("Analysing '" + params.join(" ") + "' ...")  
            // Load the model. Users optionally pass in a threshold and an array of
        // labels to include.
        toxicity.load(threshold).then(model => {
            const sentences = [params.join(" ")];
    
            model.classify(sentences).then(predictions => {
                // `predictions` is an array of objects, one for each prediction head,
                // that contains the raw probabilities for each input along with the
                // final prediction in `match` (either `true` or `false`).
                // If neither prediction exceeds the threshold, `match` is `null`.
    
                const exampleEmbed2 = new discord.MessageEmbed()
                .setColor('#0067f4')
                .setTitle('Analysed "'+params.join(" ")+'"')
                .addFields(
                    { name: 'Identity Attack', value: predictions[0].results[0].match, inline: true},
                    { name: 'Insult', value: predictions[1].results[0].match, inline: true },
                    { name: 'Obscene', value: predictions[2].results[0].match, inline: true },
                    { name: 'Severe Toxicity', value: predictions[3].results[0].match, inline: true },
                    { name: 'Sexual Explicit', value: predictions[4].results[0].match, inline: true },
                    { name: 'Threat', value: predictions[5].results[0].match, inline: true },
                    { name: 'Toxcicty', value: predictions[6].results[0].match, inline: true },
                );
    
                message.reply(exampleEmbed2)
                })
            })
        }

        
    }
}
