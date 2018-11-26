/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lambda;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context; 
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import faasinspector.register;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * uwt.lambda_test::handleRequest
 * @author wlloyd
 */
public class Service3 implements RequestHandler<Request, Response>
{
    static String CONTAINER_ID = "/tmp/container-id";
    static Charset CHARSET = Charset.forName("US-ASCII");
    
     // Initialize the Log4j logger.
    static LambdaLogger logger;
    
    static String sqlDatabaseFileName = "salespipeline.db";
    
    public static boolean setCurrentDirectory(String directory_name)
    {
        boolean result = false;  // Boolean indicating whether directory was set
        File    directory;       // Desired current working directory

        directory = new File(directory_name).getAbsoluteFile();
        if (directory.exists() || directory.mkdirs())
        {
            result = (System.setProperty("user.dir", directory.getAbsolutePath()) != null);
        }

        return result;
    }
        
     public static void createFile(String directoryName, String filename,S3Object object){
       try{
           InputStream reader = new BufferedInputStream(object.getObjectContent());
            File file = new File(directoryName, filename);      
            OutputStream writer = new BufferedOutputStream(new FileOutputStream(file));

            int read = -1;

            while ( ( read = reader.read() ) != -1 ) {
                writer.write(read);
            }

            writer.flush();
            writer.close();
            reader.close();
          
        logger.log("File after writing has size = " + file.length());
        logger.log("File path = " + file.getAbsolutePath());
	}catch(Exception ex){
            logger.log(ex.toString());
        }
    }
    
    private  void DownloadSQLiteDatabase(String bucketName, String sourceKey, String fileName, String directoryName)
    {
        
        setCurrentDirectory("/tmp");
            
        logger.log("Download the sqlite database");
        
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();         
        
        //get object file using source bucket and srcKey name
        
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, sourceKey));
        //get content of the file        
        logger.log("Now create the file after downloading..");
       createFile(directoryName, "salespipeline.db", s3Object);
       logger.log("Created the file successfully");
    }
    
    private boolean checkIfFileExists(String directory, String fileWithDatabase){
        File f = new File(directory, fileWithDatabase);
        if(f.exists()){
            logger.log("File size = " + f.length());
        }
        return f.exists();
    }
    
    private boolean deleteDatabase(String directoryName, String fileWithDatabase){
        File f = new File(directoryName, fileWithDatabase);
        return f.delete();
    }
    
     public static int getCountOfRecords(String sqliteDatabaseFileName){
	int count = 0;
	try{
	    Connection con = DriverManager.getConnection("jdbc:sqlite:" + sqliteDatabaseFileName);
	    PreparedStatement ps1 = con.prepareStatement("select count(*) as count from sales;");
	    ResultSet rs1 = ps1.executeQuery();
	    count = rs1.getInt("count");	
	}
	catch(Exception e){
		e.printStackTrace();
                logger.log(e.toString());
	}
	return count;
    }
    
    public static String[] Columns = {
        "region",
        "country",
        "itemtype",
        "saleschannel",
        "orderpriority",
        "orderdate",
        "orderid",
        "shipdate",
        "unitssold",
        "unitprice",
        "unitcost",
        "totalrevenue",
        "totalcost",
        "totalprofit"            
    };
     
    public double executeAggregateQuery(String query){
        
        double result = 0;
	try{
	    Connection con = DriverManager.getConnection("jdbc:sqlite:" + sqlDatabaseFileName);
	    PreparedStatement ps1 = con.prepareStatement(query);
	    ResultSet ps = ps1.executeQuery();
            logger.log("Execute the results");
            while (ps.next())
            {
                logger.log("value = " + ps.getDouble(1));
                result = ps.getDouble(1);
                logger.log("Read the value = " + result);
            }            
            ps.close();
            con.close();
	}
	catch(Exception e){
		e.printStackTrace();
                logger.log(e.toString());
	}
        return result;
    }
     
    public double HandleAggregateQuery(String queryType, String column, String parameters)
    {
        queryType = queryType.toLowerCase();
        String query = "";
       
        logger.log("Handle aggregate query, col " + column + " type = " + queryType);
        
        query = "SELECT " + queryType + "("+ column + ") from sales";
        logger.log("Query  = " + query);
        double result = executeAggregateQuery(query);            
        return result;
    }
    
    public LinkedList<SalesRecord> executeFilterQuery(String query){
        int count = 0;
        LinkedList<SalesRecord> results = new LinkedList<SalesRecord>();
	try{
	    Connection con = DriverManager.getConnection("jdbc:sqlite:" + sqlDatabaseFileName);
	    PreparedStatement ps1 = con.prepareStatement(query);
	    ResultSet ps = ps1.executeQuery();
            
            logger.log("Execute the results");
            
            while (ps.next())
            {
                SalesRecord record = new SalesRecord();
                record.Region = ps.getString(Columns[0]);
                record.Country = ps.getString(Columns[1]);
                record.ItemType = ps.getString(Columns[2]);
                record.SalesChannel = ps.getString(Columns[3]);
                record.OrderPriority = ps.getString(Columns[4]);
                record.OrderDate = ps.getString(Columns[5]);
                record.OrderId = ps.getInt(Columns[6]);
                record.ShipDate = ps.getString(Columns[7]);
                record.UnitsSold = ps.getInt(Columns[8]);                   
                record.UnitPrice = ps.getDouble(Columns[9]);
                record.UnitCost = ps.getDouble(Columns[10]);
                record.TotalRevenue = ps.getDouble(Columns[11]);
                record.TotalCost = ps.getDouble(Columns[12]);
                record.TotalProfit  = ps.getDouble(Columns[13]);	
                results.add(record);
            }
            ps.close();
            con.close();
	}
	catch(Exception e){
		e.printStackTrace();
                logger.log(e.toString());
	}
        return results;
    }
             
    public LinkedList<SalesRecord> HandleFilterQuery(String queryType, String column, String parameters){
        
        logger.log("Query type = " + queryType.toLowerCase());
        LinkedList<SalesRecord> results = null;
        String query = "";
        
        if(queryType.toLowerCase().equals("filter"))
        {
            logger.log("Query type is filter, column = " + column + " and parameters = " + parameters);
            query = "SELECT * FROM sales WHERE " + column + "='" + parameters + "';";            
        }
        return executeFilterQuery(query);
    }
    
    public boolean isFilterTypeQuery(String queryType){
        return queryType.toLowerCase().equals("filter");
    }
    
    // Lambda Function Handler
    public Response handleRequest(Request request, Context context) {
        String requestName = request.getName();
        // Create logger
         logger = context.getLogger();
        
        // Register function
        register reg = new register(logger);

        logger.log("Type  is another one " + requestName + " and colum");
        
        Response r = reg.StampContainer();
        String response =  "Query type " + request.getName() + " and column name = " + request.getColumns() + " and parameter = " + request.getParameters();

        r.setMessage(response);

        String directoryName = "/tmp";
        logger.log("Called the aws lamnbda");
        String bucketName = "test.bucket.562.rah1";
        String databaseFileName = sqlDatabaseFileName;
       
        if(!checkIfFileExists(directoryName, databaseFileName))
        {
            logger.log("Creating the database file since it does not exist");
            DownloadSQLiteDatabase(bucketName, databaseFileName, databaseFileName, directoryName);
            logger.log("Downloaded the entire datavase");
        }
        
        logger.log("col = " + request.getName() + " and columns = " + request.getColumns() + " and parameters = " + request.getParameters());
        
        if(isFilterTypeQuery(request.getName())){
            logger.log("Query type is fulter");
            LinkedList<SalesRecord> results = HandleFilterQuery(request.getName(), request.getColumns(), request.getParameters());
            r.setSalesRecords(results);
            r.setCount(results.size());            
        }
        else{
            double result = HandleAggregateQuery(request.getName(), request.getColumns(), bucketName);
            r.setValue(result);
            r.setCount(1);
        }
        
        return r;
    }
    
    // int main enables testing function from cmd line
    public static void main (String[] args)
    {
        Context c = new Context() {
            @Override
            public String getAwsRequestId() {
                return "";
            }

            @Override
            public String getLogGroupName() {
                return "";
            }

            @Override
            public String getLogStreamName() {
                return "";
            }

            @Override
            public String getFunctionName() {
                return "";
            }

            @Override
            public String getFunctionVersion() {
                return "";
            }

            @Override
            public String getInvokedFunctionArn() {
                return "";
            }

            @Override
            public CognitoIdentity getIdentity() {
                return null;
            }

            @Override
            public ClientContext getClientContext() {
                return null;
            }

            @Override
            public int getRemainingTimeInMillis() {
                return 0;
            }

            @Override
            public int getMemoryLimitInMB() {
                return 0;
            }

            @Override
            public LambdaLogger getLogger() {
                return new LambdaLogger() {
                    @Override
                    public void log(String string) {
                        System.out.println("LOG:" + string);
                    }
                };
            }
        };
        
        // Create an instance of the class
        Service3 lt = new Service3();
        
        
        // Grab the name from the cmdline from arg 0
        String name = (args.length > 0 ? args[0] : "");
        // Create a request object
        Request req = new Request(name, "");
        
        
        // Run the function
        Response resp = lt.handleRequest(req, c);
        try
        {
            Thread.sleep(100000);
        }
        catch (InterruptedException ie)
        {
            System.out.print(ie.toString());
        }
        // Print out function result
        System.out.println("function result:" + resp.toString());
    }
}
