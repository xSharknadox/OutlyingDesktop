package Controllers;

import ListObjects.RequestForRegisterDepartment;
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

public class AddNewDepartmentLoginController {
    private RequestService service = new RequestService();
    private long tagRestaurantId;
    private String serverAddress;

    @FXML
    TextField loginField, passwordField, passwordConfirmField;

    @FXML
    Label loginResultLable;

    public void dataInit(long tagRestaurantId, String serverAddress) {
        this.tagRestaurantId = tagRestaurantId;
        this.serverAddress = serverAddress;
    }

    @FXML
    protected void startAddNewDepartmentFXML(Stage stage, String login, String password) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../add_new_department.fxml"));
        Parent root = (Parent) loader.load();
        AddNewDepartmentController addNewDepartmentController = loader.getController();
        addNewDepartmentController.dataInit(tagRestaurantId, serverAddress, login, password);
        stage.setTitle("Hello World");
        stage.setScene(new Scene(root, 575, 400));
        stage.show();
    }

    public void loginOkClick(ActionEvent actionEvent) {
        if(!loginField.getText().equals("")&&!passwordField.getText().equals("")&&!passwordConfirmField.getText().equals("")){
            if(passwordField.getText().equals(passwordConfirmField.getText())){
                if(!service.checkForUniqueDepartmentLogin(serverAddress+"/check_for_unique_department_login", loginField.getText())) {
                    RequestForRegisterDepartment requestForRegisterDepartment = new RequestForRegisterDepartment(tagRestaurantId, loginField.getText(), passwordField.getText());
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        service.changeDish(objectMapper.writeValueAsString(requestForRegisterDepartment), serverAddress+"/register_new_department");
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    Stage stage = new Stage();
                    try {
                        startAddNewDepartmentFXML(stage, loginField.getText(), passwordField.getText());
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
}
