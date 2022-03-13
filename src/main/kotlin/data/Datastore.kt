/**

Notify
Copyright (C) 2022  BanDev

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.

 */

package data

import EnvironmentVariables
import com.mongodb.client.model.Updates.*
import dev.kord.cache.api.data.description
import dev.kord.cache.api.put
import dev.kord.cache.api.query
import dev.kord.cache.map.MapDataCache
import dev.kord.core.entity.Guild
import dev.kord.core.entity.channel.Channel
import extensions.voicestateupdate.Action
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

object Datastore {

    private val client = KMongo.createClient(EnvironmentVariables.mongoDbUri).coroutine
    private var database = client.getDatabase("notify")
    private val guildPrefs = database.getCollection<GuildPrefs>("guildPrefs")

    object GuildPrefsCollection {

        private val cache = MapDataCache()
        private val description = description(GuildPrefs::guildId)

        suspend fun setupCache() = cache.register(description)

        suspend fun get(guild: Guild): GuildPrefs {
            val cachedRecord = cache.query<GuildPrefs> { GuildPrefs::guildId eq guild.id.toString() }.singleOrNull()

            val record: GuildPrefs? = if (cachedRecord == null) {
                guildPrefs.findOne(GuildPrefs::guildId eq guild.id.toString()).also { cache.put(it!!) }
            } else {
                guildPrefs.findOne(GuildPrefs::guildId eq guild.id.toString())
            }

            if (record == null) throw Error("Guild preferences were not found for this guild")

            return GuildPrefs(
                record.guildId, record.channelId, record.join, record.switch,
                record.leave, record.stream, record.video
            )
        }

        suspend fun update(guild: Guild, feature: Action, toggle: Boolean) {
            val criteria = GuildPrefs::guildId eq guild.id.toString()
            val changes = combine(set(feature.name.lowercase(), toggle), currentDate("lastModified"))
            if (!guildPrefs.updateOne(criteria, changes).wasAcknowledged()) throw Error("DB update not ack")
            updateCache(guild)
        }

        suspend fun isNewGuild(guild: Guild): Boolean {
            return guildPrefs.findOne(GuildPrefs::guildId eq guild.id.toString()) == null
        }

        suspend fun createGuild(guild: Guild) {
            guildPrefs.insertOne(GuildPrefs(guild.id.toString(), "null",
                join = true,
                switch = true,
                leave = true,
                stream = true,
                video = true
            ))
        }

        suspend fun deleteGuild(guild: Guild) {
            guildPrefs.deleteOne(GuildPrefs::guildId eq guild.id.toString())
        }

        suspend fun updateChannel(guild: Guild, channel: Channel) {
            val criteria = GuildPrefs::guildId eq guild.id.toString()
            val changes = combine(set("channelId", channel.id.toString()), currentDate("lastModified"))
            if (!guildPrefs.updateOne(criteria, changes).wasAcknowledged()) throw Error("DB update not ack")
            updateCache(guild)
        }

        private suspend fun updateCache(guild: Guild) {
            val cachedRecord = cache.query<GuildPrefs> { GuildPrefs::guildId eq guild.id.toString() }
            val dbRecord = guildPrefs.findOne(GuildPrefs::guildId eq guild.id.toString())!!
            if (cachedRecord.singleOrNull() == null) cache.put(dbRecord)
            else cachedRecord.update { dbRecord }
        }

    }

}