package Controllers;

import ListObjects.DepartmentObject;
import ListObjects.WorkerObject;
import Services.RequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainRestaurantController {
    @FXML
    VBox vboxDepartments;

    @FXML
    VBox vboxDepartmentWorkers;

    @FXML
    TextField workerNameFXML, workerSurnameFXML;

    @FXML
    ChoiceBox departmentIdFXML;

    private String serverAddress;
    private long tagRestaurant = 2;
    private RequestService service = new RequestService();
    private List<DepartmentObject> departments = new ArrayList<>();
    private List<WorkerObject> workers = new ArrayList<>();
    private long departmentIdForNewWorker=-1;
    private static final PseudoClass ADMIN_ACTIVE= PseudoClass.getPseudoClass("admin-active");
    private static final PseudoClass ADMIN_PASSIVE= PseudoClass.getPseudoClass("admin-passive");

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setTagRestaurant(long tagRestaurant) {
        this.tagRestaurant = tagRestaurant;
    }

    public void getDepartments(){
        departments = service.getDepartments(tagRestaurant+"", serverAddress+"/get_departments_of_restaurant");
        ObservableList<String> cbList = FXCollections.<String>observableArrayList();
        ObservableList<AnchorPane> observableList = FXCollections.<AnchorPane>observableArrayList();
        for(DepartmentObject department: departments){
            observableList.add(getDepartmentView(department));
            cbList.add(department.getDepartmentId()+"");
        }

        ListView<AnchorPane> listView = new ListView<AnchorPane>(observableList);
        listView.setPrefHeight(900);
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AnchorPane>() {
            @Override
            public void changed(ObservableValue<? extends AnchorPane> observable, AnchorPane oldValue, AnchorPane newValue) {
                DepartmentChanged(observable, oldValue, newValue);
            }
        });

        vboxDepartments.setSpacing(10);
        vboxDepartments.getChildren().addAll(listView);

        departmentIdFXML.setItems(cbList);
        departmentIdFXML.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                departmentIdForNewWorker = Long.parseLong(newValue.toString());
            }
        });

    }

    public void DepartmentChanged(ObservableValue<? extends AnchorPane> observable,AnchorPane oldValue,AnchorPane newValue) {
        workers = service.getAllWorkers(Long.parseLong(newValue.getId()), serverAddress+"/get_all_workers");
        ObservableList<AnchorPane> observableList = FXCollections.<AnchorPane>observableArrayList();
        for(int x=0; x<workers.size(); x++){
            observableList.add(getWorkerView(x,workers.get(x)));
        }

        ListView<AnchorPane> listView = new ListView<AnchorPane>(observableList);
        listView.setPrefHeight(900);
        listView.setOrientation(Orientation.VERTICAL);
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AnchorPane>() {
            @Override
            public void changed(ObservableValue<? extends AnchorPane> observable, AnchorPane oldValue, AnchorPane newValue) {
                WorkerChanged(observable, oldValue, newValue);
            }
        });

        vboxDepartmentWorkers.setSpacing(0);
        vboxDepartmentWorkers.getChildren().clear();
        vboxDepartmentWorkers.getChildren().addAll(listView);
    }

    public void WorkerChanged(ObservableValue<? extends AnchorPane> observable,AnchorPane oldValue,AnchorPane newValue) {
    }

    public void Close(ActionEvent actionEvent) {

    }

    public AnchorPane getDepartmentView(DepartmentObject department){
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(80.0);
        anchorPane.setId(department.getDepartmentId()+"");
        Label label = new Label();
        label.setText("Номер відділення: "+department.getDepartmentId());
        label.setPrefSize(160.0, 30.0);
        AnchorPane.setTopAnchor(label, 0.0);
        AnchorPane.setLeftAnchor(label, 0.0);
        AnchorPane.setRightAnchor(label, 60.0);
        anchorPane.getChildren().add(label);
        Button button = new Button("Видалити це відділення");
        button.setPrefSize(40.0, 30.0);
        AnchorPane.setTopAnchor(button, 0.0);
        AnchorPane.setLeftAnchor(button, 160.0);
        AnchorPane.setRightAnchor(button, 0.0);
        anchorPane.getChildren().add(button);
        label = new Label();
        label.setText("Адреса: "+department.getDepartmentAddress());
        label.setPrefSize(200.0, 30.0);
        AnchorPane.setTopAnchor(label, 40.0);
        AnchorPane.setRightAnchor(label, 0.0);
        AnchorPane.setLeftAnchor(label, 0.0);
        anchorPane.getChildren().add(label);
        return anchorPane;
    }

    public AnchorPane getWorkerView(int pos, WorkerObject worker){
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(70.0);
        anchorPane.setId(worker.getWorderId()+"");
        Label label = new Label();
        label.setText("Фамілія: "+worker.getSurname());
        label.setPrefSize(145.0, 30.0);
        AnchorPane.setTopAnchor(label, 0.0);
        AnchorPane.setLeftAnchor(label, 0.0);
        AnchorPane.setRightAnchor(label, 155.0);
        anchorPane.getChildren().add(label);
        label = new Label();
        label.setPrefSize(145.0, 30.0);
        label.setText("Ім'я: "+worker.getName());
        AnchorPane.setTopAnchor(label, 0.0);
        AnchorPane.setLeftAnchor(label, 155.0);
        AnchorPane.setRightAnchor(label, 0.0);
        anchorPane.getChildren().add(label);
        label = new Label();
        label.setText("Рейтинг: "+worker.getRating());
        label.setPrefSize(200.0, 30.0);
        AnchorPane.setTopAnchor(label, 35.0);
        AnchorPane.setRightAnchor(label, 0.0);
        AnchorPane.setLeftAnchor(label, 0.0);
        anchorPane.getChildren().add(label);
        Button button = new Button("Видалити");
        button.setId(worker.getWorderId()+"");
        button.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                deleteWorker(worker);
            }
        });
        button.setPrefSize(30.0, 30.0);
        AnchorPane.setTopAnchor(button, 40.0);
        AnchorPane.setLeftAnchor(button, 155.0);
        AnchorPane.setRightAnchor(button, 0.0);
        anchorPane.getChildren().add(button);
        Button sbutton = new Button("A");
        sbutton.setPrefSize(30.0, 30.0);
        sbutton.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                changeAdminStatus(pos,worker);
                if(worker.isAdmin()) {
                    sbutton.setStyle("-fx-background-color: #0000FF");
                }
                else {
                    sbutton.setStyle("-fx-background-color: #C0C0C0");
                }
            }
        });
        System.out.println(worker.getName()+worker.getSurname());
        System.out.println(worker.isAdmin());
        if(worker.isAdmin()) {
            sbutton.setStyle("-fx-background-color: #0000FF");
        }
        else {
            sbutton.setStyle("-fx-background-color: #C0C0C0");
        }
        AnchorPane.setTopAnchor(sbutton, 0.0);
        AnchorPane.setLeftAnchor(sbutton, 280.0);
        AnchorPane.setRightAnchor(sbutton, 0.0);
        anchorPane.getChildren().add(sbutton);
        return anchorPane;
    }

    public void addNewWorker(ActionEvent actionEvent) {
        if((workerNameFXML.getText()!="")||(workerSurnameFXML.getText()!="")){
            WorkerObject worker = new WorkerObject();
            worker.setWorderId(departmentIdForNewWorker);
            worker.setName(workerNameFXML.getText());
            worker.setSurname(workerSurnameFXML.getText());
            worker.setRating(0);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                service.changeDish(objectMapper.writeValueAsString(worker), serverAddress+"/add_one_worker_of_new_department");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteWorker(WorkerObject worker){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            service.changeDish(objectMapper.writeValueAsString(worker), serverAddress+"/delete_one_worker_of_department");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public void changeAdminStatus(int pos, WorkerObject worker){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            service.changeDish(objectMapper.writeValueAsString(worker), serverAddress+"/change_admin_status");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        workers.get(pos).setAdmin(!workers.get(pos).isAdmin());
    }

    public void addNewDepartment(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            startAddNewDepartmentFXML(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void startAddNewDepartmentFXML(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("add_new_department_login.fxml"));
        Parent root = (Parent) loader.load();
        AddNewDepartmentLoginController addNewDepartmentLoginController = loader.getController();
        addNewDepartmentLoginController.dataInit(tagRestaurant, serverAddress);
        stage.setTitle("Hello World");
        stage.setScene(new Scene(root, 240, 300));
        stage.show();
    }

    @FXML
    public void ChangeMenu(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            startChangeMenuFXML(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void startChangeMenuFXML(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("change_menu.fxml"));
        Parent root = (Parent) loader.load();
        ChangeMenuController changeMenuController = loader.getController();
        changeMenuController.getDishesOfMenu(tagRestaurant, serverAddress+"/get_dishes_of_tag_restaurant");
        changeMenuController.setServerAddress(serverAddress);
        changeMenuController.setTagRestaurantId(tagRestaurant);
        changeMenuController.dataInit();
        stage.setScene(new Scene(root, 800, 400));
        stage.show();
    }

    public void AddNewDish(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            startAddNewDishFXML(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void startAddNewDishFXML(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("add_new_dish.fxml"));
        Parent root = (Parent) loader.load();
        AddNewDishController addNewDishController = loader.getController();
        addNewDishController.dataInit(tagRestaurant, serverAddress);
        stage.setTitle("Hello World");
        stage.setScene(new Scene(root, 310, 460));
        stage.show();
    }

    public void ChangeMenuCategories(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            startMenuCategoriesFXML(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void startMenuCategoriesFXML(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("change_menu_categories.fxml"));
        Parent root = (Parent) loader.load();
        ChangeMenuCategoriesController changeMenuCategoriesController = loader.getController();
        changeMenuCategoriesController.dataInit(tagRestaurant, serverAddress);
        stage.setScene(new Scene(root, 600, 440));
        stage.show();
    }
}
