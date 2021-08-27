// Import environment variables.
require('dotenv').config();

// Import ioredis as Redis
const Redis = require("ioredis");

// Connect to our Redis DB
const redis = new Redis({
    port: process.env.REDIS_PORT,
    host: process.env.REDIS_HOST,
    password: process.env.REDIS_PASSWORD
});

// When Redis DB is connected
redis.on("connect", function() {
    console.log("Connected to redis db");
});

// Get data from the db
module.exports.get = (key, after) => {
    return redis.get(key, function(err, res) {
        if (err) console.error(err);
        else after(res);
    });
}

// Set data in the db
module.exports.set = (key, value) => {
    redis.set(key, value);
}