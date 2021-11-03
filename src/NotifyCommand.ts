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

export class NotifyCommand extends Command {

    name = "notify"
    description = "Let your friends know you're in a VC!"

    async run(interaction, complete: (boolean) => any) {
        // Get the member who begun the
        // interaction.
        let member = interaction.member;

        // If member is not in any VC.
        if(!member.voice.channel) {
            await interaction.reply({ content: `You must be in a VC to run this command!`, ephemeral: true });
            console.log(`${member.user.tag} SENT NOTIFY COMMAND [FAILED]`);
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
        console.log(`${member.user.tag} SENT NOTIFY COMMAND [SUCCESS]`);
    } 

}