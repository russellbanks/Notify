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

module.exports = async (interaction) => {
   
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

    await interaction.reply({ content: `You are the admin` });
    console.log(`${member.user.tag} SENT SETUP COMMAND [SUCCESS]`);
}