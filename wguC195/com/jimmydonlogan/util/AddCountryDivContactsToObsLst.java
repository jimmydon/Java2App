package com.jimmydonlogan.util;

import com.jimmydonlogan.exceptions.AddRecordException;
import com.jimmydonlogan.model.Contact;
import com.jimmydonlogan.model.Country;
import com.jimmydonlogan.model.FirstLevelDivision;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The type Add country div contacts to obs lst.
 */
public class AddCountryDivContactsToObsLst {

    /**
     * The C db.
     */
    final MySqlDb cDB = new MySqlDb();

    /**
     * Gets divisions for combo.
     *
     * @param divID           the div id
     * @param obsDivisions    the obs divisions
     * @param cmbxFirstLvlDiv the cmbx first lvl div
     */
    public void getDivisionsForCombo(int divID, ObsList<FirstLevelDivision> obsDivisions,
                                     ComboBox<FirstLevelDivision> cmbxFirstLvlDiv) {
        String sqlSelect = "SELECT * FROM first_level_divisions where Country_ID=" + divID + ";";
        try {





            obsDivisions.setRsltFromDB(cDB.getDataFromWguDB(sqlSelect));

            cmbxFirstLvlDiv.setItems(obsDivisions.getObsList());
        } catch (Exception err) {

            throw new AddRecordException(
                    "the following DP operation  failed : ", err);
        }

    }

    /**
     * Gets contacts for combo.
     *
     * @param obsContact   the obs contact
     * @param cmbxContacts the cmbx contacts
     */
    public void getContactsForCombo(ObsList<Contact> obsContact, ComboBox<Contact> cmbxContacts) {

        try {
            obsContact.setRsltFromDB(cDB.getDataFromWguDB("SELECT * FROM contacts;"));
            cmbxContacts.setItems(obsContact.getObsList());

        } catch (Exception err) {

            throw new AddRecordException(
                    "the following DP operation  failed : ", err);
        }
    }

    /**
     * Gets country for combo.
     *
     * @param obsCountry    the obs country
     * @param cmbxCountries the cmbx countries
     */
    public void getCountryForCombo(ObsList<Country> obsCountry, ComboBox<Country> cmbxCountries) {

        try {
            obsCountry.setRsltFromDB(cDB.getDataFromWguDB("SELECT * FROM countries;"));
            cmbxCountries.setItems(obsCountry.getObsList());
        } catch (Exception err) {

            throw new AddRecordException(
                    "the following DP operation  failed : ", err);
        }
    }


    /**
     * Put data in contact list.
     *
     * @param obsContact the obs contact
     */
    public void putDataInContactList(ObsList<Contact> obsContact) {
        Contact aRecord;
        ResultSet rsltFromDB = obsContact.getRsltFromDB();
        try {
            while (rsltFromDB.next()) {
                aRecord = new Contact();
                aRecord.setContactName(rsltFromDB.getString("Contact_Name"));
                aRecord.setContactId(rsltFromDB.getInt("Contact_ID"));

                obsContact.addToList(aRecord);


            }


        } catch (Exception err) {

            throw new AddRecordException(
                    "the following DP operation  failed : ", err);
        }
    }

    /**
     * Put data in country list.
     *
     * @param obsCountry the obs country
     */
    public void putDataInCountryList(ObsList<Country> obsCountry) {
        Country aRecord;
        ResultSet rsltFromDB = obsCountry.getRsltFromDB();
        try {
            while (rsltFromDB.next()) {
                aRecord = new Country();
                aRecord.setCountryName(rsltFromDB.getString("Country"));
                aRecord.setCountryId(rsltFromDB.getInt("Country_ID"));

                obsCountry.addToList(aRecord);


            }


        } catch (Exception err) {

            throw new AddRecordException(
                    "the following DP operation  failed : ", err);
        }
    }

    /**
     * Put data in division in list.
     *
     * @param obsDivisions the obs divisions
     */
    public void putDataInDivisionInList(ObsList<FirstLevelDivision> obsDivisions) {
        FirstLevelDivision aRecord;

        ResultSet rsltFromDB = obsDivisions.getRsltFromDB();
        obsDivisions.clearItemsObsList();
        try {
            while (rsltFromDB.next()) {
                aRecord = new FirstLevelDivision();
                aRecord.setDivisionName(rsltFromDB.getString("Division"));
                aRecord.setDivisionId(rsltFromDB.getInt("Division_ID"));


                obsDivisions.addToList(aRecord);


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The type First level division converter.
     */
    public static class FirstLevelDivisionConverter extends StringConverter<FirstLevelDivision> {

        /**
         * The Division map.
         */
        private final Map<String, FirstLevelDivision> divisionMap = new HashMap<>();

        @Override
        public String toString(FirstLevelDivision division) {

            if(division==null)
                return "";
            divisionMap.put(division.getDivisionName(), division);
            return division.getDivisionName();
        }

        @Override
        public FirstLevelDivision fromString(String name) {
            return divisionMap.get(name);
        }

    }
}
