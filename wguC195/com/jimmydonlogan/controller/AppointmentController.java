package com.jimmydonlogan.controller;

import com.jimmydonlogan.model.*;
import com.jimmydonlogan.util.*;


import com.jimmydonlogan.view.AppointmentView;


import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;


public class AppointmentController extends BaseController<Appointment> {

    private final AppointmentView appointmentView = new AppointmentView();
    private final CustomersAppointsUtils util = new CustomersAppointsUtils();
    private final AddModifyAppointment addModifyCtrl;
    private final Stage stage;
    private String sqlSelect;
    private static final ObsList<Appointment> obsvAppnmt = new ObsList<>();
    private static LocalDate minDate = LocalDate.now();
    private static LocalDate maxDate = LocalDate.now().plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
    private String strApointmentStartingSoon = null;
    private final Predicate<String> nonNullPredicate = Objects::nonNull;
    private final Print prt = new Print();


    public AppointmentController(ComboBox<Customer> cmbxCustomers, User user, Stage stage) {
        this.stage = stage;
        addModifyCtrl = new AddModifyAppointment(cmbxCustomers, user,obsvAppnmt);
        getDataForAppointmentTable();
        initMonthWeekAll();


    }


    private void getDataForAppointmentTable() {
        sqlSelect = "SELECT " +
                "c.Customer_Name,b.Contact_Name,a.Appointment_ID,a.Contact_ID,a.Customer_ID," +
                "a.Description,a.End,a.Last_Update,a.Location," +
                "a.Start,a.Title,a.Type as typeOfAppointment,a.User_ID,a.Created_By,a.Last_Updated_By " +
                " FROM appointments as a inner join customers as c on a.Customer_ID=c.Customer_ID inner join contacts as b " +
                " on b.Contact_ID=a.Contact_ID;";
        this.getDataInDB();
        this.putDbDataInObjectModelForTable();
    }

    public VBox getView() {


        VBox vbxAppointmentview = appointmentView.buildTable();

        addEvents();
        appointmentView.tblAppointment.setItems(obsvAppnmt.getObsList());
        return vbxAppointmentview;
    }

    public void populateAppointmentTable() {
        appointmentView.tblAppointment.getItems().clear();
        getDataForAppointmentTable();


        appointmentView.tblAppointment.setItems(obsvAppnmt.getObsList());

    }

    private void putDbDataInObjectModelForTable() {


        util.putAppointmentDataInObjectModel(obsvAppnmt);


        addLocalDateTime();
        checkIfAppointmentIsSoon();

        util.showalert(strApointmentStartingSoon);


    }

    private void checkIfAppointmentIsSoon() {
        if (!nonNullPredicate.test(strApointmentStartingSoon))
            strApointmentStartingSoon = " ";

        final Boolean[] boolApmntwithinRange = {false};
        AtomicInteger numberApmnts = new AtomicInteger();

        obsvAppnmt.getObsList().forEach(appointment -> {

            try {

                Boolean diffWithinrRange = isApointmentStartingSoon(appointment.getDateStart(),
                        appointment.getAppointment_ID());

                if (diffWithinrRange) {
                    numberApmnts.getAndIncrement();
                    prt.printString.accept("appointment date time in found diff:" + appointment.getDateStart());
                    strApointmentStartingSoon = strApointmentStartingSoon +

                            "\nappointment for Appointment_ID=" + appointment.getAppointment_ID() +" " +appointment.getName()
                            + " on " +  appointment.getDateStart().toString()+
                            " is starting in less than 15 minutes.";
                    boolApmntwithinRange[0] = true;
                }


            } catch (Exception e) {
                //e.printStackTrace();
            }
        });

        if (boolApmntwithinRange[0])
            strApointmentStartingSoon = "number of appointments starting soon:" + numberApmnts + strApointmentStartingSoon;
        else
            strApointmentStartingSoon = "There are no upcoming appointments that start within the next 15 minutes";

    }

    private void addLocalDateTime() {
        strApointmentStartingSoon = null;
        for (Appointment a : obsvAppnmt.getObsList()) {

            try {


                a.setDateStart(util.convertToCurrentTimeZone(a.getStart()));
                a.setDateEnd(util.convertToCurrentTimeZone(a.getEnd()));
                if (util.convertToLocalDate(a.getDateStart()).isAfter(maxDate)) {
                    maxDate = util.convertToLocalDate(a.getDateStart());

                }
                if (util.convertToLocalDate(a.getDateStart()).isBefore(minDate)) {
                    minDate = util.convertToLocalDate(a.getDateStart());

                }

            } catch (Exception e) {

                e.printStackTrace();

            }
        }
        minDate = minDate.with(TemporalAdjusters.firstDayOfMonth());
        maxDate = maxDate.with(TemporalAdjusters.firstDayOfNextMonth());
        prt.printString.accept("max date=" + maxDate);
        prt.printString.accept("min date=" + minDate);




    }

    private Boolean isApointmentStartingSoon(Date apmntStart, int appointment_id) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime ldtapmtn = util.convertToLocalDateTime( apmntStart);
        prt.printString.accept("isApointmentStartingSoon id=" + appointment_id + " start time local" + ldtapmtn.toLocalDate().toString() +
                " " +ldtapmtn.toLocalTime());


        long lDiffInMinutes = now.until(ldtapmtn, ChronoUnit.MINUTES);
        long lDiffInHours = now.until(ldtapmtn, ChronoUnit.HOURS);

        if (lDiffInMinutes > 0)
            prt.printString.accept("id=" + appointment_id +
                    " time hour diff between now and appointment is :" + now.toLocalDate() + " " + now.toLocalTime() +
                    " and " + apmntStart.toString()+
                    " is " + lDiffInHours);

        return lDiffInMinutes > 0 && lDiffInMinutes < 15;

    }

    private void initMonthWeekAll() {
        appointmentView.cmbxMonths.setConverter(new MonthYearConverter());
        addMonthYearToCombo();
        appointmentView.dtpWeeks.setVisible(false);
        appointmentView.cmbxMonths.setVisible(false);


    }

    private void addSelectionEventForWeekDatePicker() {
        EventHandler<ActionEvent> evntSelectedDate = e -> {


            System.out.println("date picker value" + appointmentView.dtpWeeks.getValue());
            ObservableList<Appointment> obsweek = util.filterListWeek(obsvAppnmt.getObsList(),
                    appointmentView.dtpWeeks.getValue());

            appointmentView.tblAppointment.setItems(obsweek);

        };

        appointmentView.dtpWeeks.setShowWeekNumbers(true);


        appointmentView.dtpWeeks.setOnAction(evntSelectedDate);

    }

    private void addMonthYearToCombo() {


        try {
            ObsList<MonthYear> obsMonthsYears = new ObsList<>();
            Map<String, Integer> apmntMap = new HashMap<>();
            int imap = 0;
            LocalDate start = minDate;

            LocalDate end = maxDate;


            MonthYear monthYear ;

            String month;

            int year;
            String monthAndYear;
            for (LocalDate adate = start; adate.isBefore(end); adate = adate.plusDays(1)) {
                monthYear = new MonthYear();


                month = adate.getMonth().toString();
                year = adate.getYear();

                monthAndYear = month + " " + year;

                if (apmntMap.containsKey(monthAndYear)) continue;


                apmntMap.put(monthAndYear, imap++);
                monthYear.setMonthYear(monthAndYear);
                monthYear.setDate(adate.with(TemporalAdjusters.firstDayOfMonth()));

                obsMonthsYears.addToList(monthYear);

            }

            appointmentView.cmbxMonths.setItems(obsMonthsYears.getObsList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addEvents() {


        addBtnAddEvent();
        addComboMonthYearSelectEvent();
        addBtnModifyEvent();
        addBtnDeleteEvent();
        addWeekMonthAllRadioBtnEvent();
        AddCloseWindowEventForAddModify();
        addSelectionEventForWeekDatePicker();


    }

    private void addComboMonthYearSelectEvent() {
        appointmentView.cmbxMonths.setOnAction((event) -> {
            try {



                ObservableList<Appointment> obsmonth = util.filterListMonth(obsvAppnmt.getObsList(),
                        appointmentView.cmbxMonths.getSelectionModel().getSelectedItem().getDate());

                appointmentView.tblAppointment.setItems(obsmonth);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    private void addWeekMonthAllRadioBtnEvent() {

        appointmentView.rdGrpSelectTableView.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (appointmentView.rdGrpSelectTableView.getSelectedToggle() != null &&
                    appointmentView.rdbWeek.isSelected()) {
                appointmentView.dtpWeeks.setVisible(true);
                appointmentView.cmbxMonths.setVisible(false);

            } else if (appointmentView.rdbMonth.isSelected()) {
                appointmentView.dtpWeeks.setVisible(false);
                appointmentView.cmbxMonths.setVisible(true);

            } else if (appointmentView.rdbAll.isSelected()) {
                appointmentView.dtpWeeks.setVisible(false);
                appointmentView.cmbxMonths.setVisible(false);
                appointmentView.tblAppointment.setItems(obsvAppnmt.getObsList());
            }


        });
    }

    private void addBtnDeleteEvent() {
        Tooltip tooltipDelete = new Tooltip("Select an Appointment record then click to delete");
        appointmentView.btnDeleteAppointment.setTooltip(tooltipDelete);

        appointmentView.btnDeleteAppointment.setOnAction(e -> {
            if (appointmentView.tblAppointment.getSelectionModel().getSelectedItem() == null) {
                appointmentView.dtpWeeks.setVisible(false);
                appointmentView.cmbxMonths.setVisible(false);
                appointmentView.tblAppointment.setItems(obsvAppnmt.getObsList());
                util.showalert("Please select an appointment to delete, then click delete.");
                return;
            }
            String title = appointmentView.tblAppointment.getSelectionModel().getSelectedItem().getTitle();
            String name = appointmentView.tblAppointment.getSelectionModel().getSelectedItem().getName();
            String dateApmnt;

            try {
                dateApmnt = appointmentView.tblAppointment.getSelectionModel().getSelectedItem().getStart().toString();
            } catch (Exception exception) {
                dateApmnt = " ";
            }


            if (util.confirmAndDelete("Appointment for " + name + " on " + dateApmnt + " " + title, stage))
                return;

            int rowsDeleted = util.deleteAppointment(appointmentView.tblAppointment.getSelectionModel().getSelectedItem().getAppointment_ID());
            util.callThreadSleep(1000L);
            populateAppointmentTable();
            if (rowsDeleted > 0)
                util.showalert("Appointment has been deleted.");


        });

    }

    private void addBtnModifyEvent() {

        Tooltip tooltipModify = new Tooltip("Select an Appointment record then click to modify");
        appointmentView.btnModifyAppointment.setTooltip(tooltipModify);


        appointmentView.btnModifyAppointment.setOnAction(e -> {
            if (appointmentView.tblAppointment.getSelectionModel().getSelectedItem() == null) {
                appointmentView.dtpWeeks.setVisible(false);
                appointmentView.cmbxMonths.setVisible(false);
                appointmentView.tblAppointment.setItems(obsvAppnmt.getObsList());
                util.showalert("Please select an Appointment to modify, then click modify.");
                return;
            }

            addModifyCtrl.enumAddModify = AddModify.MODIFY;
            addModifyCtrl.clearInputs();
            addModifyCtrl.putSelectedAppointmentInModifyInput(appointmentView.tblAppointment.getSelectionModel().getSelectedItem());

            addModifyCtrl.display();


        });
    }

    private void addBtnAddEvent() {

        Tooltip tooltipAdd = new Tooltip("Click on To Add an Appointment");
        appointmentView.btnAddAppointment.setTooltip(tooltipAdd);

        appointmentView.btnAddAppointment.setOnAction(e -> {

            appointmentView.dtpWeeks.setVisible(false);
            appointmentView.cmbxMonths.setVisible(false);
            appointmentView.tblAppointment.setItems(obsvAppnmt.getObsList());
            addModifyCtrl.enumAddModify = AddModify.ADD;
            addModifyCtrl.clearAppointmentInputs();
            addModifyCtrl.display();
        });
    }

    private void getDataInDB() {
        MySqlDb cDB = new MySqlDb();


        obsvAppnmt.setRsltFromDB(cDB.getDataFromWguDB(sqlSelect));

    }

    private void AddCloseWindowEventForAddModify() {
        try {
            addModifyCtrl.viewAddModifyAppointment.stgAddAppointmentForm.setOnCloseRequest((evt) -> {

                if (!addModifyCtrl.getAppointmentAddedOrModified())
                    return;

                util.callThreadSleep(2000L);
                System.out.println("getting data for table after close");
                populateAppointmentTable();

            });
        } catch (Exception e) {

            //noinspection ThrowablePrintedToSystemOut
            System.out.println(e);
        }
    }

}


