package com.pafex.zscs;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {
    Connection connection;
    String ip,db,un,pass;

    @SuppressLint("NewApi")
    public Connection conclass(){
        ip="SG2NWPLS14SQL-v07.shr.prod.sin2.secureserver.net";
        db="ZSCS";
        un="zscsdbuser";
        pass="Pass12@5";
        StrictMode.ThreadPolicy tpolicy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(tpolicy);
        Connection con =null;
        String ConnectionURL=null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL= "jdbc:jtds:sqlserver://"+ip+";"+"databaseName="+ db+ ";user="+un+";password="+pass+";";
            con= DriverManager.getConnection(ConnectionURL);
        }
        catch (Exception ex)
        {
            Log.e("Error : ", ex.getMessage());
        }
        return con;
    }
}
