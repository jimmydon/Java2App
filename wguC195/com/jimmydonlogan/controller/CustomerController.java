package com.jimmydonlogan.controller;

import com.jimmydonlogan.model.AddModify;
import com.jimmydonlogan.util.*;
import com.jimmydonlogan.model.Customer;


import com.jimmydonlogan.view.CustomerView;


import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * The type Customer controller.
 */
public class CustomerController extends BaseController<Customer> {
    /**
     * The Notify delete of cust apmnt records.
     */
    private final CustomerAppointBean notifyDeleteOFCustApmntRecords;
    /**
     * The Customer view.
     */
    private final CustomerView customerView = new CustomerView();
    /**
     * The Add modify ctrl.
     */
    private AddModifyCustomer addModifyCtrl;
    /**
     * The Obsv customer.
     */
    private final ObsList<Customer> obsvCustomer = new ObsList<>();
    /**
     * The constant cmbxCustomers.
     */
    public final static ComboBox<Customer> cmbxCustomers = new ComboBox<>();

    private final CustomerAppointBean beanNotifyCancelAddCust = new CustomerAppointBean();

    /**
     * The Util.
     */
    private final CustomersAppointsUtils util = new CustomersAppointsUtils();
    /**
     * The Stage.
     */
    private final Stage stage;



    /**
     * Instantiates a new Customer controller.
     *
     * @param notifyDeleteOFCustApmntRecords the notify delete of cust apmnt records
     * @param stage                          the stage
     */
    public CustomerController(CustomerAppointBean notifyDeleteOFCustApmntRecords, Stage stage) {
        this.stage = stage;
        this.notifyDeleteOFCustApmntRecords = notifyDeleteOFCustApmntRecords;

        try {


            getDataForTable();
            initAddModifyCustomer();
        } finally {


        }


    }

    /**
     * Gets data for table.
     */
    private void getDataForTable() {

        try {
            getDataInDB();
            putDbDataInObjectModelForTable();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Put db data in object model for table.
     */
    private void putDbDataInObjectModelForTable() {


        try {
            util.putCustomerDataInObjectModel(obsvCustomer);
        } finally {

        }


    }


    /**
     * Init add modify customer.
     */
    public void initAddModifyCustomer() {

        try {
            addModifyCtrl = new AddModifyCustomer(beanNotifyCancelAddCust);
            cmbxCustomers.setConverter(new CustomerConverter());
            cmbxCustomers.setItems(obsvCustomer.getObsList());
            AddCloseWindowEventForAddModify();
            addBeanToListenForCustomerCancel();
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


            obsvCustomer.setRsltFromDB(cDB.getDataFromWguDB("Select * from customers"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Add close window event for add modify.
     */
    private void AddCloseWindowEventForAddModify() {
        try {
            addModifyCtrl.viewAddCustomer.stgAddCustomerForm.setOnCloseRequest((evt) -> {


                populateCustomerTable();

            });
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
            VBox vbxcustview = customerView.buildTable();

            addEvents();
            customerView.tblCustomers.setItems(obsvCustomer.getObsList());

            return vbxcustview;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Add events.
     */
    private void addEvents() {

        try {
            addBtnAddEvent();
            addBtnModifyEvent();
            addBtnDeleteEvent();
            addTableSelectEvent();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Add table select event. using lambda concise code,avoid using bulky anonymous class implementation
     */
    private void addTableSelectEvent() {
        try {
            customerView.tblCustomers.setOnMousePressed(e -> {
                if (customerView.tblCustomers.getSelectionModel().getSelectedItem() != null)
                    customerView.btnModifyCustomer.setDisable(false);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add btn delete event. implementing functional interface setonaction using lambda, potent concise code that
     * performs better
     */
    private void addBtnDeleteEvent() {
        try {
            Tooltip tooltipDelete = new Tooltip("Select a Customer record then click to delete");
            customerView.btnDeleteCustomer.setTooltip(tooltipDelete);


            customerView.btnDeleteCustomer.setOnAction(e -> {
                if (customerView.tblCustomers.getSelectionModel().getSelectedItem() == null) {
                    util.showAlert("Please select a customer record to delete, then click delete.");
                    return;
                }
                String name = customerView.tblCustomers.getSelectionModel().getSelectedItem().getName();
                if (util.confirmAndDelete(name, stage))
                    return;

                int appointmentRowsDeleted = util.deleteCustomerAppointments(
                        customerView.tblCustomers.getSelectionModel().getSelectedItem().getCust_id());
                int recordDel=util.deleteCustomerRecord(customerView.tblCustomers.getSelectionModel().getSelectedItem().getCust_id());
                util.callThreadSleep(1000L);

                populateCustomerTable();
                if (appointmentRowsDeleted > 0) {
                    notifyDeleteOFCustApmntRecords.setValue(appointmentRowsDeleted);

                }
                if(recordDel>0)
                    util.showAlert("Customer " + name + " has been removed from database.");


            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Populate customer table.
     */
    public void populateCustomerTable() {
        try {
            customerView.tblCustomers.getItems().clear();
            getDataForTable();

            //customerView.tblCustomers=customerView.resetTableCustomers();


            customerView.tblCustomers.setItems(obsvCustomer.getObsList());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add btn modify event. using lambdas for there ease in implementing a functional interface, concise code that
     * is more efficient
     */
    private void addBtnModifyEvent() {
        try {
            Tooltip tooltipModify = new Tooltip("Select Customer record then click to modify");
            customerView.btnModifyCustomer.setTooltip(tooltipModify);

            customerView.btnModifyCustomer.setOnAction(e -> {
                if (customerView.tblCustomers.getSelectionModel().getSelectedItem() == null) {
                    util.showAlert("Please select a Customer record to modify, then click modify.");
                    return;
                }

                addModifyCtrl.enumAddModify = AddModify.MODIFY;
                addModifyCtrl.clearInputs();
                addModifyCtrl.putSelectedCustomerInModifyInput(customerView.tblCustomers.getSelectionModel().getSelectedItem());

                addModifyCtrl.display();

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add btn add event.using lambdas since Functional Interfaces allow the use of Lambda Expressions this permits
     * the writing of smaller cleaner code and allows for the removing a lot of boiler-plate code
     */
    private void addBtnAddEvent() {
        try {
            Tooltip tooltipAdd = new Tooltip("Click on To Add a Customer");
            customerView.btnAddCustomer.setTooltip(tooltipAdd);
            customerView.btnAddCustomer.setOnAction(e -> {

                addModifyCtrl.enumAddModify = AddModify.ADD;
                addModifyCtrl.clearInputs();
                addModifyCtrl.display();

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void addBeanToListenForCustomerCancel()
    {
        try { ///listening for cancel form add modify ...case for adding new customer then closing via cancel need to poplate table
            beanNotifyCancelAddCust.addPropertyChangeListener(e ->     // lambda expression

                            populateCustomerTable()
                    // System.out.println(e.getNewValue())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
