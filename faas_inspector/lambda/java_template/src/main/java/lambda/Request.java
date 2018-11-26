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
    String name;
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    
        String columns;
    public String getColumns()
    {
        return columns;
    }
    public void setColumns(String columns)
    {
        this.columns = columns;
    }
    
    String parameters;
    public String getParameters()
    {
        return parameters;
    }
    
    public void setParameters(String parameters)
    {
        this.parameters = parameters;
    }

    public Request(String name, String columns)
    {
        this.name = name;
        this.columns = columns;
    }
    public Request()
    {
        
    }
}