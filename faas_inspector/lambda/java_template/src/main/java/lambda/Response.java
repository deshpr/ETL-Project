/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lambda;

import faasinspector.fiResponse;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wlloyd
 */
public class Response extends fiResponse {
    
    //
    // User Defined Attributes
    //
    //
    // ADD getters and setters for custom attributes here.
    //

    // Return value
    private String value;
    public String getValue()
    {
        return value;
    }
    public void setValue(String value)
    {
        this.value = value;
    }
    
    private LinkedList<SalesRecord> salesRecords;
    public LinkedList<SalesRecord> getSalesRecords(){
        return salesRecords;
    }
    
    public void setSalesRecords(LinkedList<SalesRecord> salesRecords){
        this.salesRecords = salesRecords;
    }
    
    private int count;
    public int getCount(){
        return count;
    }
    
    public void setCount(int count){
        this.count = count;
    }
    
    @Override
    public String toString()
    {
        return "value=" + this.getValue() + super.toString(); 
    }

}
