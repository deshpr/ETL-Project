import requests
import json
import time



query = "SELECT * FROM sales;"


API_ENDPOINT = "https://lqe1p8ol0m.execute-api.us-west-2.amazonaws.com/Production/"

file = "inputData_simple.json"
data = ""
with open(file) as f:
    data = json.load(f)
print(data)

stressCount = 25

for i in range(0,25):
    




