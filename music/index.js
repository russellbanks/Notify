require('dotenv').config();


module.exports = class Music{

    async request(message, params, player, discord, server, prefix){
        // If there's already a song playing

        // Remove the command
        params = params.replace(prefix+"play ", "")
        console.log(params)
        if(params.startsWith("https://music.youtube")){
            console.log("yes")
            params = params.replace("music.", "")
        } 
        
        let isPlaying = player.isPlaying(message.guild.id);
        // If there's already a song playing
        if(isPlaying){
            // Add the song to the queue
            let song = await player.addToQueue(message.guild.id, params);
            song = song.song;
            message.channel.send(`Song ${song.name} was added to the queue`);
            this.showEmbed(song, message.author, discord, message.channel, server)
        } else {
            // Else, play the song
            let song = await player.play(message.member.voice.channel, params);
            song = song.song;
            message.channel.send(`Started playing ${song.name}`);
            this.showEmbed(song, message.author, discord, message.channel, server)
        }
    }

    async skip(message, player) {
        let song = await player.skip(message.guild.id);
        message.channel.send(`${song.name} was skipped!`);
    }

    clear(message, player) {
        player.stop(message.guild.id);
        message.channel.send('Music stopped, the Queue was cleared!');
    }

    shuffle(message, player) {
        player.shuffle(message.guild.id);
        message.channel.send('Server Queue was shuffled.');
    }

    async queue(message, player) {
        let queue = await player.getQueue(message.guild.id);
        message.channel.send('Queue:\n'+(queue.songs.map((song, i) => {
            return `${i === 0 ? 'Now Playing' : `#${i+1}`} - ${song.name} | ${song.author}`
        }).join('\n')), { split: true });
    }

    loop(message, player) {
        let toggle = player.toggleLoop(message.guild.id);
        if (toggle)
            message.channel.send('I will now repeat the current playing song.');
        else message.channel.send('I will not longer repeat the current playing song.');
    }

    // THIS CODE WAS WRITTERN BY THE GREAT RUSSELL BANKS
    // leave(message) {
    //     message.guild.me.voice.channel.leave();
    // }
    // IT IS NOT IN USE ANYMORE, HE DID IT WRONG

    async pause(message, player) {
        let song = await player.pause(message.guild.id);
        message.channel.send(`${song.name} was paused!`);
    }

    async resume(message, player) {
        let song = await player.resume(message.guild.id);
        message.channel.send(`${song.name} was paused!`);
    }

    progress(message, player) {
        let progressBar = player.createProgressBar(message.guild.id, 20);
        message.channel.send(progressBar);
    }

    async playlist(message, params, player, discord, server, pefix) {

        // Remove the command
        params = params.replace(prefix+"playlist ", "")
        console.log(params)
        if(params.startsWith("https://music.youtube")){
            console.log("yes")
            params = params.replace("music.", "")
        } 

        let isPlaying = player.isPlaying(message.guild.id);
        // If MaxSongs is -1, will be infinite.
        let playlist = await player.playlist(message.guild.id, params, message.member.voice.channel, -1, message.author.tag);

        // Determine the Song (only if the music was not playing previously)
        let song = playlist.song;
        // Get the Playlist
        playlist = playlist.playlist;

        // Send information about adding the Playlist to the Queue
        message.channel.send(`Added ${playlist.name} to the queue`)
        this.showEmbedPL(playlist, message.author, discord, message.channel, server)

        // If there was no songs previously playing, send a message about playing one.
        if (!isPlaying) {
            message.channel.send(`Started playing ${song.name}`);
            this.showEmbed(song, message.author, discord, message.channel, server)
        }
    }

    showEmbed(song, user, discord, channel, server) {
        const name = process.env.NAME
        const pfpUrl = process.env.PFP
        const color = process.env.COLOR
        const embed = new discord.MessageEmbed()
            .setColor(color)
            .setTitle(song.name)
            .setURL(song.url)
            .setAuthor(user.tag, user.displayAvatarURL())
            .setDescription(song.author)
            .setThumbnail(song.thumbnail)
            .addField('Duration', song.duration, true)
            .setFooter('Playing on '+name+', ' + server, pfpUrl);
        channel.send(embed);
    }

    
    showEmbedPL(playlist, user, discord, channel, server) {
        const color = process.env.COLOR
        const name = process.env.NAME
        const pfpUrl = process.env.PFP
        const embed = new discord.MessageEmbed()
            .setColor(color)
            .setTitle(playlist.name)
            .setURL(playlist.url)
            .setAuthor(user.tag, user.displayAvatarURL())
            .setDescription(playlist.author)
            .addField('Video Count', playlist.videoCount, true)
            .setFooter('Playing on '+name+', ' + server, pfpUrl);
        channel.send(embed);
    }
}
