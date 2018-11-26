/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lambda;

/**
 *
 * @author wlloyd
 */
public class Request {

    String querytype;
    String[]  columns;
    
    public String getQuerytype(){
        return this.querytype;
    }
    
    public void setQuerytype(String querytype){
        this.querytype = this.querytype;
    }

    public String[] getColumns(){
        return this.columns;
    }
    
    public void setColumns(String[] columns){
        this.columns = this.columns;
    }

    public Request(String querytype, String[] columns)
    {
        this.querytype = querytype;
        this.columns = columns;
    }
    
    public Request()
    {
        
    }
}
