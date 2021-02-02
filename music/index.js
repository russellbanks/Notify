module.exports = class Music{

    async request(message, params, player){
        // If there's already a song playing
            // Else, play the song
            let song1 = await player.play(message.member.voice.channel, params.join(" "));
            var song2 = song1.song;
            message.channel.send(`Started playing ${song2.name}!`);

    }

}
