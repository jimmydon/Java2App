package com.jimmydonlogan.controller;


import com.jimmydonlogan.exceptions.AddRecordException;
import com.jimmydonlogan.model.*;
import com.jimmydonlogan.util.*;
import com.jimmydonlogan.view.AddModifyAppointmentView;
import javafx.scene.control.*;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * The type Add modify appointment.
 */
public class AddModifyAppointment implements MakeAddUpdateView {


    /**
     * The Btn cancel.
     */
    private final Button btnCancel = new Button("Cancel");
    /**
     * The Btn save.
     */
    private final Button btnSave = new Button("Save");
    /**
     * The View add modify appointment.
     */
    public final AddModifyAppointmentView viewAddModifyAppointment;
    /**
     * The Apmnt util.
     */
    private final CustomersAppointsUtils apmntUtil = new CustomersAppointsUtils();
    /**
     * The Appointment added or modified.
     */
    private Boolean appointmentAddedOrModified;
    private final CustomerAppointBean beanNotifyCancelAddAppointment;

    /**
     * The Enum add modify.
     */
    public AddModify enumAddModify;

    /**
     * The User.
     */
    private final User user;
    /**
     * The Apmntlst txt fields.
     */
    private ArrayList<TextField> apmntlstTxtFields;
    /**
     * The Apmnt data.
     */
    Appointment apmntData;
    /**
     * The Util.
     */
    private final CustomersAppointsUtils util = new CustomersAppointsUtils();
    /**
     * The Formatter alpha space.
     */
    private final TextFormatter<String> formatterAlphaSpace = new TextFormatter<>(util.filterAlphaSpace);
    /**
     * The Formatter alpha numeric space 1.
     */
    private final TextFormatter<String> formatterAlphaNumericSpace1 = new TextFormatter<>(util.filterAlphaNumericSpace);
    /**
     * The Formatter alpha numeric space 2.
     */
    private final TextFormatter<String> formatterAlphaNumericSpace2 = new TextFormatter<>(util.filterAlphaNumericSpace);
    /**
     * The Formatter alpha numeric space 3.
     */
    private final TextFormatter<String> formatterAlphaNumericSpace3 = new TextFormatter<>(util.filterAlphaNumericSpace);
    /**
     * The Obsv appnmt.
     */
    private final ObsList<Appointment> obsvAppnmt;
    /**
     * The Prt.
     */
    private final Print prt = new Print();

    /**
     * Instantiates a new Add modify appointment.
     *
     * @param cmbxCustomers the cmbx customers
     * @param user          the user
     * @param obsvAppnmt    the obsv appnmt
     */
    public AddModifyAppointment(ComboBox<Customer> cmbxCustomers, User user, ObsList<Appointment> obsvAppnmt, CustomerAppointBean beanNotifyCancelAddAppointment) {
        try {

        } finally {
            this.beanNotifyCancelAddAppointment = beanNotifyCancelAddAppointment;
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
    }

    /**
     * Display.
     */
    public void display() {

        try {
            viewAddModifyAppointment.addOrModify = enumAddModify;
            appointmentAddedOrModified = false;

            viewAddModifyAppointment.buildAddAppointmentForm();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Add events.
     */
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

    /**
     * Add date picker events.
     * using lambda expression to instantiate  DatePickerEvents and avoid using bulky anonymous class implementation
     * also smaller cleaner code
     */
    private void addDatePickerEvents() {

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add btn save event.
     * using lambda expression to instantiate btnSave and avoid using bulky anonymous class implementation
     * * also smaller cleaner code
     */
    private void addBtnSaveEvent() {
        try {
            btnSave.setOnAction((event) -> addOrModifyAppointmentInDB(apmntlstTxtFields));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add btn cancel event.
     * using lambda expression to instantiate btnCancel and avoid using bulky anonymous class implementation
     * also smaller cleaner code
     */
    private void addBtnCancelEvent() {
        try {
            btnCancel.setOnAction(e -> cancelNotifyClose());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelNotifyClose() {

        beanNotifyCancelAddAppointment.setValue(1);
        viewAddModifyAppointment.stgAddAppointmentForm.close();
    }

    /**
     * Do add or modify in db boolean.
     *
     * @return the boolean
     */
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

    /**
     * Add or modify appointment in db.
     *
     * @param lstTxtFields the lst txt fields
     */
    private void addOrModifyAppointmentInDB(ArrayList<TextField> lstTxtFields) {



        if(viewAddModifyAppointment.dtpEnd.getValue()==null || viewAddModifyAppointment.dtpStart==null)
        {
            util.showAlert("Select an appointment start and end date and time");
            return;
        }


        apmntData = apmntUtil.putInputDataInAppointmentObject(viewAddModifyAppointment);
        try {
            apmntData.setContactId(viewAddModifyAppointment.cmbxContacts.getSelectionModel().getSelectedItem().getContactId());
        } catch (Exception e) {
            util.showAlert("Select a Contact");
        }
        try {
            apmntData.setCust_id(AddModifyAppointmentView.cmbxCustomers.getSelectionModel().getSelectedItem().getCust_id());
        } catch (Exception e) {
            util.showAlert("Select a Customer");
        }
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

    /**
     * Check inputs boolean.
     *
     * @param lstTxtFields the lst txt fields
     * @return the boolean
     */
    private Boolean checkInputs(ArrayList<TextField> lstTxtFields) {


        try {
            if (!apmntUtil.timeDateSelected(viewAddModifyAppointment.dtpStart, "start"))
                return false;
            if (!apmntUtil.timeDateSelected(viewAddModifyAppointment.dtpEnd, "end"))
                return false;


            if (!apmntUtil.dateTimesAreValid(apmntData.getStart(), apmntData.getEnd()))
                return false;

            if (apmntUtil.isBetweenOpeningClosing(apmntData.getStart())) {

                apmntUtil.showAlert("The start time must be during business hours. After " +
                        apmntUtil.getStartTimeBasedOnEst(apmntData.getStart()));
                return false;
            }
            if (apmntUtil.isBetweenOpeningClosing(apmntData.getEnd())) {

                apmntUtil.showAlert("The end time must be during business hours. Before " +
                        apmntUtil.getEndTimeBasedOnEst(apmntData.getEnd()));

                return false;
            }

            if (doesAppointmentOverlap(apmntData.getStart()))
                return false;

            if (doesAppointmentOverlap(apmntData.getEnd()))
                return false;

            if (apmntData.getCust_id() == 0) {
                apmntUtil.showAlert("Please select a Customer to add an appointment for");
                return false;
            }

            if (apmntData.getContactId() == 0) {
                apmntUtil.showAlert("Please select a Contact");
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

    /**
     * Does appointment overlap boolean.
     *
     * @param aapmntTime the apmnt time
     * @return the boolean
     *
     *
     */
    private Boolean doesAppointmentOverlap(Timestamp aapmntTime) {

        try {
            Timestamp apmntTime=util.convertTimeStampToUTC(aapmntTime);



            for (Appointment appointment : obsvAppnmt.getObsList()) {


                prt.printString.accept(" id=" +  appointment.getAppointment_ID()
                        +" testing overlap in db start is="+appointment.getStart() +
                       " comparing with apmnt time of "+ apmntTime);


                if (apmntTime.equals(appointment.getStart())) {
                    prt.printString.accept("1match found");
                    overlapMsg(apmntTime, appointment);

                    return true;
                }
                if (apmntTime.equals(appointment.getEnd())) {
                    prt.printString.accept("2match found");
                    overlapMsg(apmntTime, appointment);
                    return true;
                }

                if (apmntTime.after(appointment.getStart()) && apmntTime.before(appointment.getEnd())) {
                    prt.printString.accept("3 match found");
                    overlapMsg(apmntTime, appointment);
                    return true;
                }



            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;


    }

    private void overlapMsg(Timestamp apmntTime, Appointment result) {

        try {
            apmntUtil.showAlert(" appointment time "+ util.convertToCurrentTimeZone(apmntTime) +
                    " overlaps existing appointment for Appointment_id=" +
                    result.getAppointment_ID() + " times:" +
                    result.getDateStart() + " to " +
                    result.getDateEnd()
                    +
                    " for " + result.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Add error checking.
     */
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

    /**
     * Clear appointment inputs.
     */
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

    /**
     * Feedback on db transaction.
     *
     * @param statusOfDbTrans the status of db trans
     */
    private void feedbackOnDBTransaction(Boolean statusOfDbTrans) {
        try {
            if (!statusOfDbTrans) {
                apmntUtil.showAlert("An error occurred please check input and try again ");


            } else {
                String name = AddModifyAppointmentView.cmbxCustomers.getSelectionModel().getSelectedItem().getName();
                if (enumAddModify == AddModify.ADD)
                    apmntUtil.showAlert("Appointment for " + name + " has been added to the database.");
                else
                    apmntUtil.showAlert("Appointment for " + name + " has been modified in the database.");


                appointmentAddedOrModified = true;
                clearAppointmentInputs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Name text fields.
     */
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

    /**
     * Init array text fields.
     */
    private void initArrayTextFields() {
        try {
            nameTextFields();
            apmntlstTxtFields = new ArrayList<>();
            putTextFieldsInArray(apmntlstTxtFields);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Put selected appointment in modify input.
     *
     * @param anAppmnt the an appmnt
     */
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


    /**
     * Sets dates in date picker modify an appointment.
     *
     * @param anAppmnt the an appmnt
     */
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

    /**
     * Sets times in spinner for modify an apmnt.
     *
     * @param anAppmnt the an appmnt
     */
    private void setTimesInSpinnerForModifyAnApmnt(Appointment anAppmnt) {
        try {

            LocalDateTime startdate = util.convertToLocalDateTime(anAppmnt.getDateStart());
            LocalDateTime enddate = util.convertToLocalDateTime(anAppmnt.getDateEnd());


            viewAddModifyAppointment.spnStart.getValueFactory().setValue(startdate.toLocalTime());
            viewAddModifyAppointment.spnEnd.getValueFactory().setValue(enddate.toLocalTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Sets to customer in combo.
     *
     * @param anAppmnt the an appmnt
     */
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

    /**
     * Sets to contact in combo.
     *
     * @param anAppmnt the an appmnt
     */
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

    /**
     * Clear inputs.
     */
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

    /**
     * Put text fields in array.
     *
     * @param lstTxtFields the lst txt fields
     */
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

    /**
     * Gets appointment added or modified.
     *
     * @return the appointment added or modified
     */
    public Boolean getAppointmentAddedOrModified() {
        return appointmentAddedOrModified;
    }


}
