package db

import Config
import com.mongodb.client.model.Updates.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

object Connection {

    private val client = KMongo.createClient(Config.mongoDbUri).coroutine
    private var database = client.getDatabase("notify")
    private val guildPrefs = database.getCollection<GuildPrefs>("guildPrefs")

    suspend fun getGuildPrefs(guildId: String): GuildPrefs {
        val record = guildPrefs.findOne(GuildPrefs::guildId eq guildId)!!
        return GuildPrefs(record.guildId, record.channelId, record.join, record.switch, record.leave, record.stream, record.video)
    }

    suspend fun updateGuildPrefs(guildId: String, feature: String, toggle: Boolean) {
        guildPrefs.updateOne(GuildPrefs::guildId eq guildId, combine(set(feature, toggle), currentDate("lastModified")))
    }

}