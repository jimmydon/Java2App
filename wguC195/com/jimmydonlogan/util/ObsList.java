package com.jimmydonlogan.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;


/**
 * The type Obs list.
 *
 * @param <T> the type parameter
 */
public class ObsList<T> {

    /**
     * The Obs list data in db.
     */
    private final ObservableList<T> obsListDataInDB;
    /**
     * The Rslt from db.
     */
    private ResultSet rsltFromDB;

    /**
     * Instantiates a new Obs list.
     */
    public ObsList() {

        obsListDataInDB = FXCollections.observableArrayList();

    }

    /**
     * Gets obs list.
     *
     * @return the obs list
     */
    public ObservableList<T> getObsList() {
        return obsListDataInDB;
    }


    /**
     * Add to list.
     *
     * @param record the record
     */
    public void addToList(T record)
   {

       obsListDataInDB.add(record);

   }

    /**
     * Clear items obs list.
     */
    public void clearItemsObsList()
   {
       obsListDataInDB.clear();
   }

    /**
     * Gets rslt from db.
     *
     * @return the rslt from db
     */
    public ResultSet getRsltFromDB() {
        return rsltFromDB;
    }

    /**
     * Sets rslt from db.
     *
     * @param rsltFromDB the rslt from db
     */
    public void setRsltFromDB(ResultSet rsltFromDB) {
        this.rsltFromDB = rsltFromDB;
    }

}
