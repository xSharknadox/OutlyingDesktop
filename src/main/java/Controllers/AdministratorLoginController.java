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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AdministratorLoginController {
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

    private long tagRestaurant;
    private long numberOfRestaurantDepartment;
    private RequestService service = new RequestService();
    private String serverAddress;
    private Stage mainWindow;

    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void setTagRestaurant(long tagRestaurant) {
        this.tagRestaurant = tagRestaurant;
    }

    public void setNumberOfRestaurantDepartment(long numberOfRestaurantDepartment) {
        this.numberOfRestaurantDepartment = numberOfRestaurantDepartment;
    }

    public void setServerAddress(String serverAddress){
        this.serverAddress = serverAddress;
    }

    @FXML
    public void loginOkClick(ActionEvent actionEvent) {
        int adminIn=-2;
        if((loginField.getText()=="")||(passwordField.getText()=="")){
            loginResultLable.setText("Заповніть усі поля");
        }else {
            String str="";
            RequestForRegisterDepartment adminLoginRequest = new RequestForRegisterDepartment(numberOfRestaurantDepartment, loginField.getText(), passwordField.getText());
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                str = objectMapper.writeValueAsString(adminLoginRequest);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            adminIn = service.AdminLogin(str, serverAddress+"/department_administrator_login");
        }
        if(adminIn==-1){
            loginResultLable.setText("Невірний пароль");
        }else if(adminIn==-2){
            loginResultLable.setText("Невідома помилка");
        }else {
            Stage stage = new Stage();
            try {
                startMainFXML(stage, tagRestaurant, numberOfRestaurantDepartment, true);
                Stage stage1 = (Stage) loginOk.getScene().getWindow();
                stage1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void startMainFXML(Stage stage,long tagId, long departmentId, boolean adminIn) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("main_department.fxml"));
        Parent root = loader.load();
        MainDepartmentController main = loader.getController();
        main.setServerAddress(serverAddress);
        main.setNumberOfRestaurantDepartment(departmentId);
        main.setTagRestaurant(tagId);
        main.setAdminIn(adminIn);
        main.setAdminMenuAble(adminIn);
        stage.setTitle("Hello World");
        stage.setScene(new Scene(root, 1200, 600));
        stage.show();
        mainWindow.close();
    }

    @FXML
    public void loginCancelClick(ActionEvent actionEvent) {
        Stage stageClose = (Stage) loginCancel.getScene().getWindow();
        Stage stage = new Stage();
        try {
            startMainFXML(stage, tagRestaurant, numberOfRestaurantDepartment, false);
            Stage stage1 = (Stage) loginOk.getScene().getWindow();
            stage1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.close();
    }
}
