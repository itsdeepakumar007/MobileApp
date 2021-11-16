package com.pafex.zscs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListItem {

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    public List<Map<String,String>>getList()
    {
        List<Map<String,String>> data;
        data = new ArrayList<Map<String,String>>();
        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.conclass();
            if(connect!=null){
                String country_query = "select * from dbo.CountryMaster" ;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(country_query);
                while (resultSet.next()){
                    Map<String,String> country_master = new HashMap<String,String>();
                    country_master.put("ID",resultSet.getString("ID"));
                    country_master.put("Name",resultSet.getString("Name"));
                    country_master.put("CountryCode",resultSet.getString("CountryCode"));
                    data.add(country_master);
                }

                ConnectionResult = "Success";
                isSuccess = true;
                connect.close();
            }
            else {
                ConnectionResult = "Failed";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return data;
    }
}


