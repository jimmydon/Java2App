package com.jimmydonlogan.controller;

import com.jimmydonlogan.model.AddModify;
import com.jimmydonlogan.util.*;
import com.jimmydonlogan.model.Customer;


import com.jimmydonlogan.view.CustomerView;


import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class CustomerController extends BaseController<Customer> {
    private final CustomerAppointBean notifyDeleteOFCustApmntRecords;
    private final CustomerView customerView = new CustomerView();
    private final AddModifyCustomer addModifyCtrl = new AddModifyCustomer();
    private final ObsList<Customer> obsvCustomer= new ObsList<>();
    public final static ComboBox<Customer> cmbxCustomers = new ComboBox<>();


    private final CustomersAppointsUtils util = new CustomersAppointsUtils();
    private final Stage stage;


    public CustomerController(CustomerAppointBean notifyDeleteOFCustApmntRecords, Stage stage) {
        this.stage = stage;
        this.notifyDeleteOFCustApmntRecords = notifyDeleteOFCustApmntRecords;
        getDataForTable();
        initAddModifyCustomer();


    }

    private void getDataForTable() {

        getDataInDB();
        putDbDataInObjectModelForTable();

    }

    private void putDbDataInObjectModelForTable() {


        util.putCustomerDataInObjectModel(obsvCustomer);



    }


    public void initAddModifyCustomer() {

        cmbxCustomers.setConverter(new CustomerConverter());
        cmbxCustomers.setItems(obsvCustomer.getObsList());
        AddCloseWindowEventForAddModify();


    }
    private void getDataInDB()
    {
        MySqlDb cDB = new MySqlDb();



        obsvCustomer.setRsltFromDB(cDB.getDataFromWguDB("Select * from customers"));

    }

    private void AddCloseWindowEventForAddModify() {
        addModifyCtrl.viewAddCustomer.stgAddCustomerForm.setOnCloseRequest((evt) -> {

            if (!addModifyCtrl.getCustomerAddedModified())
                return;
            populateCustomerTable();

        });
    }

    public VBox getView() {


        VBox vbxcustview = customerView.buildTable();

        addEvents();
        customerView.tblCustomers.setItems(obsvCustomer.getObsList());

        return vbxcustview;
    }

    private void addEvents() {

        addBtnAddEvent();
        addBtnModifyEvent();
        addBtnDeleteEvent();
        addTableSelectEvent();

    }

    private void addTableSelectEvent() {
        customerView.tblCustomers.setOnMousePressed(e -> {
            if (customerView.tblCustomers.getSelectionModel().getSelectedItem() != null)
                customerView.btnModifyCustomer.setDisable(false);
        });
    }

    private void addBtnDeleteEvent() {
        Tooltip tooltipDelete = new Tooltip("Select a Customer record then click to delete");
        customerView.btnDeleteCustomer.setTooltip(tooltipDelete);


        customerView.btnDeleteCustomer.setOnAction(e -> {
            if (customerView.tblCustomers.getSelectionModel().getSelectedItem() == null) {
                util.showalert("Please select a customer record to delete, then click delete.");
                return;
            }
            String name = customerView.tblCustomers.getSelectionModel().getSelectedItem().getName();
            if (util.confirmAndDelete(name, stage))
                return;

            int rowsDeleted = util.deleteCustomerRecord(customerView.tblCustomers.getSelectionModel().getSelectedItem().getCust_id());
            util.callThreadSleep(1000L);
            populateCustomerTable();
            if (rowsDeleted > 0) {
                notifyDeleteOFCustApmntRecords.setValue(rowsDeleted);
                util.showalert("Customer " + name+ " has been removed from database.");
            }


        });
    }

    private void populateCustomerTable() {
        customerView.tblCustomers.getItems().clear();
        getDataForTable();
        System.out.println("rows in customer table from:" + obsvCustomer.getObsList().size());
        //customerView.tblCustomers=customerView.resetTableCustomers();

        System.out.println("Size of customer table after clear=" + customerView.tblCustomers.getItems().size());
        customerView.tblCustomers.setItems(obsvCustomer.getObsList());
        System.out.println("Size of customer table=" + customerView.tblCustomers.getItems().size());
    }

    private void addBtnModifyEvent() {
        Tooltip tooltipModify = new Tooltip("Select Customer record then click to modify");
        customerView.btnModifyCustomer.setTooltip(tooltipModify);

        customerView.btnModifyCustomer.setOnAction(e -> {
            if (customerView.tblCustomers.getSelectionModel().getSelectedItem() == null) {
                util.showalert("Please select a Customer record to modify, then click modify.");
                return;
            }

            addModifyCtrl.enumAddModify = AddModify.MODIFY;
            addModifyCtrl.clearInputs();
            addModifyCtrl.putSelectedCustomerInModifyInput(customerView.tblCustomers.getSelectionModel().getSelectedItem());

            addModifyCtrl.display();

        });
    }

    private void addBtnAddEvent() {
        Tooltip tooltipAdd = new Tooltip("Click on To Add a Customer");
        customerView.btnAddCustomer.setTooltip(tooltipAdd);
        customerView.btnAddCustomer.setOnAction(e -> {

            addModifyCtrl.enumAddModify = AddModify.ADD;
            addModifyCtrl.clearInputs();
            addModifyCtrl.display();

        });

    }


}
