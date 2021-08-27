module.exports = async (interaction) => {
   
    // Get the member who begun the
    // interaction.
    let member = interaction.member;

    // Check if member is admin and
    // let them know if not.
    if (!member.permissions.has("ADMINISTRATOR")) {
        await interaction.reply({ content: `You must have administrator permissions to run this command!`, ephemeral: true });
        return;
    }

    // Send a DM to the admin who
    // requested the setup.
    member.send('Hi');

    // Let the user know we will 
    // continue conversation in
    // their DMs.
    await interaction.reply({ content: `You are a server admin. I have sent you a DM!`, ephemeral: true });
}