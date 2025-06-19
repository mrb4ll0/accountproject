/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.general;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.faces.context.FacesContext;

/**
 *
 * @author tolua
 */

public class DBConnection {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mybank2?useSSL=false";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD = "hidalgo001.com";
    public static void main(String[] args) throws Exception{
        DBConnection obj_DB_connection=new DBConnection();
        System.out.println(obj_DB_connection.get_connection());
    }
    public Connection get_connection() throws Exception{
        Connection connection=null;
        
//FacesContext ctx = FacesContext.getCurrentInstance();
//String myServerValue = ctx.getExternalContext().getInitParameter("DBSERVERIP");
//String myDBnameValue = ctx.getExternalContext().getInitParameter("DBUSERNAME");
//String myDBnameUserValue = ctx.getExternalContext().getInitParameter("DBUSERNAME");
//String myDBnamePassValue = ctx.getExternalContext().getInitParameter("DBPASSWORD");

        try {
        Class.forName("com.mysql.jdbc.Driver");
        connection=DriverManager.getConnection(JDBC_URL,JDBC_USERNAME,JDBC_PASSWORD);
        //connection=DriverManager.getConnection("jdbc:mysql://localhost/molusi","root","");
        //connection=DriverManager.getConnection("jdbc:mysql://" + myServerValue + "/mybank",myDBnameUserValue,myDBnamePassValue);
        } catch (Exception e) {
            System.out.println(e);
        }
        return connection;
    }
}