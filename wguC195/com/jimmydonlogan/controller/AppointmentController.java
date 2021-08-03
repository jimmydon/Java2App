package com.jimmydonlogan.controller;

import com.jimmydonlogan.model.*;
import com.jimmydonlogan.util.*;


import com.jimmydonlogan.view.AppointmentView;


import com.jimmydonlogan.view.ReportTitleMonthView;
import com.jimmydonlogan.view.ReportTypeMonthView;
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


/**
 * The type Appointment controller.
 */
public class AppointmentController extends BaseController<Appointment> {

    /**
     * The Appointment view.
     */
    private final AppointmentView appointmentView = new AppointmentView();
    /**
     * The Report type month view.
     */
    private final ReportTypeMonthView reportTypeMonthView = new ReportTypeMonthView();
    /**
     * The Report title month view.
     */
    private final ReportTitleMonthView reportTitleMonthView = new ReportTitleMonthView();
    /**
     * The Util.
     */
    private final CustomersAppointsUtils util = new CustomersAppointsUtils();
    /**
     * The Add modify ctrl.
     */
    private final AddModifyAppointment addModifyCtrl;
    /**
     * The Stage.
     */
    private final Stage stage;
    /**
     * The Sql select.
     */
    private String sqlSelect;
    /**
     * The Obsv appnmt.
     */
    private final ObsList<Appointment> obsvAppnmt = new ObsList<>();
    /**
     * The Obsv reprt type month.
     */
    private ObsList<ReportTypeMonth> obsvReprtTypeMonth;
    /**
     * The Obsv reprt title month.
     */
    private ObsList<ReportTitleMonth> obsvReprtTitleMonth;
    /**
     * The Min date.
     */
    private LocalDate minDate = LocalDate.now();
    /**
     * The Max date.
     */
    private LocalDate maxDate = LocalDate.now().plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
    /**
     * The Str apointment starting soon.
     */
    private String strApointmentStartingSoon = null;
    /**
     * The Non null predicate.
     */
    private final Predicate<String> nonNullPredicate = Objects::nonNull;
    /**
     * The Prt.
     */
    private final Print prt = new Print();
    private final CustomerAppointBean beanNotifyCancelAddAppointment = new CustomerAppointBean();
    private final static Long WINDOWTOAPPOINTMENT=15L;


    /**
     * Instantiates a new Appointment controller.
     *
     * @param cmbxCustomers the cmbx customers
     * @param user          the user
     * @param stage         the stage
     */
    public AppointmentController(ComboBox<Customer> cmbxCustomers, User user, Stage stage) {

        try {


            getDataForAppointmentTable();
            initMonthWeekAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.stage = stage;
            addModifyCtrl = new AddModifyAppointment(cmbxCustomers, user, obsvAppnmt, beanNotifyCancelAddAppointment);
        }


    }


    /**
     * Gets data for appointment table.
     */
    private void getDataForAppointmentTable() {
        try {
            sqlSelect = "SELECT " +
                    "c.Customer_Name,b.Contact_Name,a.Appointment_ID,a.Contact_ID,a.Customer_ID," +
                    "a.Description,a.End,a.Last_Update,a.Location," +
                    "a.Start,a.Title,a.Type as typeOfAppointment,a.User_ID,a.Created_By,a.Last_Updated_By " +
                    " FROM appointments as a inner join customers as c on a.Customer_ID=c.Customer_ID inner join contacts as b " +
                    " on b.Contact_ID=a.Contact_ID;";
            this.getDataInDB();
            this.putDbDataInObjectModelForTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets view.
     *
     * @return the view
     */
    public VBox getView() {


        try {
            VBox vbxAppointmentview = appointmentView.buildTable();

            addEvents();
            appointmentView.tblAppointment.setItems(obsvAppnmt.getObsList());
            return vbxAppointmentview;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Populate appointment table.
     */
    public void populateAppointmentTable() {
        try {
            appointmentView.tblAppointment.getItems().clear();
            getDataForAppointmentTable();


            appointmentView.tblAppointment.setItems(obsvAppnmt.getObsList());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Put db data in object model for table.
     */
    private void putDbDataInObjectModelForTable() {


        try {
            util.putAppointmentDataInObjectModel(obsvAppnmt);


            addLocalDateTime();
            checkIfAppointmentIsSoon();

            util.showAlert(strApointmentStartingSoon);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Check if appointment is soon.
     * using lambda for obsvAppnmt.getObsList().forEach:concise code,avoid using bulky anonymous class implementation
     */
    private void checkIfAppointmentIsSoon() {
        if (!nonNullPredicate.test(strApointmentStartingSoon))
            strApointmentStartingSoon = " ";

        final Boolean[] boolApmntwithinRange = {false};
        AtomicInteger numberApmnts = new AtomicInteger();

        obsvAppnmt.getObsList().forEach(appointment -> {

            try {

                Boolean diffWithinRange = isApointmentStartingSoon(appointment.getDateStart(),
                        appointment.getAppointment_ID(),WINDOWTOAPPOINTMENT);

                if (diffWithinRange) {
                    numberApmnts.getAndIncrement();
                    prt.printString.accept("appointment date time in found diff:" + appointment.getDateStart());
                    strApointmentStartingSoon = strApointmentStartingSoon +

                            "\nappointment for Appointment_ID=" + appointment.getAppointment_ID() + " " + appointment.getName()
                            + " on " + appointment.getDateStart().toString() +
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

    /**
     * Add local date time.
     */
    private void addLocalDateTime() {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Is apointment starting soon boolean.
     *
     * @param apmntStart     the apmnt start
     * @param appointment_id the appointment id
     * @return the boolean
     */
    private Boolean isApointmentStartingSoon(Date apmntStart, int appointment_id, Long timewindow) {

        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime ldtapmtn = util.convertToLocalDateTime(apmntStart);


            long lDiffInMinutes = now.until(ldtapmtn, ChronoUnit.MINUTES);
            //prt.printString.accept( "apmntid=" + appointment_id + " diff in mins="+ lDiffInMinutes);

            return lDiffInMinutes >= 0 && lDiffInMinutes <= timewindow;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * Init month week all.
     */
    private void initMonthWeekAll() {
        try {
            appointmentView.cmbxMonths.setConverter(new MonthYearConverter());
            addMonthYearToCombo();
            appointmentView.dtpWeeks.setVisible(false);
            appointmentView.cmbxMonths.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Add selection event for week date picker.
     * using Lambda Expression for  evntSelectedDate:lambdas help in writing smaller and cleaner code by removing a lot of boiler-plate code
     * functional interfaces can us lambda expressions to instantiate them and
     * avoid using bulky anonymous class implementation
     */
    private void addSelectionEventForWeekDatePicker() {
        try {
            EventHandler<ActionEvent> evntSelectedDate = e -> {


                System.out.println("date picker value" + appointmentView.dtpWeeks.getValue());
                ObservableList<Appointment> obsweek = util.filterListWeek(obsvAppnmt.getObsList(),
                        appointmentView.dtpWeeks.getValue());

                appointmentView.tblAppointment.setItems(obsweek);

            };

            appointmentView.dtpWeeks.setShowWeekNumbers(true);


            appointmentView.dtpWeeks.setOnAction(evntSelectedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Add month year to combo.
     */
    private void addMonthYearToCombo() {


        try {
            ObsList<MonthYear> obsMonthsYears = new ObsList<>();
            Map<String, Integer> apmntMap = new HashMap<>();
            int imap = 0;
            LocalDate start = minDate;

            LocalDate end = maxDate;


            MonthYear monthYear;

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

    /**
     * Add events.
     */
    private void addEvents() {


        try {
            addBtnAddEvent();
            addComboMonthYearSelectEvent();
            addBtnModifyEvent();
            addBtnDeleteEvent();
            addWeekMonthAllRadioBtnEvent();
            AddCloseWindowEventForAddModify();
            addSelectionEventForWeekDatePicker();
            addReportBtnEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Add combo month year select event. using lambdas for events lambdas allow for smaller concise better performing code
     */
    private void addComboMonthYearSelectEvent() {
        try {
            appointmentView.cmbxMonths.setOnAction((event) -> {
                try {


                    ObservableList<Appointment> obsmonth = util.filterListMonth(obsvAppnmt.getObsList(),
                            appointmentView.cmbxMonths.getSelectionModel().getSelectedItem().getDate());

                    appointmentView.tblAppointment.setItems(obsmonth);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add week month all radio btn event. using lambdas for all listeners, they allow for better forming
     * concise code
     */
    private void addWeekMonthAllRadioBtnEvent() {

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add btn delete event. adding lambda for concise better performing code
     */
    private void addBtnDeleteEvent() {
        try {
            Tooltip tooltipDelete = new Tooltip("Select an Appointment record then click to delete");
            appointmentView.btnDeleteAppointment.setTooltip(tooltipDelete);

            appointmentView.btnDeleteAppointment.setOnAction(e -> {
                if (appointmentView.tblAppointment.getSelectionModel().getSelectedItem() == null) {
                    appointmentView.dtpWeeks.setVisible(false);
                    appointmentView.cmbxMonths.setVisible(false);
                    appointmentView.tblAppointment.setItems(obsvAppnmt.getObsList());
                    util.showAlert("Please select an appointment to delete, then click delete.");
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
                int apmntID = appointmentView.tblAppointment.getSelectionModel().getSelectedItem().getAppointment_ID();
                String apmntType=appointmentView.tblAppointment.getSelectionModel().getSelectedItem().getTypeOfAppointment();

                int rowsDeleted = util.deleteAppointment(apmntID);
                util.callThreadSleep(1000L);
                populateAppointmentTable();
                if (rowsDeleted > 0)
                    util.showAlert("Appointment id=" + apmntID + " type="+ apmntType  + " has been deleted.");


            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Add btn modify event.
     * Functional Interfaces and Lambda Expressions allow for
     * smaller and cleaner code by removing a lot of boiler-plate code
     * a huge benefit of functional interfaces is that we can use
     * lambda expressions to instantiate them and avoid using anonymous class implementation
     */
    private void addBtnModifyEvent() {

        try {
            Tooltip tooltipModify = new Tooltip("Select an Appointment record then click to modify");
            appointmentView.btnModifyAppointment.setTooltip(tooltipModify);


            appointmentView.btnModifyAppointment.setOnAction(e -> {
                if (appointmentView.tblAppointment.getSelectionModel().getSelectedItem() == null) {
                    appointmentView.dtpWeeks.setVisible(false);
                    appointmentView.cmbxMonths.setVisible(false);
                    appointmentView.tblAppointment.setItems(obsvAppnmt.getObsList());
                    util.showAlert("Please select an Appointment to modify, then click modify.");
                    return;
                }

                addModifyCtrl.enumAddModify = AddModify.MODIFY;
                addModifyCtrl.clearInputs();
                addModifyCtrl.putSelectedAppointmentInModifyInput(appointmentView.tblAppointment.getSelectionModel().getSelectedItem());

                addModifyCtrl.display();


            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add report btn events.
     */
    private void addReportBtnEvents() {
        try {
            addEventTypeMonth();
            addEventContactSchedule();
            addEventTitleMonth();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add event title month.Functional Interfaces and Lambda Expressions
     * allow for smaller and cleaner code by removing a lot of boiler-plate code
     * The major benefit of java functional interfaces is the use of lambda expressions
     * to instantiate them and avoid using anonymous class implementation
     */
    private void addEventTitleMonth() {

        try {
            appointmentView.btnReportTitleMonth.setOnAction(e -> {


                generateReportTitleMonthTotal();
                reportTitleMonthView.buildReportTitleMonthForm();


            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add event type month. using lambda: concise efficient code implementation of the functional interface
     */
    private void addEventTypeMonth() {
        try {
            appointmentView.btnReportCustApmnts.setOnAction(e -> {


                generateReportTypeMonthTotal();
                reportTypeMonthView.buildReportTypeMonthForm();


            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Add event contact schedule.
     */
    public void addEventContactSchedule() {
        try {
            initContactComboForReport();
            addBtnEventGetContactSchedule();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Add btn event get contact schedule. using lambda to implement functional interface,allows  concise efficient code
     */
    private void addBtnEventGetContactSchedule() {


        try {
            appointmentView.btnReportContact.setOnAction(e -> {

                if (appointmentView.cmbxRptContacts.getSelectionModel().getSelectedItem() == null) {
                    util.showAlert("Please select a contact");
                    return;
                }

                String contactName = appointmentView.cmbxRptContacts.getSelectionModel().getSelectedItem().getContactName();
                Integer contactID = appointmentView.cmbxRptContacts.getSelectionModel().getSelectedItem().getContactId();


                ObservableList<Appointment> obsContact = util.filterListContact(obsvAppnmt.getObsList(),
                        contactID);

                appointmentView.tblAppointment.setItems(obsContact);


            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Add btn add event.  Functional Interfaces allow for Lambda Expressions that
     * allow for writing smaller and cleaner code by removing a lot of boiler-plate code
     */
    private void addBtnAddEvent() {

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets data in db.
     */
    private void getDataInDB() {
        try {
            MySqlDb cDB = new MySqlDb();


            obsvAppnmt.setRsltFromDB(cDB.getDataFromWguDB(sqlSelect));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Add close window event for add modify. using lambda for its better performance and cleaner code
     */
    private void AddCloseWindowEventForAddModify() {
        try {
            addModifyCtrl.viewAddModifyAppointment.stgAddAppointmentForm.setOnCloseRequest((evt) -> {

                if (!addModifyCtrl.getAppointmentAddedOrModified())
                    return;

                util.callThreadSleep(2000L);

                populateAppointmentTable();

            });
        } catch (Exception e) {

            //noinspection ThrowablePrintedToSystemOut
            System.out.println(e);
        }
    }

    /**
     * Generate report title month total.
     */
    private void generateReportTitleMonthTotal() {
        try {
            ReportTitleMonth rptTitleMonth;
            obsvReprtTitleMonth = new ObsList<>();
            String month;

            String title;
            String key;
            int typeCountForMonth = 0;
            Map<String, String> mapTitleMonth = new HashMap<>();
            int totalOfAllTitles = 0;

            for (Appointment appointment : obsvAppnmt.getObsList()) {

                try {

                    month = appointment.getStart().toLocalDateTime().getMonth().toString();

                    title = appointment.getTitle();
                    key = month + title;

                    if (mapTitleMonth.containsKey(key))
                        continue;
                    rptTitleMonth = new ReportTitleMonth();
                    mapTitleMonth.put(key, title);
                    typeCountForMonth = getCountForMonthOfTitle(month, title);
                    totalOfAllTitles = totalOfAllTitles + typeCountForMonth;
                    rptTitleMonth.setTitle(title);
                    rptTitleMonth.setMonth(month);
                    rptTitleMonth.setTotalMonth(typeCountForMonth);
                    obsvReprtTitleMonth.addToList(rptTitleMonth);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            rptTitleMonth = new ReportTitleMonth();
            rptTitleMonth.setTitle("Total");
            rptTitleMonth.setMonth(" ");
            rptTitleMonth.setTotalMonth(totalOfAllTitles);
            obsvReprtTitleMonth.addToList(rptTitleMonth);

            reportTitleMonthView.tblTitleMonth.getItems().clear();

            reportTitleMonthView.tblTitleMonth.setItems(obsvReprtTitleMonth.getObsList());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Gets count for month of title.
     *
     * @param month the month
     * @param title the title
     * @return the count for month of title
     */
    private int getCountForMonthOfTitle(String month, String title) {
        try {
            int count = 0;
            String apmntMonth;
            String apmntTitle;


            for (Appointment appointment : obsvAppnmt.getObsList()) {
                apmntMonth = appointment.getStart().toLocalDateTime().getMonth().toString();
                apmntTitle = appointment.getTitle();
                if (!month.equals(month) || !title.equals(apmntTitle))
                    continue;

                if (month.equals(apmntMonth) && title.equals(apmntTitle))
                    count++;

            }

            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Generate report type month total.
     */
    private void generateReportTypeMonthTotal() {
        try {
            ReportTypeMonth rptTypeMonth;
            obsvReprtTypeMonth = new ObsList<>();
            String month;

            String type;
            String key;
            int typeCountForMonth = 0;
            Map<String, String> mapTypeMonth = new HashMap<>();
            int totalOfAllTypes = 0;

            for (Appointment appointment : obsvAppnmt.getObsList()) {

                try {

                    month = appointment.getStart().toLocalDateTime().getMonth().toString();

                    type = appointment.getTypeOfAppointment();
                    key = month + type;

                    if (mapTypeMonth.containsKey(key))
                        continue;
                    rptTypeMonth = new ReportTypeMonth();
                    mapTypeMonth.put(key, type);
                    typeCountForMonth = getCountForMonthOfType(month, type);
                    totalOfAllTypes = totalOfAllTypes + typeCountForMonth;
                    rptTypeMonth.setType(type);
                    rptTypeMonth.setMonth(month);
                    rptTypeMonth.setTotalMonth(typeCountForMonth);
                    obsvReprtTypeMonth.addToList(rptTypeMonth);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            rptTypeMonth = new ReportTypeMonth();
            rptTypeMonth.setType("Total");
            rptTypeMonth.setMonth(" ");
            rptTypeMonth.setTotalMonth(totalOfAllTypes);
            obsvReprtTypeMonth.addToList(rptTypeMonth);

            reportTypeMonthView.tblTypeMonth.getItems().clear();

            reportTypeMonthView.tblTypeMonth.setItems(obsvReprtTypeMonth.getObsList());
            prt.printString.accept("done generating report total is " + totalOfAllTypes);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Gets count for month of type.
     *
     * @param month the month
     * @param type  the type
     * @return the count for month of type
     */
    private int getCountForMonthOfType(String month, String type) {
        try {
            int count = 0;
            String apmntMonth;
            String apmntType;


            for (Appointment appointment : obsvAppnmt.getObsList()) {
                apmntMonth = appointment.getStart().toLocalDateTime().getMonth().toString();
                apmntType = appointment.getTypeOfAppointment();
                if (!month.equals(month) || !type.equals(apmntType))
                    continue;

                if (month.equals(apmntMonth) && type.equals(apmntType))
                    count++;

            }

            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Init contact combo for report.
     */
    private void initContactComboForReport() {

        try {
            ObsList<Contact> obsContact = new ObsList<>();
            AddCountryDivContactsToObsLst addRecordsToLst = new AddCountryDivContactsToObsLst();
            addRecordsToLst.getContactsForCombo(obsContact, appointmentView.cmbxRptContacts);

            addRecordsToLst.putDataInContactList(obsContact);
            addBeanToListenForAppointmentCancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * using lambda to implement functional interface, allows for better performance, cleaner concise code
     */
    private void addBeanToListenForAppointmentCancel() {
        try { ///listening for cancel form add modify ...case for adding new customer then closing via cancel need to poplate table
            beanNotifyCancelAddAppointment.addPropertyChangeListener(e ->     // lambda expression


                            populateAppointmentTable()
                    // System.out.println(e.getNewValue())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


