package Controllers;

import ListObjects.*;
import Services.RequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ChangeMenuController {

    @FXML
    Label menuCategoryNameFXML, dishIngredientsFXML, howMuchFXML, unitsFXML, dishPriceFXML;

    @FXML
    ImageView dishPhotoFXML;

    @FXML
    VBox menuDishesFXML;

    private String serverAddress;

    private long selectedDishId=-1;
    private long tagRestaurantId;
    private RequestService service = new RequestService();
    private MenuDishObject[] menuDishes = new MenuDishObject[0];
    private List<String> staticDishUnits = new ArrayList<>();

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setTagRestaurantId(long tagRestaurantId) {
        this.tagRestaurantId = tagRestaurantId;
    }

    public void dataInit(){
        staticDishUnits.add("г");
        staticDishUnits.add("кг");
        staticDishUnits.add("шт");
        staticDishUnits.add("л");
    }

    public void getDishesOfMenu(long restaurantId, String url){
        menuDishes = service.getMenuDishes(restaurantId, url);

        ObservableList<AnchorPane> observableList = FXCollections.<AnchorPane>observableArrayList();
        for(MenuDishObject dish:menuDishes){
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setPrefHeight(50.0);
            anchorPane.setId(dish.getDishId()+"");

            Label label = new Label();
            label.setText(dish.getName());
            label.setPrefSize(225.0, 25.0);
            AnchorPane.setTopAnchor(label, 0.0);
            AnchorPane.setLeftAnchor(label, 0.0);
            anchorPane.getChildren().add(label);

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

        menuDishesFXML.setSpacing(10);
        menuDishesFXML.getChildren().addAll(listView);
    }

    public void seasonChanged(ObservableValue<? extends AnchorPane> observable, AnchorPane oldValue, AnchorPane newValue)
    {
        for(MenuDishObject dish:menuDishes){
            if(newValue.getId().equals(dish.getDishId()+"")){
                selectedDishId = dish.getDishId();
                Image dishPhoto = new Image("/2157d55d35204d4.jpg");
                try {
                    dishPhoto = SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(dish.getPhoto())), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dishPhotoFXML.setImage(dishPhoto);
                menuCategoryNameFXML.setText("Категорія: "+dish.getMenuCategoryName());
                dishIngredientsFXML.setText("Інгредієнти: "+dish.getIngredients());
                howMuchFXML.setText("Кількість: "+dish.getHowMuch());
                unitsFXML.setText("Розмірність кількості: "+dish.getUnits());
                dishPriceFXML.setText("Ціна: "+dish.getPrice());
            }
        }
    }

    public void changeDishPhoto(ActionEvent actionEvent) {
        JFileChooser fileChooser = new JFileChooser();
        int x = fileChooser.showDialog(null, "Відкрити файл");
        if (x == JFileChooser.APPROVE_OPTION) {
            File photo = fileChooser.getSelectedFile();
            String photoFormat = FilenameUtils.getExtension(photo.getAbsolutePath());
            BufferedImage dishPhoto=null;
            try {
                dishPhoto = ImageIO.read(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(dishPhoto, photoFormat, baos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] bytesPhoto = baos.toByteArray();
            RequestForChangeDishPhoto a = new RequestForChangeDishPhoto(selectedDishId, bytesPhoto, photoFormat);
            ObjectMapper objectMapper = new ObjectMapper();
            String str="";
            try {
                str = objectMapper.writeValueAsString(a);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            service.changeDish(str,serverAddress+"/change_dish_photo");
            for(MenuDishObject dish:menuDishes){
                if(dish.getDishId()==selectedDishId){
                    dish.setPhoto(bytesPhoto);
                    Image dishPhotoForFXML = new Image("/2157d55d35204d4.jpg");
                    try {
                        dishPhotoForFXML = SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(dish.getPhoto())), null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dishPhotoFXML.setImage(dishPhotoForFXML);
                }
            }
        }
    }


    public void changeDishIngredients(ActionEvent actionEvent) {
        MenuDishObject selectedDish = new MenuDishObject();
        for(MenuDishObject dish:menuDishes){
            if(dish.getDishId()==selectedDishId){
                selectedDish = dish;
            }
        }
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Інгредієнти");
        dialog.setContentText("Введіть інгредієнти страви:");
        TextField dialogTextField = dialog.getEditor();
        dialogTextField.setText(selectedDish.getIngredients());
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            ObjectMapper objectMapper = new ObjectMapper();
            String str = "";
            try {
                str = objectMapper.writeValueAsString(new RequestForChangeDishUnits(selectedDishId, result.get()));
                service.changeDish(str, serverAddress+"/change_dish_ingredients");
                selectedDish.setIngredients(result.get());
                dishIngredientsFXML.setText("Інгредієнти: "+result.get());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public void changeDishHowMuch(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Кількість");
        dialog.setContentText("Введіть кількість їжі:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            ObjectMapper objectMapper = new ObjectMapper();
            String str = "";
            try {
                str = objectMapper.writeValueAsString(new RequestForChangeDishHowMuch(selectedDishId, Double.parseDouble(result.get())));
                service.changeDish(str, serverAddress+"/change_dish_how_much");
                for(MenuDishObject dish:menuDishes){
                    if(dish.getDishId()==selectedDishId){
                        dish.setHowMuch(Double.parseDouble(result.get()));
                        howMuchFXML.setText("Кількість: "+Double.parseDouble(result.get()));
                    }
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public void changeDishCategories(ActionEvent actionEvent){
        RestaurantMenuCategoryObject[] categories = service.getCatergories(tagRestaurantId, serverAddress+"/get_restaurant_menu_categories");
        System.out.println(categories.length);
        List<String> categoriesNames = new ArrayList<>();
        for(RestaurantMenuCategoryObject obj: categories){
            categoriesNames.add(obj.getCategory());
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<>(categoriesNames.get(0), categoriesNames);
        dialog.setTitle("Вибір категорії");
        dialog.setHeaderText("Вибір категорії");
        dialog.setContentText("Виберіть категорію:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            for(RestaurantMenuCategoryObject obj: categories){
                if(obj.getCategory().equals(result.get())){
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        service.changeDish(objectMapper.writeValueAsString(new RequestForChangeDishCategory(selectedDishId, obj.getId())), serverAddress+"/change_dish_category");
                        menuCategoryNameFXML.setText("Категорія: "+obj.getCategory());
                        for(MenuDishObject dish:menuDishes){
                            if(dish.getDishId()==selectedDishId){
                                dish.setMenuCategoryId(obj.getId());
                                dish.setMenuCategoryName(obj.getCategory());
                            }
                        }
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void changeDishUnits(ActionEvent actionEvent) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(staticDishUnits.get(0), staticDishUnits);
        dialog.setTitle("Розмірність кількості");
        dialog.setHeaderText("Розмірність кількості");
        dialog.setContentText("Введіть розмірність кількості:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            ObjectMapper objectMapper = new ObjectMapper();
            String str = "";
            try {
                str = objectMapper.writeValueAsString(new RequestForChangeDishUnits(selectedDishId, result.get()));
                service.changeDish(str, serverAddress+"/change_dish_units");
                for(MenuDishObject dish:menuDishes){
                    if(dish.getDishId()==selectedDishId){
                        dish.setUnits(result.get());
                        unitsFXML.setText("Розмірність кількості: "+result.get());
                    }
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public void changeDishPrice(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Ціна");
        dialog.setHeaderText("Ціна");
        dialog.setContentText("Введіть ціну страви:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            ObjectMapper objectMapper = new ObjectMapper();
            String str = "";
            try {
                str = objectMapper.writeValueAsString(new RequestForChangeDishHowMuch(selectedDishId, Double.parseDouble(result.get())));
                service.changeDish(str, serverAddress+"/change_dish_price");
                for(MenuDishObject dish:menuDishes){
                    if(dish.getDishId()==selectedDishId){
                        dish.setPrice(Double.parseDouble(result.get()));
                        dishPriceFXML.setText("Ціна: "+Double.parseDouble(result.get()));
                    }
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
}
