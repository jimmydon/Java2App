package com.jimmydonlogan.util;


import java.sql.*;

import java.util.logging.Logger;


/**
 * The type My sql db.
 */
public class MySqlDb {


    /**
     * The App my sqlcon.
     */
    public Connection appMySqlcon;


    /**
     * Instantiates a new My sql db.
     */
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

    /**
     * Gets data from wgu db.
     *
     * @param query the query
     * @return the data from wgu db
     */
    public ResultSet getDataFromWguDB(String query)  {

        try{


            Statement stmt=appMySqlcon.createStatement();


            return stmt.executeQuery(query);
            //con.close();

        }catch(SQLException  sqlexcep)
        {
            sqlexcep.printStackTrace();
        }
        return  null;
    }


    /**
     * Do update or del int.
     *
     * @param query the query
     * @return the int
     */
    public int doUpdateOrDel(String query)
    {
        Statement stmtSql;


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

