/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lambda;

import java.util.LinkedList;

/**
 *
 * @author wlloyd
 */
public class Request {
    
    public LinkedList<AggregateInfo> aggregateinfo;
    
    public LinkedList<AggregateInfo> getAggregateInfo(){
        return this.aggregateinfo;
    }
    
    public void setAggregateInfo(LinkedList<AggregateInfo> aggregateinfo){
        this.aggregateinfo = aggregateinfo;
    }

    public String[] columns;
    public String[] getColumns(){
        return this.columns;
    }    
    public void setColumns(String[] columns){
        this.columns = columns;
    }    
    
    LinkedList<FilterInfo> filterinfo;     
    public LinkedList<FilterInfo> getFilterinfo(){
        return this.filterinfo;
    }
    
    public void setFilterinfo(LinkedList<FilterInfo> filterinfo){
        this.filterinfo = filterinfo;
    }
    
    public Request(String bucketname, String databasefilename, LinkedList<AggregateInfo> aggregateinfo, 
                    LinkedList<FilterInfo> filterInfo,
                    String[] columns, String[] groupbycolumns, String outputbucketname, String outputfilename, String clientid)
    {
        this.databasefilename = databasefilename;
        this.bucketname = bucketname;
        this.aggregateinfo = aggregateinfo;
        this.filterinfo = filterInfo;
        this.columns = columns;
        this.groupbycolumns = groupbycolumns;
        this.outputbucketname = outputbucketname;
        this.outputfilename = outputfilename;
        this.clientid = clientid;
    }
    
    private String bucketname;
    public String getBucketname(){
        return this.bucketname;
    }
    
    private String clientid;
    public String getClientid(){
        return clientid;
    }
    public void setClientid(String clientid){
        this.clientid = clientid;
    }
    
    private String outputbucketname;
    public String getOutputbucketname(){
        return outputbucketname;
    }
    public void setOutputbucketname(String outputbucketname){
        this.outputbucketname = outputbucketname;
    }
    
    private String outputfilename;
    public String getOutputfilename(){
        return this.outputfilename;
    }
    
    public void setOutputfilename(String outputfilename){
        this.outputfilename = outputfilename;
    }
    
    public void setBucketname(String bucketname){
        this.bucketname = bucketname;
    }
    
    private String databasefilename;
    public String getDatabasefilename(){
        return databasefilename;
    }
    public void setDatabasefilename(String databasefilename){
        this.databasefilename = databasefilename;;
    }
    
    public String toString(){
        return "bucket = " +  getBucketname() + " and file = " + getDatabasefilename();
    }
    
    private String[] groupbycolumns;
    public String[] getGroupbycolumns(){
        return groupbycolumns;
    }
    public void setGroupbycolumns(String[] groupbycolumns){
        this.groupbycolumns = groupbycolumns;
    }
    
    public Request()
    {
        
    }
}