package Main;

import com.jimmydonlogan.controller.*;

import com.jimmydonlogan.model.User;
import com.jimmydonlogan.util.CustomerAppointBean;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

/**
 * The Main method to start application.
 */
public class Main extends Application {

    private static final User user = new User(0, "", "");


    @Override
    public void start(Stage stage) {


        loadAppointmentApp(stage);


    }

    void loadAppointmentApp(Stage stage) {


        Login appLogin = new Login(user);

        if (appLogin.validLogin) {
            stage.setTitle("Appointment Application "+ user.getName() +" logged in.");
            stage.setWidth(1200);
            stage.setHeight(700);

            stage.setScene(buildCustomerAppointmentViews(stage));

            stage.show();
        }


    }


    Scene buildCustomerAppointmentViews(Stage stage) {


        Scene scene = new Scene(new Group(), 300, 200);
        CustomerAppointBean beanNotify= new CustomerAppointBean();
        CustomerController ctrlCust = new CustomerController(beanNotify,stage);
        AppointmentController ctrlApmnt = new AppointmentController(CustomerController.cmbxCustomers,user,stage);
        addBeanToListenForCustomerDeleteUpdateApmntTable(beanNotify,ctrlApmnt);
        VBox vCustBox = ctrlCust.getView();
        VBox vApmntBox = ctrlApmnt.getView();
        HBox hbCustApmnt = new HBox();
        hbCustApmnt.setSpacing(20);
        hbCustApmnt.getChildren().addAll(vCustBox, vApmntBox);

        ((Group) scene.getRoot()).getChildren().addAll(hbCustApmnt);
        scene.getStylesheets().add("css/app.css");
        return scene;
    }
    private void addBeanToListenForCustomerDeleteUpdateApmntTable(CustomerAppointBean beanNotify,AppointmentController ctrlApmnt )
    {
        beanNotify.addPropertyChangeListener(e ->     // lambda expression

                ctrlApmnt.populateAppointmentTable()
               // System.out.println(e.getNewValue())
        );
    }

    public static void main(String[] args) {


        launch(args);
    }
}
