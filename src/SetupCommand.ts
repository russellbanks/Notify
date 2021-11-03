/*
 * Notify
 * Copyright (c) 2021 BanDev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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