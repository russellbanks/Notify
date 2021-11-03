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