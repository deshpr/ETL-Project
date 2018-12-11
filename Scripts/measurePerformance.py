import requests
import json
import time



basicQueries = [
    "SELECT * FROM sales;",
]

filterQueries = [
    "SELECT * FROM sales WHERE Region = 'North America'",
    "SELECT * FROM sales WHERE ItemType = 'Office Supplies'",
    "SELECT * FROM sales WHERE saleschannel='Online'"
]

aggregateQueries = [
"SELECT region,country,itemtype,saleschannel,orderpriority,orderdate,orderid,shipdate,unitssold,unitprice,unitcost,totalrevenue,totalcost,totalprofit  ,sum(totalrevenue) from sales GROUP BY region HAVING saleschannel='Offline'",
"SELECT region,country,itemtype,saleschannel,orderpriority,orderdate,orderid,shipdate,unitssold,unitprice,unitcost,totalrevenue,totalcost,totalprofit  ,avg(unitssold) from sales GROUP BY region",
"SELECT region,country,itemtype,saleschannel,orderpriority,orderdate,orderid,shipdate,unitssold,unitprice,unitcost,totalrevenue,totalcost,totalprofit  ,avg(totalrevenue) from sales GROUP BY itemtype"
]

queryDictionary = {
    'basicQueries':basicQueries,
    'filterQueries':filterQueries,
    'aggregateQueries':aggregateQueries
}


queryName = ""
API_ENDPOINT = "https://lqe1p8ol0m.execute-api.us-west-2.amazonaws.com/Production/"

file = "inputData.json"
data = ""
with open(file) as f:
    data = json.load(f)
print(data)

headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}

for queryType, queries in queryDictionary.items():
    print("\n\n\n\nQuery type = {}".format(queryType))    
    for query in queries:    
        data["query"] = query
        print("query = {}".format(query))
        start = time.time()
        response = requests.post(url = API_ENDPOINT, data=json.dumps(data), headers=headers)
        end = time.time()
        resultcode = response.status_code
        if resultcode == 200:
            json_data = json.loads(response.text)
            print("count = {}".format(json_data["count"]))
            timetaken = float(int(json_data["endtime"])  - int(json_data["starttime"]))/1000000000
            print("start time = {}, end time  = {}, time taken = {} seconds".format(json_data["starttime"], json_data["endtime"], timetaken))
        print("Elapsed {}".format(end - start))
