local key1 = KEYS[1];
local val = (ARGV[1])
local key2 = tostring(ARGV[2])
local logtable = {}

local function logit(msg)
    logtable[#logtable+1] = msg
end

local function isNumeric(value)
    --logit("comparing " .. value .."with " ..tostring(tonumber(value)))
    if value == tostring(tonumber(value)) then
        --logit("inside true column with value -> " .. value)
        return true
    end
end


local function sumIt(redisJson,newJson)
    for i in pairs(redisJson) do
        --logit("Type of value is -> "..tostring(isNumeric(redisJson[i])).." and value is ->" ..redisJson[i])
        if isNumeric(redisJson[i]) then
            redisJson[i] = redisJson[i]+ newJson[i];
            logit("The Json aggregated in the loop successfully")
        end
    end
    return redisJson;
end

local exists = tostring(redis.pcall('HGET', key1, key2))
if exists == "1" then
    return "Duplicate Message from Kafka"
end
redis.pcall('HSET',key1,key2,tostring(0))
local e = redis.pcall('HEXISTS',key1,key1)

if e == 1 then
    local currentJson = cjson.decode(redis.pcall('HGET',key1, key1))
    val = cjson.decode(val)

    logit("line before function call "..tostring(val))
        currentJson = sumIt(currentJson,val);
        redis.pcall('HSET',key1,key1,cjson.encode(currentJson))
        redis.pcall('HSET',key1,key2,tostring(1))
        --redis.log( redis.LOG_WARNING,"Aggregation done on Redis")
        logit("Aggregation done on Redis")
        return  logtable



else
    --redis.log( redis.LOG_WARNING,"ELSE BLOCK -> Fresh JSON pushed to Redis")
    logit("Fresh JSON pushed to Redis")
    redis.pcall('HSET',key1,key1,val)
    redis.pcall('HSET',key1,key2,tostring(1))
    return  logtable
end