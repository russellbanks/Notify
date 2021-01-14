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
                channel.send("Get ballmer'd", {files: ["https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fstatic1.businessinsider.com%2Fimage%2F596905fea47cb547008b4732-2400%2Fgettyimages-154981580.jpg&f=1&nofb=1"]})
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
}
