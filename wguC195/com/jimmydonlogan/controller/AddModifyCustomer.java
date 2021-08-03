package com.jimmydonlogan.controller;

import com.jimmydonlogan.exceptions.AddRecordException;
import com.jimmydonlogan.model.*;
import com.jimmydonlogan.util.*;
import javafx.scene.control.*;
import com.jimmydonlogan.view.AddModifyCustomerView;

import java.util.ArrayList;

/**
 * The type Add modify customer.
 */
public class AddModifyCustomer implements MakeAddUpdateView {

    /**
     * The Obs divisions.
     */
    private final ObsList<FirstLevelDivision> obsDivisions;
    /**
     * The Btn cancel.
     */
    private final CustomerAppointBean notifyOfCustAddCancel;
    private final Button btnCancel = new Button("Cancel");
    /**
     * The Btn save.
     */
    private final Button btnSave = new Button("Save");

    /**
     * The Selected division id.
     */
    private int selectedDivisionId;
    /**
     * The Add records to lst.
     */
    private final AddCountryDivContactsToObsLst addRecordsToLst;
    /**
     * The Customer added or modified or deleted.
     */
    private Boolean customerAddedOrModifiedOrDeleted;
    /**
     * The Cus util.
     */
    private final CustomersAppointsUtils cusUtil = new CustomersAppointsUtils();

    /**
     * The Enum add modify.
     */
    public AddModify enumAddModify;

    /**
     * The View add customer.
     */
    public final AddModifyCustomerView viewAddCustomer = new AddModifyCustomerView(AddModify.MODIFY, btnSave, btnCancel);

    /**
     * The Cst data.
     */
    private Customer cstData;
    /**
     * The Custlst txt fields.
     */
    private ArrayList<TextField> custlstTxtFields;
    /**
     * The Util.
     */
    private final CustomersAppointsUtils util = new CustomersAppointsUtils();
    /**
     * The Formatter alpha space.
     */
    public final TextFormatter<String> formatterAlphaSpace = new TextFormatter<>(util.filterAlphaSpace);
    /**
     * The Formatter alpha numeric space.
     */
    public final TextFormatter<String> formatterAlphaNumericSpace = new TextFormatter<>(util.filterAlphaNumericSpace);

    /**
     * The Formatter alpha numeric no space.
     */
    public final TextFormatter<String> formatterAlphaNumericNoSpace = new TextFormatter<>(util.filterAlphaNumericNoSpace);


    /**
     * Instantiates a new Add modify customer.
     */
    public AddModifyCustomer(CustomerAppointBean notifyOfCustAddCancel) {


        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            this.notifyOfCustAddCancel=notifyOfCustAddCancel;
            addRecordsToLst = new AddCountryDivContactsToObsLst();
            initArrayTextFields();
            addEvents();

            obsDivisions = new ObsList<>();
            ObsList<Country> obsCountry = new ObsList<>();
            addRecordsToLst.getCountryForCombo(obsCountry, AddModifyCustomerView.cmbxCountries);

            addRecordsToLst.putDataInCountryList(obsCountry);
        }

    }

    /**
     * Display.
     */
    public void display() {


        try {
            customerAddedOrModifiedOrDeleted = false;

            viewAddCustomer.buildAddCustomerForm();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Gets customer added modified.
     *
     * @return the customer added modified
     */
    public Boolean getCustomerAddedModified() {
        return customerAddedOrModifiedOrDeleted;
    }

    /**
     * Put selected customer in modify input.
     *
     * @param aCust the a cust
     */
    public void putSelectedCustomerInModifyInput(Customer aCust) {


        try {
            putSelectedModifiedInTextBoxsForDisplay(aCust);
            getAndPutSelectedCustomerCountryDivisionToDisplayForModified(aCust);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Gets and put selected customer country division to display for modified.
     *
     * @param aCust the a cust
     */
    private void getAndPutSelectedCustomerCountryDivisionToDisplayForModified(Customer aCust)
{
    try {
        Country t = new Country();
        FirstLevelDivision y = new FirstLevelDivision();
        cusUtil.getSelectedCustomersCountryDivision(t, y, aCust.getDivision_id());
        selectedDivisionId = aCust.getDivision_id();

        putDataInDivisionComboBox(t);

        AddModifyCustomerView.cmbxCountries.setValue(t);
        AddModifyCustomerView.cmbxFirstLvlDiv.setValue(y);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    /**
     * Put selected modified in text boxs for display.
     *
     * @param aCust the a cust
     */
    private void putSelectedModifiedInTextBoxsForDisplay(Customer aCust) {

        try {
            AddModifyCustomerView.txtName.setText(aCust.getName());
            AddModifyCustomerView.txtAddress.setText(aCust.getAddress());
            AddModifyCustomerView.txtPhone.setText(aCust.getPhone());
            AddModifyCustomerView.txtPostalCode.setText(aCust.getPostal_code());
            AddModifyCustomerView.custid = aCust.getCust_id();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Name text fields.
     */
    private void nameTextFields() {
        try {
            AddModifyCustomerView.txtName.setId("Name");
            AddModifyCustomerView.txtAddress.setId("Address");
            AddModifyCustomerView.txtPostalCode.setId("Postal Code");
            AddModifyCustomerView.txtPhone.setId("Phone");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Init array text fields.
     */
    private void initArrayTextFields() {
        try {
            nameTextFields();
            custlstTxtFields = new ArrayList<>();
            putTextFieldsInArray(custlstTxtFields);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add events.
     */
    private void addEvents() {


        try {
            addSaveBtnEvent();
            addCancelBtnEvent();
            addCountriesDivisionsComboEvent();
            addErrorChecking();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add save btn event.
     * using lambda expression to instantiate btnSave and avoid using bulky anonymous class implementation
     * also smaller cleaner code
     */
    private void addSaveBtnEvent() {
        try {
            btnSave.setOnAction((event) -> addOrModifyCustomerInDB(custlstTxtFields));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Add countries divisions combo event.
     * using lambda expression to instantiate cmbxCountries.setOnAction and avoid using bulky anonymous class implementation
     * also smaller cleaner code
     */
    private void addCountriesDivisionsComboEvent() {


        try {
            AddModifyCustomerView.cmbxCountries.setOnAction((event) -> {
                Country selectedCountry = AddModifyCustomerView.cmbxCountries.getSelectionModel().getSelectedItem();
                putDataInDivisionComboBox(selectedCountry);


            });

            AddModifyCustomerView.cmbxFirstLvlDiv.setOnAction((event) -> selectedDivisionId = AddModifyCustomerView.cmbxFirstLvlDiv.getSelectionModel().getSelectedItem().getDivisionId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add cancel btn event.
     * using lambda expression to instantiate btnCanceland avoid using bulky anonymous class implementation
     * also smaller cleaner code
     */
    private void addCancelBtnEvent() {
        try {
            btnCancel.setOnAction(e -> cancelNotifyClose());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void cancelNotifyClose()
    {

        notifyOfCustAddCancel.setValue(1);
        viewAddCustomer.stgAddCustomerForm.close();
    }
    /**
     * Add error checking.
     */
    private void addErrorChecking() {
        try {
            AddModifyCustomerView.txtName.setTextFormatter(formatterAlphaSpace);
            AddModifyCustomerView.txtAddress.setTextFormatter(formatterAlphaNumericSpace);
            AddModifyCustomerView.txtPostalCode.setTextFormatter(formatterAlphaNumericNoSpace);

            // below will turn input red if non Aplhanumberic values entered....not necessary because of regex above may have use
            cusUtil.addErrorEventsAlphaAndSpacesOnly(AddModifyCustomerView.txtName);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Put data in division combo box.
     *
     * @param country the country
     */
    private void putDataInDivisionComboBox(Country country) {


        try {
            addRecordsToLst.getDivisionsForCombo(country.getCountryId(), obsDivisions, AddModifyCustomerView.cmbxFirstLvlDiv);

            addRecordsToLst.putDataInDivisionInList(obsDivisions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add or modify customer in db.
     *
     * @param lstTxtFields the lst txt fields
     */
    private void addOrModifyCustomerInDB(ArrayList<TextField> lstTxtFields) {


        try {
            if (!checkInputs(lstTxtFields))
                return;


            cstData = cusUtil.putInputDataInCustomerObject(selectedDivisionId);

            Boolean statusOfDbTrans = doAddOrModifyINDB();
            feedbackOnDBTransaction(statusOfDbTrans);


        } catch (Exception err) {

            throw new AddRecordException(
                    "the following DB operation  failed : ", err);
        }


    }

    /**
     * Feedback on db transaction.
     *
     * @param statusOfDbTrans the status of db trans
     */
    private void feedbackOnDBTransaction(Boolean statusOfDbTrans) {
        try {
            if (!statusOfDbTrans) {
                cusUtil.showAlert("An error occurred please check input and try again ");


            } else {
                if (enumAddModify == AddModify.ADD)
                    cusUtil.showAlert(AddModifyCustomerView.txtName.getText() + " has been added to the database.");
                else
                    cusUtil.showAlert(AddModifyCustomerView.txtName.getText() + " has been modified in the database.");


                customerAddedOrModifiedOrDeleted = true;
                clearInputs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Do add or modify indb boolean.
     *
     * @return the boolean
     */
    private Boolean doAddOrModifyINDB() {
        try {
            boolean statusOfDbTrans;
            if (enumAddModify == AddModify.ADD)
                statusOfDbTrans = cusUtil.insertDataIntoCustomerTable(cstData);
            else
                statusOfDbTrans = cusUtil.updateCustomerRecordInDb(cstData);

            return statusOfDbTrans;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check inputs boolean.
     *
     * @param lstTxtFields the lst txt fields
     * @return the boolean
     */
    private Boolean checkInputs(ArrayList<TextField> lstTxtFields) {
        try {
            if (selectedDivisionId == 0) {
                cusUtil.showAlert("Please select a Country and Division");
                return false;
            }
            if (cusUtil.anEmptyTextField(lstTxtFields))
                return false;
            if (cusUtil.hasMinChars(lstTxtFields))
                return false;


            return cusUtil.phoneNumberIsValid(AddModifyCustomerView.txtPhone.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Put text fields in array.
     *
     * @param lstTxtFields the lst txt fields
     */
    private void putTextFieldsInArray(ArrayList<TextField> lstTxtFields) {


        try {
            lstTxtFields.add(AddModifyCustomerView.txtName);
            lstTxtFields.add(AddModifyCustomerView.txtAddress);
            lstTxtFields.add(AddModifyCustomerView.txtPostalCode);
            lstTxtFields.add(AddModifyCustomerView.txtPhone);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Clear inputs.
     */
    public void clearInputs() {
        try {
            AddModifyCustomerView.txtName.setText("");
            AddModifyCustomerView.txtAddress.setText("");
            AddModifyCustomerView.txtPostalCode.setText("");
            AddModifyCustomerView.txtPhone.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


