module.exports = async (interaction) => {

    // Get the member who begun the
    // interaction.
    let member = interaction.member;

    // If member is not in any VC.
    if(!member.voice.channel) {
        await interaction.reply({ content: `You must be in a VC to run this command!`, ephemeral: true });
        console.log(`${member.id} SENT NOTIFY COMMAND [FAILED]`);
        return;
    }

    // Make an empty members
    // list for adding things into
    // later.
    var members: String[] = []

    // Get the VC that the member 
    // is in.
    let vc = member.voice.channel;

    // Iterate through VC members
    // and remove caller and bots.
    vc.members.forEach(it => {
        if(it != member && !it.user.bot) members.push(it.displayName)
    });

    // Decide on what should be
    // said after the caller.
    var after: String;
    if(members.length == 1) {
        // Just one other member
        after = `with ${members[0]}`
    } else if(members.length == 0) {
        // No other members :(
        after = ``;
    } else {
        // A few members
        let final = members[members.length - 1];
        members.pop();
        after = `with ${members.join(`, `)} and ${final}`;
    }

    // Reply to the interaction.
    await interaction.reply(`@everyone, ${member.displayName} is in **${vc.name}** ${after}`);

    // Log info in the console
    console.log(`${member.id} SENT NOTIFY COMMAND [SUCCESS]`);
}