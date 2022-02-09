package db

import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo
import org.litote.kmongo.eq
import org.litote.kmongo.findOne

object Connection {

    private val client = KMongo.createClient(Config.mongoDbUri)
    private var database: MongoDatabase = client.getDatabase("notify")

    fun getGuildPrefs(guildId: String): GuildPrefs {
        val coll = database.getCollection("guildPrefs")
        val record = coll.findOne(GuildPrefs::guildId eq guildId)!!
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

}