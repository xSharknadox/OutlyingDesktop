package Controllers;

import ListObjects.RequestForRegisterRestaurant;
import Services.RequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateNewRestaurantLoginController {

    private RequestService service = new RequestService();
    private long tagRestaurantId;
    private String serverAddress;

    @FXML
    TextField loginField, passwordField, passwordConfirmField;

    @FXML
    Label loginResultLable;

    public void setServerAddress(String serverAddress){
        this.serverAddress = serverAddress;
    }

    public void loginOkClick(ActionEvent actionEvent) {
        if(!loginField.getText().equals("")&&!passwordField.getText().equals("")&&!passwordConfirmField.getText().equals("")){
            if(passwordField.getText().equals(passwordConfirmField.getText())){
                if(!service.checkForUniqueDepartmentLogin(serverAddress+"/check_for_unique_restaurant_login", loginField.getText())) {
                    RequestForRegisterRestaurant requestForRegisterRestaurant = new RequestForRegisterRestaurant(loginField.getText(), passwordField.getText());
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        this.tagRestaurantId = service.registerNewRestaurant(objectMapper.writeValueAsString(requestForRegisterRestaurant), serverAddress+"/register_new_restaurant");
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    Stage stage = new Stage();
                    try {
                        startCreateNewRestaurantInfoFXML(stage, loginField.getText(), passwordField.getText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Stage stage1 = (Stage) loginField.getScene().getWindow();
                    stage1.close();
                }else {
                    loginResultLable.setText("Такий логін вже існує");
                }
            }else{
                loginResultLable.setText("Паролі не співпадають");
            }
        }else{
            loginResultLable.setText("Заповніть усі поля");
        }

    }

    @FXML
    protected void startCreateNewRestaurantInfoFXML(Stage stage, String login, String password) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../create_new_restaurant_info.fxml"));
        Parent root = (Parent) loader.load();
        CreateNewRestaurantInfoController createNewRestaurantInfoController = loader.getController();
        createNewRestaurantInfoController.dataInit(tagRestaurantId, serverAddress, login, password);
        stage.setTitle("Hello World");
        stage.setScene(new Scene(root, 600, 360));
        stage.show();
    }
}
