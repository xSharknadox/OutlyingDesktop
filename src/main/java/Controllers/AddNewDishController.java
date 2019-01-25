package Controllers;

import ListObjects.RequestForNewDish;
import ListObjects.RestaurantMenuCategoryObject;
import Services.RequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
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

public class AddNewDishController {
    @FXML
    ImageView dishImageFXML;

    @FXML
    TextField dishNameFXML, dishIngredientsFXML, dishHowMuchFXML, dishPriceFXML;

    @FXML
    ChoiceBox dishUnitsFXML, categoryFXML;

    private long tagRestaurantId;
    private String serverAddress;
    private RequestService service = new RequestService();
    private RequestForNewDish newDish = new RequestForNewDish();
    private RestaurantMenuCategoryObject[] categories = new RestaurantMenuCategoryObject[0];
    private List<String> categoriesList = new ArrayList<>();

    public void dataInit(long tagRestaurantId, String serverAddress){
        newDish.setRestaurantDepartmentId(tagRestaurantId);
        this.tagRestaurantId = tagRestaurantId;
        this.serverAddress = serverAddress;
        categories = service.getCatergories(tagRestaurantId, serverAddress+"/get_restaurant_menu_categories");
        for(RestaurantMenuCategoryObject category:categories){
            categoriesList.add(category.getCategory());
        }
        dishUnitsFXML.setItems(FXCollections.observableArrayList("шт", "г", "кг", "л"));
        dishUnitsFXML.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                newDish.setUnits(String.valueOf(newValue));
            }
        });
        categoryFXML.setItems(FXCollections.observableArrayList(categoriesList));
        categoryFXML.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                for(RestaurantMenuCategoryObject category:categories){
                    System.out.println(String.valueOf(newValue));
                    if(category.getCategory().equals(String.valueOf(newValue))){
                        newDish.setCategory(category.getId());
                    }
                }
            }
        });
    }

    public void ConfrimDish(ActionEvent actionEvent) {
        newDish.setName(dishNameFXML.getText());
        newDish.setIngredients(dishIngredientsFXML.getText());
        newDish.setHowMuch(Double.parseDouble(dishHowMuchFXML.getText()));
        newDish.setPrice(Double.parseDouble(dishPriceFXML.getText()));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try {
            json = objectMapper.writeValueAsString(newDish);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        service.changeDish(json, serverAddress+"/add_new_dish");
    }

    public void Cancel(ActionEvent actionEvent) {
        Stage stage = (Stage)dishUnitsFXML.getScene().getWindow();
        stage.close();
    }

    public void ChoosePhoto(ActionEvent actionEvent) {
        JFileChooser fileChooser = new JFileChooser();
        int x = fileChooser.showDialog(null, "Відкрити файл");
        if(x == JFileChooser.APPROVE_OPTION){
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
            newDish.setPhoto(bytesPhoto);
            newDish.setPhotoFormat(photoFormat);
            Image dishPhotoForFXML = new Image("/2157d55d35204d4.jpg");
            try {
                dishPhotoForFXML = SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(bytesPhoto)), null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            dishImageFXML.setImage(dishPhotoForFXML);
        }
    }
}
