package Controllers;

import ListObjects.RequestForAddNewDepartment;
import ListObjects.WorkerObject;
import Services.RequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddNewDepartmentController {
    private RequestService service = new RequestService();
    private String serverAddress;
    private long tagRestaurantId;
    private String  login, password;
    private List<WorkerObject> workersObjectsList = new ArrayList<>();
    private ObservableList<AnchorPane> observableList = FXCollections.<AnchorPane>observableArrayList();
    private int uniqueWorkerId=0;
    private int selectedWorkerInList=-1;

    @FXML
    TextField countryFXML, regionFXML, cityFXML, streetFXML, houseFXML;

    @FXML
    Label errorFXML;

    @FXML
    ListView workerListFXML;

    public void dataInit(long tagRestaurantId, String serverAddress, String login, String password){
        this.tagRestaurantId = tagRestaurantId;
        this.serverAddress = serverAddress;
        this.login = login;
        this.password = password;
        observableList = FXCollections.<AnchorPane>observableArrayList();
        observableList.add(createWorkerView("Поки що немає офіціантів", "", -1));

        workerListFXML.setItems(observableList);
        workerListFXML.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                seasonChanged(observable, oldValue, newValue);
            }
        });
        observableList = FXCollections.<AnchorPane>observableArrayList();
    }

    public void seasonChanged(ObservableValue observable, Object oldValue, Object newValue) {
        AnchorPane anchorPane = (AnchorPane) newValue;
        selectedWorkerInList = Integer.parseInt(anchorPane.getId());
    }

    private void NewWorkerInList(String name, String surname, int id){
        observableList.add(createWorkerView(name, surname, id));
        workerListFXML.setItems(observableList);
    }

    private AnchorPane createWorkerView(String name, String surname, int id){
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setId(id+"");
        anchorPane.setPrefSize(180.0, 30.0);
        Label label = new Label(name+" "+surname);
        label.setPrefSize(230.0, 30);
        AnchorPane.setLeftAnchor(label, 0.0);
        AnchorPane.setBottomAnchor(label, 0.0);
        AnchorPane.setTopAnchor(label, 0.0);
        anchorPane.getChildren().add(label);
        return anchorPane;
    }

    public void AddNewWorker(ActionEvent actionEvent) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Новий офіціант");
        dialog.setHeaderText("Введіть ім'я і фамілію нового офіціанта");
        ButtonType confirmButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField name = new TextField();
        name.setPromptText("Ім'я");
        TextField surname = new TextField();
        surname.setPromptText("Фамілія");

        grid.add(new Label("Ім'я: "), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Фамілія: "), 0, 1);
        grid.add(surname, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> name.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return new Pair<>(name.getText(), surname.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        if(result.isPresent()){
            WorkerObject worker = new WorkerObject();
            worker.setName(result.get().getKey());
            worker.setSurname(result.get().getValue());
            worker.setRating(0.0);
            worker.setWorderId(-1);
            workersObjectsList.add(worker);
            NewWorkerInList(workersObjectsList.get(workersObjectsList.size()-1).getName(), workersObjectsList.get(workersObjectsList.size()-1).getSurname(), uniqueWorkerId);
            uniqueWorkerId++;
        }


    }

    public void ConfirmDepartment(ActionEvent actionEvent) {
        if(countryFXML.getText().equals("")){
            errorFXML.setText("Ви не ввели країну");
        }else if(regionFXML.getText().equals("")){
            errorFXML.setText("Ви не ввели область");
        } else if(cityFXML.getText().equals("")){
            errorFXML.setText("Ви не ввели місто");
        } else if(streetFXML.getText().equals("")){
            errorFXML.setText("Ви не ввели вулицю");
        } else if(houseFXML.getText().equals("")){
            errorFXML.setText("Ви не ввели будинок");
        } else {
            errorFXML.setText("");
            RequestForAddNewDepartment requestForAddNewDepartment = new RequestForAddNewDepartment(tagRestaurantId, login, password, countryFXML.getText(), regionFXML.getText(), cityFXML.getText(), streetFXML.getText(), Integer.parseInt(houseFXML.getText()));
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                long newDepartmentId = service.addNewDepartment(objectMapper.writeValueAsString(requestForAddNewDepartment), serverAddress+"/add_new_restaurant_department");
                if(workersObjectsList.size()>0){
                    for(WorkerObject workerObject: workersObjectsList){
                        workerObject.setWorderId(newDepartmentId);
                    }
                    service.changeDish(objectMapper.writeValueAsString(workersObjectsList), serverAddress+"/add_workers_of_new_department");
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        Stage stage1 = (Stage) errorFXML.getScene().getWindow();
        stage1.close();
    }

    public void DeleteNewWorker(ActionEvent actionEvent) {
        for(int x = 0; x<observableList.size(); x++){
            if(Integer.parseInt(observableList.get(x).getId())==selectedWorkerInList){
                observableList.remove(x);
                workersObjectsList.remove(x);
                break;
            }
        }
    }
}
