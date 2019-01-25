package Controllers;

import ListObjects.RequestForDepartmentLogin;
import Services.RequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RestaurantLoginController {

    @FXML
    Button loginOk;

    @FXML
    Button loginCancel;

    @FXML
    TextField loginField;

    @FXML
    TextField passwordField;

    @FXML
    Label loginResultLable;

    private RequestService service = new RequestService();
    private String serverAddress;

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    @FXML
    public void loginOkClick(ActionEvent actionEvent) {
        long tagId = -2;
        if((loginField.getText()=="")||(passwordField.getText()=="")){
            loginResultLable.setText("Заповніть усі поля");
        }else {
            String str="";
            RequestForDepartmentLogin departmentLoginRequest = new RequestForDepartmentLogin(loginField.getText(), passwordField.getText());
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                str = objectMapper.writeValueAsString(departmentLoginRequest);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            tagId=service.RestaurantLogin(str, serverAddress+"/restaurant_login");
        }
        if(tagId==-1){
            loginResultLable.setText("Невірний пароль");
        }else if(tagId==-2){
            loginResultLable.setText("Невідома помилка");
        }else {
            Stage stage = new Stage();
            try {
                startMainFXML(stage, tagId);
                Stage stage1 = (Stage) loginOk.getScene().getWindow();
                stage1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void startMainFXML(Stage stage,long tagId) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("main_restaurant.fxml"));
        Parent root = loader.load();
        MainRestaurantController main = loader.getController();
        main.setServerAddress(serverAddress);
        main.setTagRestaurant(tagId);
        main.getDepartments();
        stage.setTitle("Hello World");
        stage.setScene(new Scene(root, 1200, 600));
        stage.show();
    }

    @FXML
    public void loginCancelClick(ActionEvent actionEvent) {
        Stage stage = (Stage) loginCancel.getScene().getWindow();
        stage.close();
    }
}
