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

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The type Main.
 */
public class Main extends Application {

    /**
     * The constant user.
     */
    private static final User user = new User(0, "", "");


    @Override
    public void start(Stage stage) {


        try {
            loadAppointmentApp(stage);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }


    }

    /**
     * Load appointment app.
     *
     * @param stage the stage
     */
    void loadAppointmentApp(Stage stage) {

        try {

            ResourceBundle loginMessages = ResourceBundle.getBundle("Resources/MessagesBundle",
                    Locale.getDefault());


            Login appLogin = new Login(user,loginMessages);

            if (appLogin.validLogin) {
                stage.setTitle("Appointment Application "+ user.getName() +" logged in.");
                stage.setWidth(1200);
                stage.setHeight(700);

                stage.setScene(buildCustomerAppointmentViews(stage));

                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * Build customer appointment views scene.
     *
     * @param stage the stage
     * @return the scene
     */
    Scene buildCustomerAppointmentViews(Stage stage) {


        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Add bean to listen for customer delete update apmnt table.
     *
     * @param beanNotify the bean notify
     * @param ctrlApmnt  the ctrl apmnt
     * using lambda expression to instantiate and avoid using bulky anonymous class implementation
     * also smaller cleaner code used to update Appointment table after customer adn appointments have been deleted
     */
    private void addBeanToListenForCustomerDeleteUpdateApmntTable(CustomerAppointBean beanNotify,AppointmentController ctrlApmnt )
    {
        try {
            beanNotify.addPropertyChangeListener(e ->     // lambda expression

                    ctrlApmnt.populateAppointmentTable()
                   // System.out.println(e.getNewValue())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {


        try {
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
