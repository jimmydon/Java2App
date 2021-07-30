package com.jimmydonlogan.view;

import com.jimmydonlogan.model.BuildView;
import com.jimmydonlogan.model.Customer;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * created by James Logan creates the GUI for viewing customer data
 */
public class CustomerView implements BuildView {

     public final TableView<Customer> tblCustomers = buildTableOfCustomers();
     public final Button btnAddCustomer = new Button("Add Customer");
     public final Button btnModifyCustomer = new Button("Modify Customer");
     public final Button btnDeleteCustomer = new Button("Delete Customer");


    public VBox buildTable() {



        HBox hbTop = new HBox();
        hbTop.setSpacing(20);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);

        Text txtCustomers = new Text("Customers");
        txtCustomers.setFont(Font.font("Courier New", FontWeight.BOLD, 20));
        txtCustomers.setEffect(dropShadow);
        txtCustomers.setId("textAppCss");


        hbTop.getChildren().addAll(txtCustomers);





        VBox vCustomerBox = new VBox();


        btnAddCustomer.setId("btnAppCss");
        btnModifyCustomer.setId("btnAppCss");
        btnDeleteCustomer.setId("btnAppCss");



        vCustomerBox.setSpacing(5);
        vCustomerBox.setPadding(new Insets(10, 0, 0, 10));
        HBox hbAddModDel = new HBox();
        hbAddModDel.setSpacing(20);
        hbAddModDel.getChildren().addAll(btnAddCustomer, btnModifyCustomer, btnDeleteCustomer);
        vCustomerBox.getChildren().addAll(hbTop, tblCustomers, hbAddModDel);





        return vCustomerBox;

    }

    TableView buildTableOfCustomers () {

        TableColumn<Customer, Integer> colDivID = new TableColumn<>("Division ID");
        TableColumn<Customer, Integer> colCustID = new TableColumn<>("Cust ID");

        TableColumn<Customer, String> colName = new TableColumn<>("Name");
        colName.setSortType(TableColumn.SortType.ASCENDING);
        TableColumn<Customer, String> colAddress = new TableColumn<>("Address");
        TableColumn<Customer, String> colPostalCode = new TableColumn<>("Postal_Code");
        TableColumn<Customer, String> colPhone = new TableColumn<>("Phone");

        colDivID.setCellValueFactory(new PropertyValueFactory<>("division_id"));
        colCustID.setCellValueFactory(new PropertyValueFactory<>("cust_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postal_code"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));


        final var tblCustomers1 = new TableView();
        tblCustomers1.getColumns().addAll(colCustID, colName, colAddress, colPostalCode, colPhone);

        return tblCustomers1;

    }
}
