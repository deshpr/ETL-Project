import requests
import json
import time


def http_post(URL, jsonToSend, headers):
    response = requests.post(url = API_ENDPOINT_SERVICE_ONE, data=json.dumps(jsonToSend), headers=headers)
    return response


API_ENDPOINT_SERVICE_ONE = "https://ckjry51uc0.execute-api.us-west-2.amazonaws.com/Final/"
API_ENDPOINT_SERVICE_TWO = " https://250z3s9kcd.execute-api.us-west-2.amazonaws.com/Final/"
API_ENDPOINT_SERVICE_THREE = "https://lqe1p8ol0m.execute-api.us-west-2.amazonaws.com/Production/"

print("\n\n\n Calling Service 1")
jsonToSend = {"bucketname":"final.bucket.562.team6", "filename":"100 Sales Records.csv"}
headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}
response = http_post(API_ENDPOINT_SERVICE_ONE, jsonToSend, headers)
resultcode  = response.status_code
if resultcode != 200:
    print("Service 1 has failed")
    exit(-1)
else:
    json_data = json.loads(response.text)
    print(json_data)
    print("\n\n\n Calling Service 2")
    API_ENDPOINT_SERVICE_ONE = "https://ckjry51uc0.execute-api.us-west-2.amazonaws.com/Final/"
    jsonToSend = {"bucketname":"final.bucket.562.team6", "filename":"100 Sales Records.csv"}
    response = http_post(API_ENDPOINT_SERVICE_TWO, jsonToSend, headers)
    resultcode = response.status_code
    if resultcode != 200:
        print("Service 2 has failed")
        exit(-1)
    else:
        json_data = json.loads(response.text)    
        print(json_data)        
        print("\n\n\n Calling Service 3")
        API_ENDPOINT_SERVICE_ONE = "https://lqe1p8ol0m.execute-api.us-west-2.amazonaws.com/Production/"
        jsonToSend = {"bucketname":"final.bucket.562.team6", "filename":"salespipeline.db", "query":"SELECT * FROM sales;"}
        response = http_post(API_ENDPOINT_SERVICE_THREE, jsonToSend, headers)
        resultcode = response.status_code
        if resultcode == 200:
            json_data = json.loads(response.text)    
            print(json_data)
        else:
            print("Service 3 has failed")
            exit(-1)
