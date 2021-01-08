module.exports = class People{
    
    // Sends a pic of BALLMER 
    russell(channel){
        switch(Math.floor(Math.random() * 10)){
            case 0:
                channel.send("russell stinks")
                break;
            case 1:
                channel.send("russell is bad")
                break;
            case 2:
                channel.send("russell is not as cool as jack")
                break;
            case 3:
                channel.send("russell is stupid")
                break;
            case 4:
                channel.send("russell is stupid")
                break;
            default:
                channel.send("russell is a spaget coder")
                break;
        }
    }

    // Jack is cool
    jack(channel){
        channel.send("jack is the best. He is so so so seggsy")
    }
    
    // Hannah smeels
    hannah(channel){
        channel.send("hannah smeels")
    }

    // Protends to kick someone
    kick(channel, user){
        channel.send("Kicking " + user + " ...")
    }
}
