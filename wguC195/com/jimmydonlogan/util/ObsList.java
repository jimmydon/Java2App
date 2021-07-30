package com.jimmydonlogan.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;


public class ObsList<T> {

    private final ObservableList<T> obsListDataInDB;
    private ResultSet rsltFromDB;

public ObsList() {

        obsListDataInDB = FXCollections.observableArrayList();

    }
    public ObservableList<T> getObsList() {
        return obsListDataInDB;
    }


   public void addToList(T record)
   {

       obsListDataInDB.add(record);

   }
   public void clearItemsObsList()
   {
       obsListDataInDB.clear();
   }
    public ResultSet getRsltFromDB() {
        return rsltFromDB;
    }

    public void setRsltFromDB(ResultSet rsltFromDB) {
        this.rsltFromDB = rsltFromDB;
    }

}
