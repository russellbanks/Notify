import { Command } from "./Command";

export class SetupCommand extends Command {

    name = "setup"
    description = "CHANGEME"

    async run(interaction, complete: (boolean) => any) {
        // Get the member who begun the
        // interaction.
        let member = interaction.member;

        // Check if member is admin and
        // let them know if not.
        if (!member.permissions.has("ADMINISTRATOR")) {
            await interaction.reply({ content: `You need admin` });
            console.log(`${member.user.tag} SENT SETUP COMMAND [FAILED]`);
            return;
        }

        const embed = new this.discord.MessageEmbed()
            .setColor('#0067f4')
            .setTitle(`Responding`)
            .setDescription('Choose events Notify should respond to')
            .addField(`joining`, `:white_check_mark: yes`, true)
            .addField(`leaving`, `:x: no`, true)
            .addField(`switching`, `:white_check_mark: yes`, true)
            .addField(`streaming`, `:x: no`, true)
            .setTimestamp();

        await interaction.reply({ content: `You are the admin` });
        member.send({ embeds: [embed] });
        console.log(`${member.user.tag} SENT SETUP COMMAND [SUCCESS]`);
    } 

}