#!/bin/bash

# JSON object to pass to Lambda Function


#json={"\"bucketname\"":"\"test.bucket.562.firsttest\",\"query\"":"\"SELECT region,country,itemtype,saleschannel,orderpriority,orderdate,orderid,shipdate,unitssold,unitprice,unitcost,totalrevenue,totalcost,totalprofit  ,sum(totalrevenue) from sales GROUP BY region HAVING saleschannel='Offline'\""}
#json={"\"query\"":"\"SELECT region,country,itemtype,saleschannel,orderpriority,orderdate,orderid,shipdate,unitssold,unitprice,unitcost,totalrevenue,totalcost,totalprofit  ,sum(totalrevenue) from sales GROUP BY region HAVING saleschannel='Offline'\",\"bucketname\"":\"test.bucket.562.rah1\",\"param2\"":2,\"param3\"":3}

echo "Invoking Lambda function using API Gateway"  >> SequentialTestOutput.txt
start_time="$(date -u +%s.%N)"
echo "here"
echo "start time : $start_time" >> SequentialTestOutput.txt
echo $json
time output=`curl -s -H "Content-Type: application/json" -X POST -d @inputData.json https://lqe1p8ol0m.execute-api.us-west-2.amazonaws.com/Production/`
echo $output

