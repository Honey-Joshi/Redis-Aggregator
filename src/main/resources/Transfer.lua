local logtable = {}

local function logit(msg)
    logtable[#logtable+1] = msg
end

local key1 = KEYS[1];
local val = (ARGV[1])
local key2 = tostring(ARGV[2])

local exists = tostring(redis.pcall('HGET', key1, key2))
if exists == "1" then
    return "Duplicate Message from Kafka"
end
redis.pcall('HSET',key1,key2,tostring(0))
local e = redis.pcall('HEXISTS',key1,key1)

if e == 1 then
    local currentJson = cjson.decode(redis.pcall('HGET',key1, key1))
    val = cjson.decode(val)

        local instantJsonVal = val.Instant
        local acqJsonVal = val.Acquiring
        local refJsonVal = val.Refund

        local instantJsonCurr = currentJson.Instant
        local acqJsonCurr = currentJson.Acquiring
        local refJsonCurr = currentJson.Refund


        if (instantJsonCurr ~= nil and instantJsonVal ~= nil)  then
            instantJsonCurr.total_amount = instantJsonCurr.total_amount+instantJsonVal.total_amount
            instantJsonCurr.total_count = instantJsonCurr.total_count+instantJsonVal.total_count
            instantJsonCurr.commision = instantJsonCurr.commision+instantJsonVal.commision
            instantJsonCurr.commision_tax = instantJsonCurr.commision_tax+instantJsonVal.commision_tax
            instantJsonCurr.pc_fees = instantJsonCurr.pc_fees+instantJsonVal.pc_fees
            instantJsonCurr.pc_tax = instantJsonCurr.pc_tax+instantJsonVal.pc_tax
            logit("Inside Instant Aggregation")
        end
        if (acqJsonCurr ~= nil and acqJsonVal ~= nil) then
            acqJsonCurr.total_amount = acqJsonCurr.total_amount+acqJsonVal.total_amount
            acqJsonCurr.total_count = acqJsonCurr.total_count+acqJsonVal.total_count
            acqJsonCurr.commision = acqJsonCurr.commision+acqJsonVal.commision
            acqJsonCurr.commision_tax = acqJsonCurr.commision_tax+acqJsonVal.commision_tax
            acqJsonCurr.pc_fees = acqJsonCurr.pc_fees+acqJsonVal.pc_fees
            acqJsonCurr.pc_tax = acqJsonCurr.pc_tax+acqJsonVal.pc_tax
            logit("Inside ACQ Aggregation")
        end

        if (refJsonCurr ~= nil and refJsonVal ~= nil)  then

            refJsonCurr.total_amount = refJsonCurr.total_amount+refJsonVal.total_amount
            refJsonCurr.total_count = refJsonCurr.total_count+refJsonVal.total_count
            refJsonCurr.commision = refJsonCurr.commision+refJsonVal.commision
            refJsonCurr.commision_tax = refJsonCurr.commision_tax+refJsonVal.commision_tax
            refJsonCurr.pc_fees = refJsonCurr.pc_fees+refJsonVal.pc_fees
            refJsonCurr.pc_tax = refJsonCurr.pc_tax+refJsonVal.pc_tax
            logit("Inside REFUND Aggregation")
        end

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