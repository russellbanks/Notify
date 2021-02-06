module.exports = class Music{

    async request(message, params, player){
        // If there's already a song playing
        
        params.splice(0, 1);
          

        let isPlaying = player.isPlaying(message.guild.id);
        // If there's already a song playing
        if(isPlaying){
            // Add the song to the queue
            let song = await player.addToQueue(message.guild.id, params.join(' '));
            song = song.song;
            message.channel.send(`Song ${song.name} was added to the queue!`);
        } else {
            // Else, play the song
            let song = await player.play(message.member.voice.channel, params.join(' '));
            song = song.song;
            message.channel.send(`Started playing ${song.name}!`);
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
        }).join('\n')));
    }

    loop(message, player) {
        let toggle = player.toggleLoop(message.guild.id);
        if (toggle)
            message.channel.send('I will now repeat the current playing song.');
        else message.channel.send('I will not longer repeat the current playing song.');
    }

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

    async playlist(message, params, player) {
        params.splice(0, 1);
        let isPlaying = player.isPlaying(message.guild.id);
        // If MaxSongs is -1, will be infinite.
        let playlist = await player.playlist(message.guild.id, params.join(' '), message.member.voice.channel, 10, message.author.tag);

        // Determine the Song (only if the music was not playing previously)
        let song = playlist.song;
        // Get the Playlist
        playlist = playlist.playlist;

        // Send information about adding the Playlist to the Queue
        message.channel.send(`Added a Playlist to the queue with **${playlist.videoCount} songs**, that was **made by ${playlist.channel}**.`)

        // If there was no songs previously playing, send a message about playing one.
        if (!isPlaying) {

            message.channel.send(`Started playing ${song.name}!`);

            // Send a message, when Queue would be empty.
            song.queue.on('end', () => {
                message.channel.send('The queue is empty, please add new songs!');
            }).on('songChanged', (oldSong, newSong, skipped, repeatMode, repeatQueue) => {
                if (repeatMode) {
                    message.channel.send(`Playing ${newSong.name} again...`);
                } else if(repeatQueue) {
                    message.channel.send(`Playing **${newSong.name}...**\nAdded **${oldSong.name}** to the end of the queue (repeatQueue).`);
                } else {
                    message.channel.send(`Now playing ${newSong.name}...`);
                }
            }).on('songError', (errMessage, song) => {
                if(errMessage === 'VideoUnavailable')
                    message.channel.send(`Could not play **${song.name}** - The song was Unavailable, skipping...`);
                else message.channel.send(`Could not play ${song.name} - ${errMessage}.`);
            });
        }
    }
}
