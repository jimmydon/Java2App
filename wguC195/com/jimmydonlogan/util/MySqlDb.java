package com.jimmydonlogan.util;


import java.sql.*;

import java.util.logging.Logger;


public class MySqlDb {


    public Connection appMySqlcon;

    private static final Logger LOGGER = Logger.getLogger(MySqlDb.class.getName());

    public MySqlDb(){
        try {
            appMySqlcon = DriverManager.getConnection(
                    "jdbc:mysql://wgudb.ucertify.com:3306/WJ06AM9", "U06AM9", "53688707209");
        }
        catch(SQLException sqlexcep)
        {
            sqlexcep.printStackTrace();
        }
    }

    public ResultSet getDataFromWguDB(String query)  {

        try{

            LOGGER.info("query="+query);
            Statement stmt=appMySqlcon.createStatement();


            return stmt.executeQuery(query);
            //con.close();

        }catch(SQLException  sqlexcep)
        {
            sqlexcep.printStackTrace();
        }
        return  null;
    }


    public int doUpdateOrDel(String query)
    {
        Statement stmtSql;
        LOGGER.info("sqlUpdateOrDel:"+ query );

        try{
            stmtSql= appMySqlcon.createStatement();
            return stmtSql.executeUpdate(query);
        }
        catch(SQLException  sqlexcep)
        {
           sqlexcep.printStackTrace();
        }
        return -1;
    }

}

