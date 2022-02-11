package db

import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Updates.*
import org.litote.kmongo.KMongo
import org.litote.kmongo.MongoOperator
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.updateOne

object Connection {

    private val client = KMongo.createClient(Config.mongoDbUri)
    private var database: MongoDatabase = client.getDatabase("notify")
    private val guildPrefs = database.getCollection("guildPrefs")

    fun getGuildPrefs(guildId: String): GuildPrefs {
        val record = guildPrefs.findOne(GuildPrefs::guildId eq guildId)!!
        return GuildPrefs(
            record.getString("guildId"),
            record.getString("channelId"),
            record.getBoolean("join"),
            record.getBoolean("switch"),
            record.getBoolean("leave"),
            record.getBoolean("stream"),
            record.getBoolean("video")
        )
    }

    fun updateGuildPrefs(guildId: String, feature: String, toggle: Boolean) {
        guildPrefs.updateOne(GuildPrefs::guildId eq guildId, combine(set(feature, toggle), currentDate("lastModified")))
    }

}