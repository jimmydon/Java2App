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

/**
 * The type Customers appoints utils.
 */
public class CustomersAppointsUtils {

    /**
     * The Pattern alpha spaces.
     */
    private final Pattern patternAlphaSpaces = Pattern.compile("[a-zA-Z ]*");

    /**
     * The Pattern alpha numeric spaces.
     */
    private final Pattern patternAlphaNumericSpaces = Pattern.compile("^[a-zA-Z0-9 ]*$");
    /**
     * The Pattern alpha numeric no space.
     */
    private final Pattern patternAlphaNumericNoSpace = Pattern.compile("^[a-zA-Z0-9]*$");
    /**
     * The Non null predicate.
     */
    private final Predicate<String> nonNullPredicate = Objects::nonNull;
    /**
     * The Prt.
     */
    private final Print prt = new Print();


    /**
     * The My sqldb.
     */
    final MySqlDb mySqldb;

    {
        mySqldb = new MySqlDb();
    }


    /**
     * Put appointment data in object model.
     *
     * @param obsActions the obs actions
     */
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

    /**
     * Put customer data in object model.
     *
     * @param obsActions the obs actions
     */
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

    /**
     * Put input data in customer object customer.
     *
     * @param divID the div id
     * @return the customer
     */
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


    /**
     * Put input data in appointment object appointment.
     *
     * @param vwAc the vw ac
     * @return the appointment
     */
    public Appointment putInputDataInAppointmentObject(AddModifyAppointmentView vwAc) {
        try {


            Appointment apmnt = new Appointment();
            LocalDateTime dtlocalStart = getLocalDateTime(vwAc.dtpStart.getValue(),
                    vwAc.spnStart.getValue());


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


    /**
     * Insert data into appointment db table boolean.
     *
     * @param anAppointment the an appointment
     * @return the boolean
     */
    public boolean insertDataIntoAppointmentDBTable(Appointment anAppointment) {
        try {


            SimpleDateFormat dtformat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.US);


            dtformat.setTimeZone(TimeZone.getTimeZone("UTC"));

            String start = dtformat.format(anAppointment.getStart());
            String end = dtformat.format(anAppointment.getEnd());

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


            sqlPreparedStmt.execute();
            return true;

        } catch (SQLException sqlexcep) {
            prt.printString.accept(sqlexcep.toString());
        }
        return false;
    }

    /**
     * Insert data into customer table boolean.
     *
     * @param aCust the a cust
     * @return the boolean
     */
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

    /**
     * Update appointment record in db boolean.
     *
     * @param anAppointment the an appointment
     * @return the boolean
     */
    public Boolean updateAppointmentRecordInDb(Appointment anAppointment) {

        try {

            SimpleDateFormat dtformat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.US);


            dtformat.setTimeZone(TimeZone.getTimeZone("UTC"));

            String start = dtformat.format(anAppointment.getStart());
            String end = dtformat.format(anAppointment.getEnd());
            String updatedAt = dtformat.format(new Timestamp(System.currentTimeMillis()));


            String sqlUpdate = makeUpdateStatementForAppointment();


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


            sqlPreparedStmt.executeUpdate();

            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;

    }

    /**
     * Update customer record in db boolean.
     *
     * @param aCust the a cust
     * @return the boolean
     */
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


            sqlPreparedStmt.executeUpdate();
            return true;
        } catch (SQLException sqlexcep) {
            sqlexcep.printStackTrace();
        }
        return false;

    }

    /**
     * Make update statement for customer string.
     *
     * @return the string
     */
    private String makeUpdateStatementForCustomer() {


        return "update customers set Customer_Name=?," +
                "Address=?," +
                "Postal_Code=?," +
                "Phone=?," +
                "Last_Updated_By=?," +
                "Division_ID=? where Customer_ID=?; ";
    }

    /**
     * Make update statement for appointment string.
     *
     * @return the string
     */
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

    /**
     * Make insert statement for customer string.
     *
     * @return the string
     */
    private String makeInsertStatementForCustomer() {


        return "insert into customers(Customer_Name,Address," +
                "Postal_Code,Phone,Created_By,Last_Updated_By,Division_ID) values(?,?,?,?,?,?,?); ";
    }

    /**
     * Make insert statement for appointment string.
     *
     * @return the string
     */
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

    /**
     * Gets user from user name.
     *
     * @param user the user
     */
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

    /**
     * Gets selected customers country division.
     *
     * @param t     the t
     * @param y     the y
     * @param divID the div id
     */
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

    /**
     * Gets acountry and adivision from db.
     *
     * @param divId the div id
     * @return the acountry and adivision from db
     */
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

    /**
     * Gets user password id from db.
     *
     * @param userName the user name
     * @return the user password id from db
     */
    private ResultSet getUserPasswordIdFromDB(String userName) {
        try {


            Statement stmt = mySqldb.appMySqlcon.createStatement();
            String sql = "SELECT User_ID,Password  " +
                    " from users where User_Name='" +
                    userName + "'";


            return stmt.executeQuery(sql);
        } catch (SQLException sqlexcep) {
            sqlexcep.printStackTrace();
        }
        return null;


    }

    /**
     * Delete appointment int.
     *
     * @param Appointment_ID the appointment id
     * @return the int
     */
    public int deleteAppointment(int Appointment_ID) {
        try {
            String queryDelApmnt = "delete from appointments where Appointment_ID=" + Appointment_ID;
            int rowsDeleted = mySqldb.doUpdateOrDel(queryDelApmnt);

            callThreadSleep(1000L);
            return rowsDeleted;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Delete customer record int.
     *
     * @param CustID the cust id
     * @return the int
     */
    public int deleteCustomerAppointments(int CustID) {

        try {
            String queryDelApmnt = "delete from appointments where Customer_ID=" + CustID;

            int rowsOfAppointsDeleted = mySqldb.doUpdateOrDel(queryDelApmnt);
            callThreadSleep(2000L);





            return rowsOfAppointsDeleted;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @param CustID
     * @return
     */
    public int deleteCustomerRecord(int CustID)
    {

        try {
        String queryDelCustomer = "delete from customers where Customer_ID=" + CustID;
        int rowsDeleted = mySqldb.doUpdateOrDel(queryDelCustomer);



            return rowsDeleted;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    /**
     * Call thread sleep.
     *
     * @param milliSeconds the milli seconds
     */
    public void callThreadSleep(Long milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Gets local date time.
     *
     * @param date the date
     * @param time the time
     * @return the local date time
     */
    public LocalDateTime getLocalDateTime(LocalDate date, LocalTime time) {

        try {


            return time.atDate(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    /**
     * Phone number is valid boolean.
     *
     * @param phoneNumber the phone number
     * @return the boolean
     */
    public boolean phoneNumberIsValid(String phoneNumber) {


        if (!isAValidPhoneNumber(phoneNumber)) {
            showAlert("Please Enter a valid phone number.");
            return false;
        }
        return true;
    }

    /**
     * Convert date to local date local date.
     *
     * @param aDate the a date
     * @return the local date
     */
    public LocalDate convertDateToLocalDate(Date aDate) {

        try {
            return aDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Date times are valid boolean.
     *
     * @param earlier the earlier
     * @param later   the later
     * @return the boolean
     */
    public boolean dateTimesAreValid(Timestamp earlier, Timestamp later) {


        try {
            if(earlier.equals(later))
            {
                showAlert("The Start and End time cannot be the same.");
                return false;

            }

            if (earlier.after(later)) {

                showAlert("The End time must be after the Start time");
                return false;
            }


            Timestamp timestampNow = new Timestamp(System.currentTimeMillis());


            if (earlier.before(timestampNow)) {
                showAlert("The Start time must be after the current time");
                return false;
            }


            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Add error events alpha and spaces only.
     *
     * @param textField the text field
     *                  using lambda for addListener:concise code,avoid using bulky anonymous class implementation
     */
    public void addErrorEventsAlphaAndSpacesOnly(TextField textField) {
        try {
            textField.textProperty().addListener(event -> {

                textField.pseudoClassStateChanged(
                        PseudoClass.getPseudoClass("error"),
                        !textField.getText().isEmpty() &&
                                !textField.getText().
                                        matches("([a-zA-Z ]+)")
                );
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Convert to local date local date.
     *
     * @param date the date
     * @return the local date
     */
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

    /**
     * Convert to local date time local date time.
     *
     * @param date the date
     * @return the local date time
     */
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
    public Timestamp convertTimeStampToUTC(Timestamp timestamp) {

        return Timestamp.valueOf(timestamp.toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime());
    }
    /**
     * Convert to current time zone date.
     *
     * @param timestamp the timestamp
     * @return the date
     */
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

    /**
     * Field len min 5 string.
     *
     * @param lstTextField the lst text field
     * @return the string
     */
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


        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    /**
     * Empty text field string.
     *
     * @param lstTextField the lst text field
     * @return the string
     */
    private String emptyTextField(ArrayList<TextField> lstTextField) {
        try {


            TextFieldIsEmpty tstText = new TextFieldIsEmpty();
            for (TextField textField : lstTextField) {

                if (tstText.emptyValue.test(textField)) {
                    textField.requestFocus();
                    return textField.getId();
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }


        return null;
    }

    /**
     * Is a valid phone number boolean.
     *
     * @param phoneNo the phone no
     * @return the boolean
     */
    private static boolean isAValidPhoneNumber(String phoneNo) {
        try {
            //format "1234567890"
            if (phoneNo.matches("\\d{10}")) return true;
                //with -, . or spaces
            else if (phoneNo.matches("\\d{3}[-.\\s]\\d{3}[-.\\s]\\d{4}")) return true;
                //extension length from 3 to 5
            else if (phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
                //varea code is in braces ()
            else return phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


    /**
     * Showalert.
     *
     * @param msg the msg
     */
    public void showAlert(String msg) {
        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment Application");
            alert.setHeaderText(msg);
            //alert.setContentText(msg);

            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Has min chars boolean.
     *
     * @param lstTxtFields the lst txt fields
     * @return the boolean
     */
    public boolean hasMinChars(ArrayList<TextField> lstTxtFields) {
        try {
            String strHasMin = fieldLenMin5(lstTxtFields);
            if (strHasMin != null) {

                showAlert(strHasMin + " must be at least 5 chars");
                return true;
            }


            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * An empty text field boolean.
     *
     * @param lstTxtFields the lst txt fields
     * @return the boolean
     */
    public boolean anEmptyTextField(ArrayList<TextField> lstTxtFields) {


        try {
            String strEmptyFieldName = emptyTextField(lstTxtFields);
            if (strEmptyFieldName != null) {

                showAlert(strEmptyFieldName + " cannot be empty.");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }


    /**
     * The Filter alpha numeric no space. using lambda concise code,avoid using bulky anonymous class implementation
     */
    public final UnaryOperator<TextFormatter.Change> filterAlphaNumericNoSpace = c -> {
        if (patternAlphaNumericNoSpace.matcher(c.getControlNewText()).matches()) {
            return c;
        } else {
            return null;
        }
    };
    /**
     * The Filter alpha space. using lambda concise code,avoid using bulky anonymous class implementation
     */
    public final UnaryOperator<TextFormatter.Change> filterAlphaSpace = c -> {
        if (patternAlphaSpaces.matcher(c.getControlNewText()).matches()) {
            return c;
        } else {
            return null;
        }
    };
    /**
     * The Filter alpha numeric space. using lambda concise code,avoid using bulky anonymous class implementation
     */
    public final UnaryOperator<TextFormatter.Change> filterAlphaNumericSpace = c -> {
        if (patternAlphaNumericSpaces.matcher(c.getControlNewText()).matches()) {
            return c;
        } else {
            return null;
        }
    };

    /**
     * Confirm and delete boolean.
     *
     * @param name  the name
     * @param stage the stage
     * @return the boolean
     */
    public boolean confirmAndDelete(String name, Stage stage) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(stage);
            alert.initModality(Modality.WINDOW_MODAL);
            alert.setTitle("Appointment Application");
            alert.setHeaderText("Delete " + name + "?");
            alert.setContentText("Are you sure you want to delete?");

            Optional<ButtonType> result = alert.showAndWait();
            return result.isEmpty() || result.get() != ButtonType.OK;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Filter list week observable list.
     *
     * @param list  the list
     * @param adate the adate
     * @return the observable list
     */
    public ObservableList<Appointment> filterListWeek(List<Appointment> list,
                                                      LocalDate adate) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Filter list contact observable list.
     *
     * @param list       the list
     * @param contact_id the contact id
     * @return the observable list
     */
    public ObservableList<Appointment> filterListContact(List<Appointment> list, Integer contact_id) {

        try {
            List<Appointment> filteredList = new ArrayList<>();


            for (Appointment appointment : list) {

                try {


                    if (appointment.getContactId() == contact_id) {

                        filteredList.add(appointment);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return FXCollections.observableList(filteredList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Filter list month observable list.
     *
     * @param list         the list
     * @param startOfMonth the start of month
     * @return the observable list
     */
    public ObservableList<Appointment> filterListMonth(List<Appointment> list, LocalDate startOfMonth) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets end time based on est.
     *
     * @param appointmentTime the appointment time
     * @return the end time based on est
     */
    public String getEndTimeBasedOnEst(Timestamp appointmentTime) {

        try {
            ZoneId zidUser = ZoneId.of(TimeZone.getDefault().getID());


            LocalDateTime ldt = appointmentTime.toLocalDateTime();


            ZonedDateTime nyTime = ZonedDateTime.of(
                    LocalDateTime.of(ldt.getYear(),
                            ldt.getMonth(), ldt.getDayOfMonth(),
                            22, 0, 0),
                    ZoneId.of("America/New_York"));


            ZonedDateTime dateTime = nyTime.withZoneSameInstant(zidUser);


            DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");


            return dtf2.format(dateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets start time based on est.
     *
     * @param appointmentTime the appointment time
     * @return the start time based on est
     */
    public String getStartTimeBasedOnEst(Timestamp appointmentTime) {

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Is between opening closing boolean.
     *
     * @param appointmentTime the appointment time
     * @return the boolean
     */
    public boolean isBetweenOpeningClosing(Timestamp appointmentTime) {


        try {
            ZoneId zidLocal = ZoneId.of(TimeZone.getDefault().getID());    //Source timezone

            ZoneId zidEST = ZoneId.of("America/New_York");  //Target timezone

            LocalDateTime ldt = appointmentTime.toLocalDateTime();          //Current time

            //Zoned date time at source timezone
            ZonedDateTime zdtAtSource = ldt.atZone(zidLocal);
            //prt.printString.accept( "  id="+ apmtID+ " converted time="+zdtAtSource.toLocalDateTime());

            //Zoned date time at target timezone
            ZonedDateTime zdtEST = zdtAtSource.withZoneSameInstant(zidEST);


            ZonedDateTime zdtopen = ZonedDateTime.of(
                    LocalDateTime.of(zdtEST.getYear(),
                            zdtEST.getMonth(), zdtEST.getDayOfMonth(),
                            8, 0, 0),
                    ZoneId.of("America/New_York"));


            ZonedDateTime zdtclose = ZonedDateTime.of(
                    LocalDateTime.of(zdtEST.getYear(),
                            zdtEST.getMonth(), zdtEST.getDayOfMonth(),
                            22, 0, 0),
                    ZoneId.of("America/New_York"));


            return !zdtopen.isBefore(zdtEST) || !zdtclose.isAfter(zdtEST);// Inclusive.
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Time date selected boolean.
     *
     * @param dtpkr    the dtpkr
     * @param startEnd the start end
     * @return the boolean
     */
    public Boolean timeDateSelected(DatePicker dtpkr, String startEnd) {
        try {
            if (dtpkr.getValue() == null) {
                showAlert("Please select a date for " + startEnd + " of Appointment");
                return false;

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
}
