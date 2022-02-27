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

import Config
import com.mongodb.client.model.Updates.*
import dev.kord.cache.api.data.description
import dev.kord.cache.api.put
import dev.kord.cache.api.query
import dev.kord.cache.map.MapDataCache
import dev.kord.common.entity.Snowflake
import extensions.voicestateupdate.Action
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

object Datastore {

    private val client = KMongo.createClient(Config.mongoDbUri).coroutine
    private var database = client.getDatabase("notify")
    private val guildPrefs = database.getCollection<GuildPrefs>("guildPrefs")

    object GuildPrefsCollection {

        private val cache = MapDataCache()
        private val description = description(GuildPrefs::guildId)

        suspend fun setupCache() = cache.register(description)

        suspend fun get(guildId: Snowflake): GuildPrefs {
            val cachedRecord = cache.query<GuildPrefs> { GuildPrefs::guildId eq guildId.toString() }.singleOrNull()

            val record: GuildPrefs? = if (cachedRecord == null) {
                guildPrefs.findOne(GuildPrefs::guildId eq guildId.toString()).also { cache.put(it!!) }
            } else {
                guildPrefs.findOne(GuildPrefs::guildId eq guildId.toString())
            }

            if (record == null) throw Error("Guild preferences were not found for this guild")

            return GuildPrefs(
                record.guildId, record.channelId, record.join, record.switch,
                record.leave, record.stream, record.video
            )
        }

        suspend fun update(guildId: Snowflake, feature: Action, toggle: Boolean) {
            val criteria = GuildPrefs::guildId eq guildId.toString()
            val changes = combine(set(feature.name.lowercase(), toggle), currentDate("lastModified"))

            if (!guildPrefs.updateOne(criteria, changes).wasAcknowledged()) throw Error("DB update not ack")

            val cachedRecord = cache.query<GuildPrefs> { GuildPrefs::guildId eq guildId.toString() }

            val dbRecord = guildPrefs.findOne(GuildPrefs::guildId eq guildId.toString())!!

            if (cachedRecord.singleOrNull() == null) cache.put(dbRecord)
            else cachedRecord.update { dbRecord }
        }

        suspend fun isNewGuild(guildId: Snowflake): Boolean {
            return guildPrefs.findOne(GuildPrefs::guildId eq guildId.toString()) == null
        }

        suspend fun createGuild(guildId: Snowflake) {
            guildPrefs.insertOne(GuildPrefs(guildId.toString(), "null",
                join = true,
                switch = true,
                leave = true,
                stream = true,
                video = true
            ))
        }

        suspend fun deleteGuild(guildId: Snowflake) {
            guildPrefs.deleteOne(GuildPrefs::guildId eq guildId.toString())
        }

    }

}