module.exports = async (interaction) => {
   
    // Get the member who begun the
    // interaction.
    let member = interaction.member;

    // Check if member is admin and
    // let them know if not.
    if (!member.permissions.has("ADMINISTRATOR")) {
        await interaction.reply({ content: `You need admin` });
        return;
    }

    await interaction.reply({ content: `You are the admin` });
     
}