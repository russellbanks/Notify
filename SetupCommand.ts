import { Command } from "./Command";

export class SetupCommand extends Command {

    name = "setup"
    description = "Begin setting up a server"

    async run(interaction, complete) {
        // Get the member who begun the
        // interaction.
        let member = interaction.member;

        // Check if member is admin and
        // let them know if not.
        if (!member.permissions.has("ADMINISTRATOR")) {
            await interaction.reply({ content: `You need admin` });
            console.log(`${member.user.tag} SENT SETUP COMMAND [FAILED]`);
            complete(false);
            return;
        }

        await interaction.reply({ content: `You are the admin` });
        console.log(`${member.user.tag} SENT SETUP COMMAND [SUCCESS]`);
        complete(true);
    } 

}