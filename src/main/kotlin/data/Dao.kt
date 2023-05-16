/**

Notify
Copyright (C) 2023 Russell Banks

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

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.russellbanks.Database
import com.russellbanks.data.GuildPrefs
import dev.kord.cache.api.data.description
import dev.kord.cache.api.put
import dev.kord.cache.api.query
import dev.kord.cache.map.MapDataCache
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.GuildBehavior
import dev.kord.core.behavior.channel.ChannelBehavior
import extensions.voicestateupdate.Action

object Dao {
    private val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:notify.db").apply {
        if (!getConnection().metaData.getTables(null, null, "guildPrefs", null).next()) {
            Database.Schema.create(this)
        }
    }
    private val database = Database(driver)
    private val queries = database.guildInfoQueries

    private val cache = MapDataCache()
    private val description = description(GuildPrefs::guildId)

    suspend fun setupCache() = cache.register(description)

    suspend fun get(guild: GuildBehavior): GuildPrefs = get(guild.id)

    private suspend fun get(guildId: Snowflake): GuildPrefs {
        val cachedRecord = cache.query<GuildPrefs> { GuildPrefs::guildId eq guildId.toString() }.singleOrNull()
        return cachedRecord ?: queries.getGuild(guildId.toString()).executeAsOne().also { cache.put(it) }
    }

    fun isNewGuild(guild: GuildBehavior): Boolean = !queries.isNewGuild(guild.id.toString()).executeAsOne()

    fun createGuild(guild: GuildBehavior) = queries.createGuild(guild.id.toString())

    fun deleteGuild(guild: GuildBehavior) = queries.deleteGuild(guild.id.toString())

    fun updateChannel(guild: GuildBehavior, channel: ChannelBehavior?) {
        queries.updateChannel(channel?.id.toString(), guild.id.toString())
    }

    suspend fun update(guild: GuildBehavior, action: Action, toggle: Boolean) {
        val guildId = guild.id.toString()
        when (action) {
            Action.JOIN -> queries.updateJoin(join = toggle, guildId)
            Action.SWITCH -> queries.updateSwitch(switch = toggle, guildId)
            Action.LEAVE -> queries.updateLeave(leave = toggle, guildId)
            Action.STREAM -> queries.updateStream(stream = toggle, guildId)
            Action.VIDEO -> queries.updateVideo(video = toggle, guildId)
            else -> throw Error("Invalid feature: $action")
        }
        updateCache(guild)
    }

    private suspend fun updateCache(guild: GuildBehavior) {
        val cachedRecord = cache.query<GuildPrefs> { GuildPrefs::guildId eq guild.id.toString() }
        val dbRecord = queries.getGuild(guild.id.toString()).executeAsOne()
        if (cachedRecord.singleOrNull() == null) {
            cache.put(dbRecord)
        } else {
            cachedRecord.update { dbRecord }
        }
    }
}
