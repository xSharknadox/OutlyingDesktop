package Controllers;

import ListObjects.DepartmentLoginObject;
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

public class DepartmentLoginController {
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
        DepartmentLoginObject departmentLogin = new DepartmentLoginObject();
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
                departmentLogin=service.DepartmentLogin(str, serverAddress+"/department_login");
        }
        if(departmentLogin.getDepartmentId()==-1){
            loginResultLable.setText("Невірний пароль");
        }else if(departmentLogin.getDepartmentId()==-2){
            loginResultLable.setText("Невідома помилка");
        }else {
            Stage stage = new Stage();
            try {
                startMainFXML(stage, departmentLogin.getTagId(), departmentLogin.getDepartmentId());
                Stage stage1 = (Stage) loginOk.getScene().getWindow();
                stage1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void startMainFXML(Stage stage,long tagId, long departmentId) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("main_department.fxml"));
        Parent root = loader.load();
        MainDepartmentController main = loader.getController();
        main.setServerAddress(serverAddress);
        main.setNumberOfRestaurantDepartment(departmentId);
        main.setTagRestaurant(tagId);
        main.setAdminMenuAble(false);
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

