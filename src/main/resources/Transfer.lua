local key1 = KEYS[1];
local val = (ARGV[1])
local key3 = tostring(ARGV[3])
local key4 = tostring(ARGV[4])
local logtable = {}
local key2 = tostring(ARGV[2])

local tot=0
local function logit(msg)
    logtable[#logtable+1] = msg
end

local function isInteger(str)

    return not (str == "" or str:find("%D"))  -- str:match("%D") also works

end


local function sumIt(redisJson,newJson)
    for i in pairs(redisJson) do
        --logit("Type of value is -> "..tostring(isNumeric(redisJson[i])).." and value is ->" ..redisJson[i])
        if type(redisJson[i]) == "table" then
            sumIt(redisJson[i],newJson[i])
        end
        if i == "amount" then
            tot = tot+newJson["amount"];
            redisJson["amount"] = redisJson["amount"]+ newJson["amount"];
        end
        if i == "count" then
            redisJson["count"] = redisJson["count"]+ 1;
        end
        end

    return redisJson;
end

local exists = tostring(redis.pcall('HGET', key1, key3))
if exists == "1" then
    return "Duplicate Message from Kafka"
end
redis.pcall('HSET',key1,key3,tostring(0))
local e = redis.pcall('HEXISTS',key1,key2)

if e == 1 then
    local currentJson = cjson.decode(redis.pcall('HGET',key1, key2))
    val = cjson.decode(val)

        currentJson = sumIt(currentJson,val);
        redis.pcall('HSET',key1,key2,cjson.encode(currentJson))
        local t = cjson.decode(redis.pcall('HGET',key1,"Total_Aggregate"))
        t.totalAmount = tot
        redis.pcall('HSET',key1,"Total_Aggregate",cjson.encode(t))
        redis.pcall('HSET',key1,key3,tostring(1))
        --redis.log( redis.LOG_WARNING,"Aggregation done on Redis")
        logit("Aggregation done on Redis")
        return  logtable



else
    --redis.log( redis.LOG_WARNING,"ELSE BLOCK -> Fresh JSON pushed to Redis")
    logit("Fresh JSON pushed to Redis")
    redis.pcall('HSET',key1,key2,val)
    redis.pcall('HSET',key1,"Total_Aggregate",key4)
    redis.pcall('HSET',key1,key3,tostring(1))
    return  logtable
end