package com.jimmydonlogan.controller;

import com.jimmydonlogan.exceptions.AddRecordException;
import com.jimmydonlogan.model.*;
import com.jimmydonlogan.util.*;
import javafx.scene.control.*;
import com.jimmydonlogan.view.AddModifyCustomerView;

import java.util.ArrayList;

public class AddModifyCustomer implements MakeAddUpdateView {

    private final ObsList<FirstLevelDivision> obsDivisions;
    private final Button btnCancel = new Button("Cancel");
    private final Button btnSave = new Button("Save");

    private int selectedDivisionId;
    private final AddCountryDivContactsToObsLst addRecordsToLst;
    private Boolean customerAddedOrModifiedOrDeleted;
    private final CustomersAppointsUtils cusUtil = new CustomersAppointsUtils();

    public AddModify enumAddModify;

    public final AddModifyCustomerView viewAddCustomer = new AddModifyCustomerView(AddModify.MODIFY, btnSave, btnCancel);

    private Customer cstData;
    private ArrayList<TextField> custlstTxtFields;
    private final CustomersAppointsUtils util = new CustomersAppointsUtils();
    public final TextFormatter<String> formatterAlphaSpace = new TextFormatter<>(util.filterAlphaSpace);
    public final TextFormatter<String> formatterAlphaNumericSpace = new TextFormatter<>(util.filterAlphaNumericSpace);

    public final TextFormatter<String> formatterAlphaNumericNoSpace = new TextFormatter<>(util.filterAlphaNumericNoSpace);


    public AddModifyCustomer() {


        addRecordsToLst = new AddCountryDivContactsToObsLst();
        initArrayTextFields();
        addEvents();

        obsDivisions = new ObsList<>();
        ObsList<Country> obsCountry = new ObsList<>();
        addRecordsToLst.getCountryForCombo(obsCountry, AddModifyCustomerView.cmbxCountries);

        addRecordsToLst.putDataInCountryList(obsCountry);

    }

    public void display() {


        customerAddedOrModifiedOrDeleted = false;

        viewAddCustomer.buildAddCustomerForm();


    }

    public Boolean getCustomerAddedModified() {
        return customerAddedOrModifiedOrDeleted;
    }

    public void putSelectedCustomerInModifyInput(Customer aCust) {


        putSelectedModifiedInTextBoxsForDisplay(aCust);
        getAndPutSelectedCustomerCountryDivisionToDisplayForModified(aCust);


    }
private void getAndPutSelectedCustomerCountryDivisionToDisplayForModified(Customer aCust)
{
    Country t = new Country();
    FirstLevelDivision y = new FirstLevelDivision();
    cusUtil.getSelectedCustomersCountryDivision(t, y, aCust.getDivision_id());
    selectedDivisionId = aCust.getDivision_id();

    putDataInDivisionComboBox(t);

    AddModifyCustomerView.cmbxCountries.setValue(t);
    AddModifyCustomerView.cmbxFirstLvlDiv.setValue(y);
}
    private void putSelectedModifiedInTextBoxsForDisplay(Customer aCust) {

        AddModifyCustomerView.txtName.setText(aCust.getName());
        AddModifyCustomerView.txtAddress.setText(aCust.getAddress());
        AddModifyCustomerView.txtPhone.setText(aCust.getPhone());
        AddModifyCustomerView.txtPostalCode.setText(aCust.getPostal_code());
        AddModifyCustomerView.custid = aCust.getCust_id();
    }

    private void nameTextFields() {
        AddModifyCustomerView.txtName.setId("Name");
        AddModifyCustomerView.txtAddress.setId("Address");
        AddModifyCustomerView.txtPostalCode.setId("Postal Code");
        AddModifyCustomerView.txtPhone.setId("Phone");
    }

    private void initArrayTextFields() {
        nameTextFields();
        custlstTxtFields = new ArrayList<>();
        putTextFieldsInArray(custlstTxtFields);
    }

    private void addEvents() {


        addSaveBtnEvent();
        addCancelBtnEvent();
        addCountriesDivisionsComboEvent();
        addErrorChecking();
    }

    private void addSaveBtnEvent() {
        btnSave.setOnAction((event) -> addOrModifyCustomerInDB(custlstTxtFields));

    }

    private void addCountriesDivisionsComboEvent() {


        AddModifyCustomerView.cmbxCountries.setOnAction((event) -> {
            Country selectedCountry = AddModifyCustomerView.cmbxCountries.getSelectionModel().getSelectedItem();
            putDataInDivisionComboBox(selectedCountry);


        });

        AddModifyCustomerView.cmbxFirstLvlDiv.setOnAction((event) -> selectedDivisionId = AddModifyCustomerView.cmbxFirstLvlDiv.getSelectionModel().getSelectedItem().getDivisionId());
    }

    private void addCancelBtnEvent() {
        btnCancel.setOnAction(e -> viewAddCustomer.stgAddCustomerForm.close());

    }

    private void addErrorChecking() {
        AddModifyCustomerView.txtName.setTextFormatter(formatterAlphaSpace);
        AddModifyCustomerView.txtAddress.setTextFormatter(formatterAlphaNumericSpace);
        AddModifyCustomerView.txtPostalCode.setTextFormatter(formatterAlphaNumericNoSpace);

        // below will turn input red if non Aplhanumberic values entered....not necessary because of regex above may have use
        cusUtil.addErrorEventsAlphaAndSpacesOnly(AddModifyCustomerView.txtName);


    }

    private void putDataInDivisionComboBox(Country country) {


        addRecordsToLst.getDivisionsForCombo(country.getCountryId(), obsDivisions, AddModifyCustomerView.cmbxFirstLvlDiv);

        addRecordsToLst.putDataInDivisionInList(obsDivisions);
    }

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

    private void feedbackOnDBTransaction(Boolean statusOfDbTrans) {
        if (!statusOfDbTrans) {
            cusUtil.showalert("An error occurred please check input and try again ");


        } else {
            if (enumAddModify == AddModify.ADD)
                cusUtil.showalert(AddModifyCustomerView.txtName.getText() + " has been added to the database.");
            else
                cusUtil.showalert(AddModifyCustomerView.txtName.getText() + " has been modified in the database.");


            customerAddedOrModifiedOrDeleted = true;
            clearInputs();
        }
    }

    private Boolean doAddOrModifyINDB() {
        boolean statusOfDbTrans;
        if (enumAddModify == AddModify.ADD)
            statusOfDbTrans = cusUtil.insertDataIntoCustomerTable(cstData);
        else
            statusOfDbTrans = cusUtil.updateCustomerRecordInDb(cstData);

        return statusOfDbTrans;
    }

    private Boolean checkInputs(ArrayList<TextField> lstTxtFields) {
        if (selectedDivisionId == 0) {
            cusUtil.showalert("Please select a Country and Division");
            return false;
        }
        if (cusUtil.anEmptyTextField(lstTxtFields))
            return false;
        if (cusUtil.hasMinChars(lstTxtFields))
            return false;



        return cusUtil.phoneNumberIsValid(AddModifyCustomerView.txtPhone.getText());
    }

    private void putTextFieldsInArray(ArrayList<TextField> lstTxtFields) {


        lstTxtFields.add(AddModifyCustomerView.txtName);
        lstTxtFields.add(AddModifyCustomerView.txtAddress);
        lstTxtFields.add(AddModifyCustomerView.txtPostalCode);
        lstTxtFields.add(AddModifyCustomerView.txtPhone);


    }

    public void clearInputs() {
        AddModifyCustomerView.txtName.setText("");
        AddModifyCustomerView.txtAddress.setText("");
        AddModifyCustomerView.txtPostalCode.setText("");
        AddModifyCustomerView.txtPhone.setText("");
    }


}


