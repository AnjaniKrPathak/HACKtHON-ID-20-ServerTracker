package com.example.servertracker.server.repo;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConfigDataSource {

    @Bean
    public static StringBuilder getDBFlatOfferingDetails(String url, String user_name) throws SQLException
    {

        DataSourceBuilder<?> dSB
                = DataSourceBuilder.create();
        dSB.driverClassName("oracle.jdbc.OracleDriver");
        dSB.url(url);
        dSB.username(user_name);
        dSB.password(user_name);

        Connection connection=dSB.build().getConnection();
//        String sqlQueryFO = "select flat_offering_id,name from poc_offering where flat_offering_id =76120000";
        String sqlsqlQueryDBSpace=    "select a.tablespace_name \"Table_Space_Name\",a.total \"SPACE_ALLOCATED\",a.total-b.free \"SPACE_USED\",b.free \"SPACE_FREE\", \n" +
                "trunc(((a.total-b.free)/a.total)*100) \"PCT_Used\" from \n" +
                "(select tablespace_name,sum(bytes/1024/1024) Total from dba_data_files group by tablespace_name) a, \n" +
                "(select tablespace_name,sum(bytes/1024/1024) Free from dba_free_space group by tablespace_name) b \n" +
                "where a.tablespace_name=b.tablespace_name";

        // Create a statement
        Statement statement = connection.createStatement();
        // Execute the query
        ResultSet resultSet = statement.executeQuery(sqlsqlQueryDBSpace);


        // Process the result set
        StringBuilder result = new StringBuilder();
        while (resultSet.next()) {
            result.append("Server Name:").append(url).append(":  Table_Space_Name: ").append(resultSet.getInt("SPACE_ALLOCATED")).append(", SPACE_USED: ").append(resultSet.getString("SPACE_USED")).append(", SPACE_FREE: ").append(resultSet.getString("SPACE_FREE")).append(", PCT_Used: ").append(resultSet.getString("PCT_Used")).append("\n");
        }

        // Close resources
        resultSet.close();
        statement.close();
        connection.close();
        return result;
    }
}