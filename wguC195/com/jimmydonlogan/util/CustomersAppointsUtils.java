package com.jimmydonlogan.util;

import com.jimmydonlogan.exceptions.AddRecordException;
import com.jimmydonlogan.model.*;
import com.jimmydonlogan.view.AddModifyAppointmentView;
import com.jimmydonlogan.view.AddModifyCustomerView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.Date;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class CustomersAppointsUtils {

    private static final Logger LOGGER = Logger.getLogger(CustomersAppointsUtils.class.getName());
    private final Pattern patternAlphaSpaces = Pattern.compile("[a-zA-Z ]*");

    private final Pattern patternAlphaNumericSpaces = Pattern.compile("^[a-zA-Z0-9 ]*$");
    private final Pattern patternAlphaNumericNoSpace = Pattern.compile("^[a-zA-Z0-9]*$");
    private final Predicate<String> nonNullPredicate = Objects::nonNull;
    private final Print prt = new Print();


    final MySqlDb mySqldb;

    {
        mySqldb = new MySqlDb();
    }


    public void putAppointmentDataInObjectModel(ObsList<Appointment> obsActions) {
        Appointment aRecord;

        try {

            while (obsActions.getRsltFromDB().next()) {
                aRecord = new Appointment();
                aRecord.setName(obsActions.getRsltFromDB().getString("Customer_Name"));
                aRecord.setAppointment_ID(obsActions.getRsltFromDB().getInt("Appointment_ID"));
                aRecord.setTypeOfAppointment(obsActions.getRsltFromDB().getString("typeOfAppointment"));
                aRecord.setContactId(obsActions.getRsltFromDB().getInt("Contact_ID"));

                aRecord.setDescription(obsActions.getRsltFromDB().getString("Description"));
                aRecord.setStart(obsActions.getRsltFromDB().getTimestamp("Start"));
                aRecord.setEnd(obsActions.getRsltFromDB().getTimestamp("End"));

                aRecord.setContactName(obsActions.getRsltFromDB().getString("Contact_Name"));
                aRecord.setTitle(obsActions.getRsltFromDB().getString("Title"));
                aRecord.setCust_id(obsActions.getRsltFromDB().getInt("Customer_ID"));
                aRecord.setLocation(obsActions.getRsltFromDB().getString("Location"));
                aRecord.setUser_ID(obsActions.getRsltFromDB().getInt("User_ID"));
                obsActions.addToList(aRecord);


            }


        } catch (Exception err) {

            throw new AddRecordException(
                    "the following DP operation  failed : ", err);
        }
    }

    public void putCustomerDataInObjectModel(ObsList<Customer> obsActions) {
        Customer customer;
        ResultSet rsltset = obsActions.getRsltFromDB();

        try {
            while (rsltset.next()) {
                customer = new Customer();
                customer.setDivision_id(obsActions.getRsltFromDB().getInt("Division_ID"));
                customer.setCust_id(obsActions.getRsltFromDB().getInt("Customer_ID"));
                customer.setName(obsActions.getRsltFromDB().getString(("Customer_Name")));
                //prt.printString.accept("name of customer:" + rsltFromDB.getString(("Customer_Name")));
                customer.setAddress(obsActions.getRsltFromDB().getString(("Address")));
                customer.setPostal_code(obsActions.getRsltFromDB().getString(("Postal_Code")));
                customer.setPhone(obsActions.getRsltFromDB().getString(("Phone")));
                obsActions.addToList(customer);


            }


        } catch (Exception err) {

            throw new AddRecordException(
                    "the following DP operation  failed : ", err);
        }
    }

    public Customer putInputDataInCustomerObject(int divID) {
        try {
            Customer cst = new Customer();
            cst.setName(AddModifyCustomerView.txtName.getText());
            cst.setAddress(AddModifyCustomerView.txtAddress.getText());
            cst.setPhone(AddModifyCustomerView.txtPhone.getText());
            cst.setPostal_code(AddModifyCustomerView.txtPostalCode.getText());
            cst.setCust_id(AddModifyCustomerView.custid);
            cst.setDivision_id(divID);
            return cst;
        } catch (Exception err) {

            throw new AddRecordException(
                    "the following DP operation  failed : ", err);
        }

    }


    public Appointment putInputDataInAppointmentObject(AddModifyAppointmentView vwAc) {
        try {


            Appointment apmnt = new Appointment();
            LocalDateTime dtlocalStart = getLocalDateTime(vwAc.dtpStart.getValue(),
                    vwAc.spnStart.getValue());

            prt.printString.accept("vwAc.dtpStart.getValue()=" + vwAc.dtpStart.getValue());

            LocalDateTime dtlocalEnd = getLocalDateTime(vwAc.dtpEnd.getValue(),
                    vwAc.spnEnd.getValue());


            apmnt.setTitle(AddModifyAppointmentView.txtApmntTitle.getText());
            apmnt.setStart(Timestamp.valueOf(dtlocalStart));
            apmnt.setEnd(Timestamp.valueOf(dtlocalEnd));
            apmnt.setDescription(AddModifyAppointmentView.txtDescription.getText());
            apmnt.setLocation(AddModifyAppointmentView.txtLocation.getText());
            apmnt.setTypeOfAppointment(AddModifyAppointmentView.txtType.getText());
            apmnt.setAppointment_ID(AddModifyAppointmentView.apmntID);

            return apmnt;
        } catch (Exception err) {

            throw new AddRecordException(
                    "the following DP operation  failed : ", err);
        }

    }


    public boolean insertDataIntoAppointmentDBTable(Appointment anAppointment) {
        try {


            SimpleDateFormat dtformat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.US);

            LOGGER.info("start before utc convert:" + anAppointment.getStart().toLocalDateTime());
            LOGGER.info("end before utc convert:" + anAppointment.getEnd().toLocalDateTime());

            dtformat.setTimeZone(TimeZone.getTimeZone("UTC"));

            String start = dtformat.format(anAppointment.getStart());
            String end = dtformat.format(anAppointment.getEnd());
            LOGGER.info("start after utc convert:" + start);
            LOGGER.info("end after utc convert:" + end);

            //anAppointment.getStart().toInstant()


            String sqlInsert = makeInsertStatementForAppointment();
            PreparedStatement sqlPreparedStmt = mySqldb.appMySqlcon.prepareStatement(sqlInsert);
            sqlPreparedStmt.setString(1, anAppointment.getTitle());
            sqlPreparedStmt.setString(2, anAppointment.getDescription());

            sqlPreparedStmt.setString(3, anAppointment.getLocation());
            sqlPreparedStmt.setString(4, anAppointment.getTypeOfAppointment());
            sqlPreparedStmt.setString(5, start);
            sqlPreparedStmt.setString(6, end);
            sqlPreparedStmt.setString(7, "C195 Java App James Logan");
            sqlPreparedStmt.setString(8, "C195 Java App James Logan");
            sqlPreparedStmt.setInt(9, anAppointment.getCust_id());
            sqlPreparedStmt.setInt(10, anAppointment.getUser_ID());
            sqlPreparedStmt.setInt(11, anAppointment.getContactId());


            LOGGER.info("sqlPreparedStmt:" + sqlPreparedStmt);

            sqlPreparedStmt.execute();
            return true;

        } catch (SQLException sqlexcep) {
            prt.printString.accept(sqlexcep.toString());
        }
        return false;
    }

    public boolean insertDataIntoCustomerTable(Customer aCust) {
        try {
            String sqlInsert = makeInsertStatementForCustomer();
            PreparedStatement sqlPreparedStmt = mySqldb.appMySqlcon.prepareStatement(sqlInsert);
            sqlPreparedStmt.setString(1, aCust.getName());
            sqlPreparedStmt.setString(2, aCust.getAddress());
            sqlPreparedStmt.setString(3, aCust.getPostal_code());
            sqlPreparedStmt.setString(4, aCust.getPhone());
            sqlPreparedStmt.setString(5, "C195 Java App James Logan");
            sqlPreparedStmt.setString(6, "C195 Java App James Logan");
            sqlPreparedStmt.setInt(7, aCust.getDivision_id());


            sqlPreparedStmt.execute();
            return true;
        } catch (SQLException sqlexcep) {
            sqlexcep.printStackTrace();
        }
        return false;
    }

    public Boolean updateAppointmentRecordInDb(Appointment anAppointment) {

        try {

            SimpleDateFormat dtformat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.US);

            LOGGER.info("start before utc convert:" + anAppointment.getStart().toLocalDateTime());
            LOGGER.info("end before utc convert:" + anAppointment.getEnd().toLocalDateTime());

            dtformat.setTimeZone(TimeZone.getTimeZone("UTC"));

            String start = dtformat.format(anAppointment.getStart());
            String end = dtformat.format(anAppointment.getEnd());
            String updatedAt = dtformat.format(new Timestamp(System.currentTimeMillis()));


            String sqlUpdate = makeUpdateStatementForAppointment();


            LOGGER.info("sqlupdate:" + sqlUpdate);
            PreparedStatement sqlPreparedStmt = mySqldb.appMySqlcon.prepareStatement(sqlUpdate);

            sqlPreparedStmt.setString(1, anAppointment.getTitle());
            sqlPreparedStmt.setString(2, anAppointment.getDescription());
            sqlPreparedStmt.setString(3, anAppointment.getLocation());
            sqlPreparedStmt.setString(4, anAppointment.getTypeOfAppointment());
            sqlPreparedStmt.setString(5, start);
            sqlPreparedStmt.setString(6, end);
            sqlPreparedStmt.setString(7, "Modified id#" + anAppointment.getAppointment_ID() + " C195 Java App James Logan");


            sqlPreparedStmt.setInt(8, anAppointment.getCust_id());
            sqlPreparedStmt.setInt(9, anAppointment.getUser_ID());
            sqlPreparedStmt.setInt(10, anAppointment.getContactId());


            sqlPreparedStmt.setString(11, updatedAt);
            sqlPreparedStmt.setInt(12, anAppointment.getAppointment_ID());

            LOGGER.info(" sqlPreparedStmt:" + sqlPreparedStmt);


            sqlPreparedStmt.executeUpdate();

            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;

    }

    public boolean updateCustomerRecordInDb(Customer aCust) {
        try {
            String sqlUpdate = makeUpdateStatementForCustomer();
            PreparedStatement sqlPreparedStmt = mySqldb.appMySqlcon.prepareStatement(sqlUpdate);
            sqlPreparedStmt.setString(1, aCust.getName());
            sqlPreparedStmt.setString(2, aCust.getAddress());
            sqlPreparedStmt.setString(3, aCust.getPostal_code());
            sqlPreparedStmt.setString(4, aCust.getPhone());
            sqlPreparedStmt.setString(5, "C195 Java App James Logan");
            sqlPreparedStmt.setInt(6, aCust.getDivision_id());
            sqlPreparedStmt.setInt(7, aCust.getCust_id());
            LOGGER.info("sqlupdate:" + sqlUpdate);

            sqlPreparedStmt.executeUpdate();
            return true;
        } catch (SQLException sqlexcep) {
            sqlexcep.printStackTrace();
        }
        return false;

    }

    private String makeUpdateStatementForCustomer() {


        return "update customers set Customer_Name=?," +
                "Address=?," +
                "Postal_Code=?," +
                "Phone=?," +
                "Last_Updated_By=?," +
                "Division_ID=? where Customer_ID=?; ";
    }

    private String makeUpdateStatementForAppointment() {


        try {


            return "update appointments set " +
                    "Title=?," +
                    "Description=?," +
                    "Location=?," +
                    "Type=?," +
                    "Start=?," +
                    "End=?, " +
                    "Last_Updated_By=?," +
                    "Customer_ID=?," +
                    "User_ID=?, " +
                    "Contact_ID=?, " +
                    "Last_Update=? " +
                    " where Appointment_ID=?; ";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String makeInsertStatementForCustomer() {


        return "insert into customers(Customer_Name,Address," +
                "Postal_Code,Phone,Created_By,Last_Updated_By,Division_ID) values(?,?,?,?,?,?,?); ";
    }

    private String makeInsertStatementForAppointment() {


        return "insert into appointments(" +
                "Title," +
                "Description," +
                "Location," +
                "Type," +
                "Start," +
                "End," +
                "Created_By," +
                "Last_Updated_By," +
                "Customer_ID," +
                "User_ID," +
                "Contact_ID) values(?,?,?,?,?,?,?,?,?,?,?); ";
    }

    public void getUserFromUserName(User user) {
        ResultSet rsltFromDB = getUserPasswordIdFromDB(user.getName());
        try {
            while (Objects.requireNonNull(rsltFromDB).next()) {

                user.setPassword(rsltFromDB.getString("Password"));
                user.setUserId(rsltFromDB.getInt("User_ID"));


            }


        } catch (SQLException sqlexcep) {
            sqlexcep.printStackTrace();
        }


    }

    public void getSelectedCustomersCountryDivision(Country t, FirstLevelDivision y, int divID) {

        ResultSet rsltFromDB = getAcountryAndAdivisionFromDB(divID);
        try {
            while (Objects.requireNonNull(rsltFromDB).next()) {

                t.setCountryName(rsltFromDB.getString("Country"));
                t.setCountryId(rsltFromDB.getInt("Country_ID"));
                y.setDivisionName(rsltFromDB.getString("Division"));
                y.setDivisionId(divID);


            }


        } catch (SQLException sqlexcep) {
            sqlexcep.printStackTrace();
        }
    }

    private ResultSet getAcountryAndAdivisionFromDB(int divId) {
        try {


            Statement stmt = mySqldb.appMySqlcon.createStatement();

            return stmt.executeQuery("SELECT d.Division_ID,d.Division,c.Country_ID, " +
                    "c.Country from countries AS c inner join first_level_divisions " +
                    "AS d ON d.Country_ID = c.Country_ID where Division_ID=" +
                    divId);
        } catch (SQLException sqlexcep) {
            sqlexcep.printStackTrace();
        }
        return null;


    }

    private ResultSet getUserPasswordIdFromDB(String userName) {
        try {


            Statement stmt = mySqldb.appMySqlcon.createStatement();
            String sql = "SELECT User_ID,Password  " +
                    " from users where User_Name='" +
                    userName + "'";
            LOGGER.info("sql=" + sql);

            return stmt.executeQuery(sql);
        } catch (SQLException sqlexcep) {
            sqlexcep.printStackTrace();
        }
        return null;


    }

    public int deleteAppointment(int Appointment_ID) {
        String queryDelApmnt = "delete from appointments where Appointment_ID=" + Appointment_ID;
        int rowsDeleted = mySqldb.doUpdateOrDel(queryDelApmnt);

        callThreadSleep(1000L);
        return rowsDeleted;
    }

    public int deleteCustomerRecord(int CustID) {

        try {
            String queryDelApmnt = "delete from appointments where Customer_ID=" + CustID;

            int rowsOfApmntsDeleted = mySqldb.doUpdateOrDel(queryDelApmnt);
            callThreadSleep(2000L);

         



            callThreadSleep(2000L);

            return rowsOfApmntsDeleted;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void callThreadSleep(Long milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public LocalDateTime getLocalDateTime(LocalDate date, LocalTime time) {

        try {


            return time.atDate(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    public boolean phoneNumberIsValid(String phoneNumber) {

        LOGGER.info("phone number:" + phoneNumber);
        if (!isAValidPhoneNumber(phoneNumber)) {
            showalert("Please Enter a valid phone number.");
            return false;
        }
        return true;
    }

    public LocalDate convertDateToLocalDate(Date aDate) {

        try {
            return aDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean dateTimesAreValid(Timestamp earlier, Timestamp later) {

        prt.printString.accept("earlier time is:" + earlier.toLocalDateTime());
        prt.printString.accept("later time is:" + later.toLocalDateTime());
        if (earlier.after(later)) {

            showalert("The End time must be after the Start time");
            return false;
        }


        Timestamp timestampNow = new Timestamp(System.currentTimeMillis());


        LOGGER.info("timestamp:" + timestampNow);
        if (earlier.before(timestampNow)) {
            showalert("The Start time must be after the current time");
            return false;
        }


        return true;
    }

    public void addErrorEventsAlphaAndSpacesOnly(TextField textField) {
        textField.textProperty().addListener(event -> {
            prt.printString.accept("Changed");
            textField.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !textField.getText().isEmpty() &&
                            !textField.getText().
                                    matches("([a-zA-Z ]+)")
            );
        });
    }



    public LocalDate convertToLocalDate(Date date) {
        try {
            return Instant.ofEpochMilli(date.getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public LocalDateTime convertToLocalDateTime(Date date) {
        try {
            return date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date convertToCurrentTimeZone(Timestamp timestamp) {
        //noinspection unused

        try {

            Date gmtTime = new Date(timestamp.getTime());
            Date localTime = new Date();


            return new Date(gmtTime.getTime() + TimeZone.getDefault().getOffset(localTime.getTime()));


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    //get the current time zone

    private String fieldLenMin5(ArrayList<TextField> lstTextField) {
        try {


            boolean outcome;

            for (TextField textField : lstTextField) {

                outcome = nonNullPredicate.and(TextFieldMin5.preLenAtLeast5).test(textField.getText());
                if (!outcome) {
                    textField.requestFocus();
                    return textField.getId();
                }
            }


        } catch (Exception exception) {

            LOGGER.info(exception.toString());
        }

        return null;
    }

    private String emptyTextField(ArrayList<TextField> lstTextField) {
        try {


            TextFieldIsEmpty tstText = new TextFieldIsEmpty();
            for (TextField textField : lstTextField) {

                if (tstText.emptyValue.test(textField)) {
                    textField.requestFocus();
                    return textField.getId();
                }
            }
        } catch (Exception exception) {

            LOGGER.info(exception.toString());
        }


        return null;
    }

    private static boolean isAValidPhoneNumber(String phoneNo) {
        //format "1234567890"
        if (phoneNo.matches("\\d{10}")) return true;
            //with -, . or spaces
        else if (phoneNo.matches("\\d{3}[-.\\s]\\d{3}[-.\\s]\\d{4}")) return true;
            //extension length from 3 to 5
        else if (phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //varea code is in braces ()
        else return phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}");

    }


    public void showalert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Appointment Application");
        alert.setHeaderText(msg);
        //alert.setContentText(msg);

        alert.showAndWait();


    }

    public boolean hasMinChars(ArrayList<TextField> lstTxtFields) {
        try {
            String strHasMin = fieldLenMin5(lstTxtFields);
            if (strHasMin != null) {

                showalert(strHasMin + " must be at least 5 chars");
                return true;
            }


            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean anEmptyTextField(ArrayList<TextField> lstTxtFields) {


        String strEmptyFieldName = emptyTextField(lstTxtFields);
        if (strEmptyFieldName != null) {

            showalert(strEmptyFieldName + " cannot be empty.");
            return true;
        }


        return false;
    }



    public final UnaryOperator<TextFormatter.Change> filterAlphaNumericNoSpace = c -> {
        if (patternAlphaNumericNoSpace.matcher(c.getControlNewText()).matches()) {
            return c;
        } else {
            return null;
        }
    };
    public final UnaryOperator<TextFormatter.Change> filterAlphaSpace = c -> {
        if (patternAlphaSpaces.matcher(c.getControlNewText()).matches()) {
            return c;
        } else {
            return null;
        }
    };
    public final UnaryOperator<TextFormatter.Change> filterAlphaNumericSpace = c -> {
        if (patternAlphaNumericSpaces.matcher(c.getControlNewText()).matches()) {
            return c;
        } else {
            return null;
        }
    };

    public boolean confirmAndDelete(String name, Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(stage);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.setTitle("Appointment Application");
        alert.setHeaderText("Delete " + name + "?");
        alert.setContentText("Are you sure you want to delete?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isEmpty() || result.get() != ButtonType.OK;
    }

    public ObservableList<Appointment> filterListWeek(List<Appointment> list,
                                                      LocalDate adate) {
        List<Appointment> filteredList = new ArrayList<>();
        TemporalField aWeek = WeekFields.of(DayOfWeek.SUNDAY, 1).dayOfWeek();

        LocalDate startOfWeek = adate.with(aWeek, 1).minusDays(1);
        LocalDate endOfWeek = startOfWeek.plusDays(8);


        prt.printString.accept("Start of week " + startOfWeek.getDayOfWeek() + " date is" + startOfWeek);
        prt.printString.accept("End of week" + endOfWeek.getDayOfWeek() + " date is " + endOfWeek);


        LocalDate dateInlist;
        for (Appointment apmnt : list) {

            try {

                dateInlist = convertDateToLocalDate(apmnt.getDateStart());
                if (dateInlist.isAfter(startOfWeek) && dateInlist.isBefore(endOfWeek)) {

                    filteredList.add(apmnt);
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return FXCollections.observableList(filteredList);
    }

    public ObservableList<Appointment> filterListMonth(List<Appointment> list, LocalDate startOfMonth) {
        List<Appointment> filteredList = new ArrayList<>();

        LocalDate endOfMonth = startOfMonth.plusMonths(1);
        startOfMonth = startOfMonth.minusDays(1);




        LocalDate dateInlist;
        for (Appointment apmnt : list) {

            try {

                dateInlist = convertDateToLocalDate(apmnt.getDateStart());
                if (dateInlist.isAfter(startOfMonth) && dateInlist.isBefore(endOfMonth)) {

                    filteredList.add(apmnt);
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return FXCollections.observableList(filteredList);
    }

    public String getEndTimeBasedOnEst(Timestamp appointmentTime) {

        ZoneId zidUser = ZoneId.of(TimeZone.getDefault().getID());


        LocalDateTime ldt = appointmentTime.toLocalDateTime();


        ZonedDateTime nyTime = ZonedDateTime.of(
                LocalDateTime.of(ldt.getYear(),
                        ldt.getMonth(), ldt.getDayOfMonth(),
                        22, 0, 0),
                ZoneId.of("America/New_York"));




        ZonedDateTime CaliforniaDateTime = nyTime.withZoneSameInstant(zidUser);
        prt.printString.accept("California Date-time " + CaliforniaDateTime);

        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");


        return dtf2.format(CaliforniaDateTime);
    }

    public String getStartTimeBasedOnEst(Timestamp appointmentTime) {

        ZoneId zidUser = ZoneId.of(TimeZone.getDefault().getID());


        LocalDateTime ldt = appointmentTime.toLocalDateTime();


        ZonedDateTime nyTime = ZonedDateTime.of(
                LocalDateTime.of(ldt.getYear(),
                        ldt.getMonth(), ldt.getDayOfMonth(),
                        8, 0, 0),
                ZoneId.of("America/New_York"));




        ZonedDateTime CaliforniaDateTime = nyTime.withZoneSameInstant(zidUser);


        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");


        return dtf2.format(CaliforniaDateTime);
    }

    public boolean isBetweenOpeningClosing(Timestamp appointmentTime) {


        ZoneId zidLocal = ZoneId.of(TimeZone.getDefault().getID());    //Source timezone

        ZoneId zidEST = ZoneId.of("America/New_York");  //Target timezone

        LocalDateTime ldt = appointmentTime.toLocalDateTime();          //Current time

        //Zoned date time at source timezone
        ZonedDateTime zdtAtSource = ldt.atZone(zidLocal);
        //prt.printString.accept( "  id="+ apmtID+ " converted time="+zdtAtSource.toLocalDateTime());

        //Zoned date time at target timezone
        ZonedDateTime zdtEST = zdtAtSource.withZoneSameInstant(zidEST);
        prt.printString.accept("zdtEST=" + zdtEST.toLocalDateTime());


        ZonedDateTime zdtopen = ZonedDateTime.of(
                LocalDateTime.of(zdtEST.getYear(),
                        zdtEST.getMonth(), zdtEST.getDayOfMonth(),
                        8, 0, 0),
                ZoneId.of("America/New_York"));

        prt.printString.accept("zdtopen=" + zdtopen.toLocalDateTime());

        ZonedDateTime zdtclose = ZonedDateTime.of(
                LocalDateTime.of(zdtEST.getYear(),
                        zdtEST.getMonth(), zdtEST.getDayOfMonth(),
                        22, 0, 0),
                ZoneId.of("America/New_York"));


        return !zdtopen.isBefore(zdtEST) || !zdtclose.isAfter(zdtEST);// Inclusive.
    }

    public Boolean timeDateSelected(DatePicker dtpkr, String startEnd) {
        try {
            if (dtpkr.getValue() == null) {
                showalert("Please select a date for " + startEnd + " of Appointment");
                return false;

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
}
