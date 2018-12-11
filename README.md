# Servive 3




Expected Inputs: A json defining the query.


A json  for example:

{
	"bucketname":"test.bucket.562.rah1",
	"databasefilename":"salespipeline.db",
	"clientid":"12345",
	"outputbucketname":"test.bucket.562.rah1",
	"outputfilename":"myoutput",
	"aggregateInfo":[
		{
			"type":"avg",
			"columnname":"totalrevenue"
		}
	],
	"groupbycolumns":[
		"itemtype"
		]

}


bucketname: the name of the bucket from where to get the  .db file
databasefilename: the name of the .db file, if not specified then salespipeline.db is used by default
clientid: the id of the client who is making the request. This allows us to differentiate between different clients when accessing the databasefilename
aggregateInfo: The columns for which we wanna do aggregation:
    For example, in this example:
    "aggregateInfo":[
            {
                "type":"avg",
                "columnname":"totalrevenue"
            }
        ]
    will result in the following SQL Query: 
    select col1, col2,....,col10, avg(totalrevenue) from sales;
groupbycolumns: The columns for which we wanna group the results. For example:

	"groupbycolumns":[
		"itemtype"
		]
    Will result in the following SQL Query:
    SELECT  col1, col2,...col_all from sales GROUP BY itemtype
	"groupbycolumns":[
		"itemtype",
        "saleschannel"
		]
    Will result in the following SQL Query:
    SELECT  col1, col2,...col_all from sales GROUP BY itemtype, saleschannel

Also if we want we can directly indicate the SQL Query to run, using the 'query' parameter, like this:

{
	"bucketname":"test.bucket.562.rah1",
	"databasefilename":"salespipeline.db",
	"clientid":"12345",
	"query":"SELECT * FROM sales;",
	"outputbucketname":"test.bucket.562.rah1",
	"outputfilename":"myoutput",
	"filterinfo":[
			{
				"columnname":"Sales Channel",
				"columnvalue":"Offline"
			}
		],
	"aggregateInfo":[
		{
			"type":"sum",
			"columnname":"totalrevenue"
		}
	],
	"groupbycolumns":[
		"region"
		]

}

If you send this JSON over, then 




Optional parameters:

    outputbucketname: Optionalthe name of the bucket to write the output to. 
