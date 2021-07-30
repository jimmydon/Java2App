package com.jimmydonlogan.controller;


import com.jimmydonlogan.exceptions.AddRecordException;
import com.jimmydonlogan.model.*;
import com.jimmydonlogan.util.*;
import com.jimmydonlogan.view.AddModifyAppointmentView;
import javafx.scene.control.*;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;

public class AddModifyAppointment implements MakeAddUpdateView {


    private final Button btnCancel = new Button("Cancel");
    private final Button btnSave = new Button("Save");
    public final AddModifyAppointmentView viewAddModifyAppointment;
    private final CustomersAppointsUtils apmntUtil = new CustomersAppointsUtils();
    private Boolean appointmentAddedOrModified;


    public AddModify enumAddModify;

    private final User user;
    private ArrayList<TextField> apmntlstTxtFields;
    Appointment apmntData;
    private final CustomersAppointsUtils util = new CustomersAppointsUtils();
    private final TextFormatter<String> formatterAlphaSpace = new TextFormatter<>(util.filterAlphaSpace);
    private final TextFormatter<String> formatterAlphaNumericSpace1 = new TextFormatter<>(util.filterAlphaNumericSpace);
    private final TextFormatter<String> formatterAlphaNumericSpace2 = new TextFormatter<>(util.filterAlphaNumericSpace);
    private final TextFormatter<String> formatterAlphaNumericSpace3 = new TextFormatter<>(util.filterAlphaNumericSpace);
    private final ObsList<Appointment> obsvAppnmt;
    private final Print prt = new Print();

    public AddModifyAppointment(ComboBox<Customer> cmbxCustomers, User user, ObsList<Appointment> obsvAppnmt) {
        this.obsvAppnmt = obsvAppnmt;
        this.user = user;
        initArrayTextFields();
        viewAddModifyAppointment = new AddModifyAppointmentView(btnSave, btnCancel, cmbxCustomers);
        ObsList<Contact> obsContact = new ObsList<>();
        AddCountryDivContactsToObsLst addRecordsToLst = new AddCountryDivContactsToObsLst();
        addRecordsToLst.getContactsForCombo(obsContact, viewAddModifyAppointment.cmbxContacts);

        addRecordsToLst.putDataInContactList(obsContact);
        addEvents();
    }

    public void display() {

        viewAddModifyAppointment.addOrModify = enumAddModify;
        appointmentAddedOrModified = false;

        viewAddModifyAppointment.buildAddAppointmentForm();


    }

    private void addEvents() {

        try {
            addBtnSaveEvent();
            addBtnCancelEvent();
            addErrorChecking();
            addDatePickerEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void addDatePickerEvents() {

        viewAddModifyAppointment.dtpStart.getEditor().focusedProperty().addListener((obj, wasFocused, isFocused) -> {
            if (!isFocused) {
                try {
                    viewAddModifyAppointment.dtpStart.setValue(
                            viewAddModifyAppointment.dtpStart.getConverter().fromString(viewAddModifyAppointment.dtpStart.getEditor().getText()));
                } catch (DateTimeParseException e) {
                    viewAddModifyAppointment.dtpStart.getEditor().setText(
                            viewAddModifyAppointment.dtpStart.getConverter().toString(viewAddModifyAppointment.dtpStart.getValue()));
                }
            }
        });

        viewAddModifyAppointment.dtpEnd.getEditor().focusedProperty().addListener((obj, wasFocused, isFocused) -> {
            if (!isFocused) {
                try {
                    viewAddModifyAppointment.dtpEnd.setValue(
                            viewAddModifyAppointment.dtpEnd.getConverter().fromString(
                                    viewAddModifyAppointment.dtpEnd.getEditor().getText()));
                } catch (DateTimeParseException e) {
                    viewAddModifyAppointment.dtpEnd.getEditor().setText(
                            viewAddModifyAppointment.dtpEnd.getConverter().toString(
                                    viewAddModifyAppointment.dtpEnd.getValue()));
                }
            }
        });
    }

    private void addBtnSaveEvent() {
        try {
            btnSave.setOnAction((event) -> addOrModifyAppointmentInDB(apmntlstTxtFields));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addBtnCancelEvent() {
        try {
            btnCancel.setOnAction(e -> viewAddModifyAppointment.stgAddAppointmentForm.close());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean doAddOrModifyInDb() {
        try {

            Boolean statusOfDbTrans;

            if (enumAddModify == AddModify.ADD)
                statusOfDbTrans = apmntUtil.insertDataIntoAppointmentDBTable(apmntData);
            else
                statusOfDbTrans = apmntUtil.updateAppointmentRecordInDb(apmntData);

            return statusOfDbTrans;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void addOrModifyAppointmentInDB(ArrayList<TextField> lstTxtFields) {


        apmntData = apmntUtil.putInputDataInAppointmentObject(viewAddModifyAppointment);
        apmntData.setContactId(viewAddModifyAppointment.cmbxContacts.getSelectionModel().getSelectedItem().getContactId());
        apmntData.setCust_id(AddModifyAppointmentView.cmbxCustomers.getSelectionModel().getSelectedItem().getCust_id());
        apmntData.setUser_ID(user.getUserId());


        if (!checkInputs(lstTxtFields))
            return;


        try {


            Boolean statusOfDbTrans = doAddOrModifyInDb();
            feedbackOnDBTransaction(statusOfDbTrans);


        } catch (Exception err) {

            throw new AddRecordException(
                    "the following DP operation  failed : ", err);
        }


    }

    private Boolean checkInputs(ArrayList<TextField> lstTxtFields) {


        try {
            if (!apmntUtil.timeDateSelected(viewAddModifyAppointment.dtpStart, "start"))
                return false;
            if (!apmntUtil.timeDateSelected(viewAddModifyAppointment.dtpEnd, "end"))
                return false;


            if (!apmntUtil.dateTimesAreValid(apmntData.getStart(), apmntData.getEnd()))
                return false;

            if (apmntUtil.isBetweenOpeningClosing(apmntData.getStart())) {

                apmntUtil.showalert("The start time must be during business hours. After " +
                        apmntUtil.getStartTimeBasedOnEst(apmntData.getStart()));
                return false;
            }
            if (apmntUtil.isBetweenOpeningClosing(apmntData.getEnd())) {

                apmntUtil.showalert("The end time must be during business hours. Before " +
                        apmntUtil.getEndTimeBasedOnEst(apmntData.getEnd()));

                return false;
            }

            if(doesAppointmentOverlap(apmntData.getStart()))
              return  false;

            if(doesAppointmentOverlap(apmntData.getEnd()))
                return  false;

            if (apmntData.getCust_id() == 0) {
                apmntUtil.showalert("Please select a Customer to add an appointment for");
                return false;
            }

            if (apmntData.getContactId() == 0) {
                apmntUtil.showalert("Please select a Contact");
                return false;
            }
            if (apmntUtil.anEmptyTextField(lstTxtFields))
                return false;
            return !apmntUtil.hasMinChars(lstTxtFields);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Boolean doesAppointmentOverlap(Timestamp apmntTime) {

        Optional<Appointment> result =
                obsvAppnmt.getObsList().stream().filter(appointment -> (
                             apmntTime.after(appointment.getStart())  || apmntTime.equals(appointment.getStart()) )
                        && ( apmntTime.before(appointment.getEnd() ) || apmntTime.equals(appointment.getEnd())
                        )).findFirst();

        if(result.isEmpty())
            return false;
        apmntUtil.showalert( " appointment time " + apmntTime.toLocalDateTime() +
                "   overlaps existing appointment for Appointment_id=" + result.get().getAppointment_ID() + " times:"+
                result.get().getDateStart() + " to " +
                 result.get().getDateEnd()
                +
                " for " + result.get().getName());

        prt.printString.accept(  apmntTime.toLocalDateTime()+ " time overlaps " + result.get().getStart().toLocalDateTime()
        + " id="+ result.get().getAppointment_ID());
        return true;


    }

    private void addErrorChecking() {
        try {
            AddModifyAppointmentView.txtApmntTitle.setTextFormatter(formatterAlphaSpace);
            AddModifyAppointmentView.txtDescription.setTextFormatter(formatterAlphaNumericSpace1);
            AddModifyAppointmentView.txtLocation.setTextFormatter(formatterAlphaNumericSpace2);
            AddModifyAppointmentView.txtType.setTextFormatter(formatterAlphaNumericSpace3);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void clearAppointmentInputs() {


        try {
            AddModifyAppointmentView.txtApmntTitle.setText("");
            AddModifyAppointmentView.txtDescription.setText("");
            AddModifyAppointmentView.txtLocation.setText("");
            AddModifyAppointmentView.txtType.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void feedbackOnDBTransaction(Boolean statusOfDbTrans) {
        try {
            if (!statusOfDbTrans) {
                apmntUtil.showalert("An error occurred please check input and try again ");


            } else {
                String name = AddModifyAppointmentView.cmbxCustomers.getSelectionModel().getSelectedItem().getName();
                if (enumAddModify == AddModify.ADD)
                    apmntUtil.showalert("Appointment for " + name + " has been added to the database.");
                else
                    apmntUtil.showalert("Appointment for " + name + " has been modified in the database.");


                appointmentAddedOrModified = true;
                clearAppointmentInputs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void nameTextFields() {
        try {
            AddModifyAppointmentView.txtApmntTitle.setId("Title");
            AddModifyAppointmentView.txtDescription.setId("Description");
            AddModifyAppointmentView.txtLocation.setId("Location");
            AddModifyAppointmentView.txtType.setId("Type");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initArrayTextFields() {
        try {
            nameTextFields();
            apmntlstTxtFields = new ArrayList<>();
            putTextFieldsInArray(apmntlstTxtFields);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putSelectedAppointmentInModifyInput(Appointment anAppmnt) {

        try {
            AddModifyAppointmentView.txtApmntTitle.setText(anAppmnt.getTitle());
            AddModifyAppointmentView.txtDescription.setText(anAppmnt.getDescription());
            AddModifyAppointmentView.txtLocation.setText(anAppmnt.getLocation());
            AddModifyAppointmentView.txtType.setText(anAppmnt.getTypeOfAppointment());
            AddModifyAppointmentView.apmntID = anAppmnt.getAppointment_ID();

            setToCustomerInCombo(anAppmnt);
            setToContactInCombo(anAppmnt);
            setDatesInDatePickerModifyAnAppointment(anAppmnt);
            setTimesInSpinnerForModifyAnApmnt(anAppmnt);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void setDatesInDatePickerModifyAnAppointment(Appointment anAppmnt) {

        try {
            LocalDate startdate = util.convertToLocalDate(anAppmnt.getDateStart());
            LocalDate enddate = util.convertToLocalDate(anAppmnt.getDateEnd());

            viewAddModifyAppointment.dtpStart.setValue(startdate);
            viewAddModifyAppointment.dtpEnd.setValue(enddate);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setTimesInSpinnerForModifyAnApmnt(Appointment anAppmnt) {
        System.out.println("1Start date=" + anAppmnt.getDateStart() + "end date=" + anAppmnt.getDateEnd());

        LocalDateTime startdate = util.convertToLocalDateTime(anAppmnt.getDateStart());
        LocalDateTime enddate = util.convertToLocalDateTime(anAppmnt.getDateEnd());

        System.out.println("2Start date=" + startdate.toString() + "end date=" + enddate.toString());
        viewAddModifyAppointment.spnStart.getValueFactory().setValue(startdate.toLocalTime());
        viewAddModifyAppointment.spnEnd.getValueFactory().setValue(enddate.toLocalTime());

    }


    private void setToCustomerInCombo(Appointment anAppmnt) {

        try {
            Customer c = new Customer();
            c.setName(anAppmnt.getName());
            c.setCust_id(anAppmnt.getCust_id());
            AddModifyAppointmentView.cmbxCustomers.setValue(c);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setToContactInCombo(Appointment anAppmnt) {
        try {
            Contact c = new Contact();
            c.setContactName(anAppmnt.getContactName());
            c.setContactId(anAppmnt.getContactId());
            viewAddModifyAppointment.cmbxContacts.setValue(c);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void clearInputs() {
        try {
            AddModifyAppointmentView.txtApmntTitle.setText("");
            AddModifyAppointmentView.txtDescription.setText("");
            AddModifyAppointmentView.txtLocation.setText("");
            AddModifyAppointmentView.txtType.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void putTextFieldsInArray(ArrayList<TextField> lstTxtFields) {


        try {
            lstTxtFields.add(AddModifyAppointmentView.txtApmntTitle);
            lstTxtFields.add(AddModifyAppointmentView.txtDescription);
            lstTxtFields.add(AddModifyAppointmentView.txtLocation);
            lstTxtFields.add(AddModifyAppointmentView.txtType);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public Boolean getAppointmentAddedOrModified() {
        return appointmentAddedOrModified;
    }


}
