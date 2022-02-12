package data

import Config
import com.mongodb.client.model.Updates.*
import dev.kord.cache.api.data.description
import dev.kord.cache.api.put
import dev.kord.cache.api.query
import dev.kord.cache.map.MapDataCache
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

        suspend fun get(guildId: String): GuildPrefs {
            // Get the record from cache
            val cachedRecord = cache.query<GuildPrefs> { GuildPrefs::guildId eq guildId }.singleOrNull()

            // If record was not in the cache, get it
            // from the db instead
            var record: GuildPrefs? = null
            if (cachedRecord == null) {
                record = guildPrefs.findOne(GuildPrefs::guildId eq guildId)!!
                cache.put(record) // Add to cache for next time
            } else record = guildPrefs.findOne(GuildPrefs::guildId eq guildId)

            // Is the record still null?
            if (record == null) throw Error("guildPrefs was not found for this guild")

            // Return the record
            return GuildPrefs(
                record.guildId, record.channelId, record.join, record.switch,
                record.leave, record.stream, record.video
            )
        }

        suspend fun update(guildId: String, feature: String, toggle: Boolean) {
            // Update changes in the database
            val criteria = GuildPrefs::guildId eq guildId
            val changes = combine(set(feature, toggle), currentDate("lastModified"))

            // See if the changes where acknowledged, if
            // not throw error to avoid inconsistencies
            // in data.
            if(!guildPrefs.updateOne(criteria, changes).wasAcknowledged()) throw Error("DB update not ack")

            // Update this for the cache
            val cachedRecord = cache.query<GuildPrefs> { GuildPrefs::guildId eq guildId }

            // Get record from the database
            val dbRecord = guildPrefs.findOne(GuildPrefs::guildId eq guildId)!!

            // Update or add to the cache
            if(cachedRecord.singleOrNull() == null) {
                cache.put(dbRecord)
            } else {
                cachedRecord.update { dbRecord }
            }
        }

    }

}