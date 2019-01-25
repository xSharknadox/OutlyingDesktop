package Controllers;

import ListObjects.*;
import Services.RequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChangeMenuCategoriesController {

    private long tagId;
    private String serverAddress;
    private RequestService service = new RequestService();
    private RestaurantMenuCategoryObject[] categories = new RestaurantMenuCategoryObject[0];

    @FXML
    VBox menuCategoriesListFXML;

    public void dataInit(long tagId, String serverAddress) {
        this.tagId = tagId;
        this.serverAddress = serverAddress;
        ObservableList<AnchorPane> observableList = FXCollections.observableArrayList();
        categories = service.getCatergories(tagId, serverAddress+"/get_restaurant_menu_categories");
        for(RestaurantMenuCategoryObject category: categories){
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setPrefHeight(30.0);
            anchorPane.setId(category.getId()+"");
            Label label = new Label();
            label.setText(category.getCategory());
            label.setPrefSize(440.0, 30.0);
            AnchorPane.setTopAnchor(label, 0.0);
            AnchorPane.setLeftAnchor(label, 0.0);
            AnchorPane.setBottomAnchor(label, 0.0);
            AnchorPane.setRightAnchor(label, 140.0);
            anchorPane.getChildren().add(label);
            Button button = new Button();
            button.setId(category.getId()+"");
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    RequestForChangeDishCategory requestForDeleteCategory = new RequestForChangeDishCategory(tagId, Long.parseLong(button.getId()));
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json="";
                    try {
                        json = objectMapper.writeValueAsString(requestForDeleteCategory);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    service.changeDish(json, serverAddress+"/delete_menu_category");
                }
            });
            button.setText("Видалити");
            button.setPrefWidth(140);
            AnchorPane.setTopAnchor(button, 0.0);
            AnchorPane.setRightAnchor(button, 0.0);
            AnchorPane.setBottomAnchor(button, 0.0);
            AnchorPane.setLeftAnchor(button, 440.0);
            anchorPane.getChildren().add(button);
            observableList.add(anchorPane);
        }

        ListView<AnchorPane> listView = new ListView<AnchorPane>(observableList);
        listView.setPrefHeight(900);
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AnchorPane>() {
            @Override
            public void changed(ObservableValue<? extends AnchorPane> observable, AnchorPane oldValue, AnchorPane newValue) {
                seasonChanged(observable, oldValue, newValue);
            }
        });
        menuCategoriesListFXML.setSpacing(10);
        menuCategoriesListFXML.getChildren().addAll(listView);
    }


    public void seasonChanged(ObservableValue<? extends AnchorPane> observable,AnchorPane oldValue,AnchorPane newValue)
    {

    }

    public void AddNewMenuCategory(ActionEvent actionEvent) {

        RestaurantMenuCategoryObject[] allCategories = service.getCatergories(tagId, serverAddress+"/get_all_menu_categories");
        List<String> categoriesNames = new ArrayList<>();
        for(RestaurantMenuCategoryObject obj: allCategories){
            categoriesNames.add(obj.getCategory());
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<>(categoriesNames.get(0), categoriesNames);
        dialog.setTitle("Нова категорія");
        dialog.setHeaderText("Нова категорія");
        dialog.setContentText("Введіть назву нової категорії");
        Optional<String> result = dialog.showAndWait();

        for(RestaurantMenuCategoryObject obj: allCategories) {
            if (obj.getCategory().equals(result.get())) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    service.changeDish(objectMapper.writeValueAsString(new RequestForChangeDishCategory(tagId, obj.getId())), serverAddress + "/add_menu_category");
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void AddNewUniqueMenuCategory(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Нова категорія");
        dialog.setTitle("Нова категорія");
        dialog.setContentText("Введіть назву нової унікальної категорії: ");
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()){
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                service.changeDish(objectMapper.writeValueAsString(new RequestForChangeDishUnits(tagId, result.get())), serverAddress+"/add_new_unique_category");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

    }
}
