package Controllers;

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
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddNewDepartmentWorkersController {
    private RequestService service = new RequestService();
    private String serverAddress;
    private long tagRestaurantId, restaurantDepartmentId;
    private List<WorkerObject> workersObjectsList = new ArrayList<>();
    private List<WorkerObject> newWorkersObjectsList = new ArrayList<>();
    private ObservableList<AnchorPane> newObservableList = FXCollections.<AnchorPane>observableArrayList();
    private ObservableList<AnchorPane> observableList = FXCollections.<AnchorPane>observableArrayList();
    private int uniqueNewWorkerId=0;
    private int uniqueWorkerId=0;
    private int selectedWorkerInNewList=-1;
    private int selectedWorkerInList=-1;
    private boolean deleteSomeWorkersInCurrentList = false;
    private List<WorkerObject> deleteWorkersList = new ArrayList<>();


    @FXML
    ListView workerListFXML, newWorkerListFXML;

    public void dataInit(long tagRestaurantId, long restaurantDepartmentId, String serverAddress){
        this.tagRestaurantId = tagRestaurantId;
        this.restaurantDepartmentId = restaurantDepartmentId;
        this.serverAddress = serverAddress;
        newObservableList = FXCollections.<AnchorPane>observableArrayList();
        newObservableList.add(createWorkerView("Поки що немає офіціантів", "", -1));
        workersObjectsList = service.getAllWorkers(restaurantDepartmentId, serverAddress+"/get_all_workers");
        for(WorkerObject worker:workersObjectsList){
            observableList.add(createWorkerView(worker.getName(), worker.getSurname(), uniqueWorkerId));
            uniqueWorkerId++;
        }
        workerListFXML.setItems(observableList);
        workerListFXML.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                seasonChanged(observable, oldValue, newValue);
            }
        });
        newWorkerListFXML.setItems(newObservableList);
        newWorkerListFXML.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                seasonNewChanged(observable, oldValue, newValue);
            }
        });
        newObservableList = FXCollections.<AnchorPane>observableArrayList();
    }

    public void seasonChanged(ObservableValue observable, Object oldValue, Object newValue) {
        AnchorPane anchorPane = (AnchorPane) newValue;
        selectedWorkerInList = Integer.parseInt(anchorPane.getId());
    }

    public void seasonNewChanged(ObservableValue observable, Object oldValue, Object newValue) {
        AnchorPane anchorPane = (AnchorPane) newValue;
        selectedWorkerInNewList = Integer.parseInt(anchorPane.getId());
    }

    private void NewWorkerInList(String name, String surname, int id){
        newObservableList.add(createWorkerView(name, surname, id));
        newWorkerListFXML.setItems(newObservableList);
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
            newWorkersObjectsList.add(worker);
            NewWorkerInList(newWorkersObjectsList.get(newWorkersObjectsList.size()-1).getName(), newWorkersObjectsList.get(newWorkersObjectsList.size()-1).getSurname(), uniqueNewWorkerId);
            uniqueNewWorkerId++;
        }


    }

    public void DeleteNewWorker(ActionEvent actionEvent) {
        for(int x = 0; x<newObservableList.size(); x++){
            if(Integer.parseInt(newObservableList.get(x).getId())==selectedWorkerInNewList){
                newObservableList.remove(x);
                newWorkersObjectsList.remove(x);
                break;
            }
        }
    }

    public void ConfirmDepartmentWorkers(ActionEvent actionEvent) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if(newWorkersObjectsList.size()>0){
                for(WorkerObject workerObject: newWorkersObjectsList){
                    workerObject.setWorderId(restaurantDepartmentId);
                }
                service.changeDish(objectMapper.writeValueAsString(newWorkersObjectsList), serverAddress+"/add_workers_of_new_department");
            }
            if(deleteSomeWorkersInCurrentList){
                service.changeDish(objectMapper.writeValueAsString(deleteWorkersList), serverAddress+"/delete_workers_of_department");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void DeleteWorker(ActionEvent actionEvent) {
        for(int x = 0; x<observableList.size(); x++){
            if(Integer.parseInt(observableList.get(x).getId())==selectedWorkerInList){
                deleteWorkersList.add(workersObjectsList.get(x));
                observableList.remove(x);
                workersObjectsList.remove(x);
                deleteSomeWorkersInCurrentList = true;
                break;
            }
        }
    }
}
