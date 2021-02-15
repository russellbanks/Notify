//Dotenv library
require('dotenv').config();

//Import request library
const request = require('request');

//Get setting variables
const color = process.env.COLOR;
const nasakey = process.env.NASAKEY;

//The class that will be accessed by index.js
module.exports = class Space{

    //Send a rover picture
    rover(message, discord, args) {
        var rover = args[0];
        var date = args[1];
        //Send a text message first
        message.channel.send("Requesting image data for **" + rover + "** on **" + date + "** from **NASA**");
        //Make a request using the nasa api
        request('https://api.nasa.gov/mars-photos/api/v1/rovers/' + rover + '/photos?api_key=' + nasakey + '&earth_date=' + date, { json: true }, (err, res, body) => {
            if (err) { return console.log(err); }
            try {
                var photo = body.photos[0];
                //Create a nice embed to send back to tell the user the commands it knows
                const embed = new discord.MessageEmbed()
                    .setColor(color)
                    .setTitle(photo.rover.name + ' took this picture on Mars:')
                    .setURL('https://mars.nasa.gov/mer/')
                    .setImage(photo.img_src)
                    .addFields(
                        { name: 'Rover Name', value: photo.rover.name, inline: true },
                        { name: 'Earth Date', value: photo.earth_date, inline: true },
                        { name: 'Sol Date', value: photo.sol, inline: true },
                        { name: 'Camera Name', value: photo.camera.name, inline: true }
                    )
                    .setFooter('Data provided by NASA', 'https://logo.clearbit.com/nasa.gov');
                //Send the command to the channel where the message was recieved
                message.channel.send(embed);
            }catch{
                const embed = new discord.MessageEmbed()
                    .setColor(color)
                    .setTitle('Error')
                    .setURL('https://mars.nasa.gov/mer/')
                    
                if(date >= '2010-3-22' && rover == 'spirit') {
                    embed.setDescription('Spirit stopped communicating before this date due to getting stuck in a sand trap');
                }else if(date >= '2018-6-10' && rover == 'opportunity') {
                    embed.setDescription('Opportunity stopped communicating before this date due to severe dust storms');
                }else if(rover == "perseverance") {
                    embed.setDescription('The perseverance rover has not yet touched down on mars');
                }else if(rover != 'spirit' || rover != 'opportunity' || rover != 'curiosity') {
                    embed.setDescription('Unkown rover **' + rover + '**');
                }else {
                    embed.setDescription('Unknown error occurred');
                }
                //Send the command to the channel where the message was recieved
                message.channel.send(embed);
            }
        });
    }
}
