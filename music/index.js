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
}
