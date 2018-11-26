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
           
/*	    byte[] buffer = new byte[input.available()];
            String currentDirectory = System.getProperty("user.dir");
            logger.log("Current dir = " + currentDirectory);
            input.read(buffer);
            logger.log("Read the entire buffer of length = " +  buffer.length);            
	    File file = new File(currentDirectory, filename);      
	    OutputStream out = new FileOutputStream(file);
            logger.log("Write to the buffer");
            out.write(buffer);
            out.flush();
	    out.close(); 
	}
	catch(Exception e){
	    e.printStackTrace();
            logger.log(e.toString());  */

        logger.log("File after writing has size = " + file.length());

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
    
    // Lambda Function Handler
    public Response handleRequest(Request request, Context context) {
                        String hello = "Hello " + request.getName();



        // Create logger
         logger = context.getLogger();
        
        // Register function
        register reg = new register(logger);

        //stamp container with uuid
        Response r = reg.StampContainer();
        String directoryName = "/tmp";
                r.setValue(hello);
        
        setCurrentDirectory(directoryName);
        
        logger.log("Called the aws lamnbda");
        String bucketName = "test.bucket.562.rah1";
        String fileName = "salespipeline.db";
       
            
        
        
        if(!checkIfFileExists(directoryName, fileName)){
            DownloadSQLiteDatabase(bucketName, fileName, fileName, directoryName);
            logger.log("Downloaded the entire datavase");
        }

        /*
        
        try{
            Connection con = DriverManager.getConnection("jdbc:sqlite:salespipeline.db");

            logger.log("trying to create table 'sales' if it does not exists"); 
            PreparedStatement   ps   =   con.prepareStatement("SELECT   name   FROM   sqlite_master   WHERE type='table' AND name='sales'");
            ResultSet rs = ps.executeQuery();
            if (!rs.next())
            {
                    // 'sales' does not exist, and should be created
                    logger.log("trying to create table 'sales'");
                    ps = con.prepareStatement("CREATE TABLE IF NOT EXISTS sales(region text,country text,itemtype text,saleschannel text,orderpriority text,orderdate text,orderid integer,shipdate text,unitssold integer,unitprice real,unitcost real,totalrevenue real,totalcost real,totalprofit real);");  
                    ps.execute();
            }
            rs.close();
            logger.log("Created the datavase");
        }
        catch(Exception ex)
        {
        logger.log(ex.toString());
        }

        */
        
        
        
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
        
        // Create a request object
        Request req = new Request();
        
        // Grab the name from the cmdline from arg 0
        String name = (args.length > 0 ? args[0] : "");
        
        // Load the name into the request object
        req.setName(name);

        // Report name to stdout
        System.out.println("cmd-line param name=" + req.getName());
        
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
