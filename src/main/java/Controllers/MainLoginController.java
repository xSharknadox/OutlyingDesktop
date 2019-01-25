package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainLoginController {

    private String serverAddress;

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    @FXML
    protected void startLoginDepartmentFXML(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("department_login.fxml"));
        Parent root = loader.load();
        DepartmentLoginController departmentLoginController = loader.getController();
        departmentLoginController.setServerAddress(serverAddress);
        stage.setScene(new Scene(root, 260, 240));
        stage.show();
    }

    @FXML
    protected void startLoginRestaurantFXML(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("restaurant_login.fxml"));
        Parent root = loader.load();
        RestaurantLoginController restaurantLoginController = loader.getController();
        restaurantLoginController.setServerAddress(serverAddress);
        stage.setScene(new Scene(root, 260, 240));
        stage.show();
    }

    @FXML
    protected void startCreateNewRestaurantLoginFXML(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../create_new_restaurant_login.fxml"));
        Parent root = (Parent) loader.load();
        CreateNewRestaurantLoginController controller = loader.getController();
        controller.setServerAddress(serverAddress);
        stage.setScene(new Scene(root, 280, 300));
        stage.show();
    }

    public void enterToDepartment(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            startLoginDepartmentFXML(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enterToRestaurant(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            startLoginRestaurantFXML(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerRestaurant(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            startCreateNewRestaurantLoginFXML(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
