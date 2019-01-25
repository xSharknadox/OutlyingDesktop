package Controllers;

import ListObjects.RequestForCreateRestaurantInfo;
import Services.RequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class CreateNewRestaurantInfoController {
    private RequestService service = new RequestService();
    private String serverAddress;
    private String login, password;
    private RequestForCreateRestaurantInfo requestForCreateRestaurantInfo = new RequestForCreateRestaurantInfo();

    @FXML
    ImageView restaurantImageFXML, restaurantLogoFXML;

    @FXML
    TextField restaurantNameFXML;

    public void dataInit(long tagRestaurantId, String serverAddress, String login, String password){
        this.serverAddress = serverAddress;
        this.login = login;
        this.password = password;
        requestForCreateRestaurantInfo.setTagId(tagRestaurantId);
    }
    public void ChoosePhoto(ActionEvent actionEvent) {
        JFileChooser fileChooser = new JFileChooser();
        int x = fileChooser.showDialog(null, "Відкрити файл");
        if (x == JFileChooser.APPROVE_OPTION) {
            File photo = fileChooser.getSelectedFile();
            String photoFormat = FilenameUtils.getExtension(photo.getAbsolutePath());
            BufferedImage restaurantPhoto = null;
            try {
                restaurantPhoto = ImageIO.read(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(restaurantPhoto, photoFormat, baos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] bytesPhoto = baos.toByteArray();
            requestForCreateRestaurantInfo.setPhoto(bytesPhoto);
            requestForCreateRestaurantInfo.setPhotoFormat(photoFormat);
            Image dishPhotoForFXML = new Image("/2157d55d35204d4.jpg");
            try {
                dishPhotoForFXML = SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(bytesPhoto)), null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            restaurantImageFXML.setImage(dishPhotoForFXML);
        }
    }

    public void Cancel(ActionEvent actionEvent) {
    }

    public void ConfrimRestaurant(ActionEvent actionEvent) {
        requestForCreateRestaurantInfo.setName(restaurantNameFXML.getText());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            service.changeDish(objectMapper.writeValueAsString(requestForCreateRestaurantInfo), serverAddress+"/create_restaurant_info");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Stage stage1 = (Stage) restaurantImageFXML.getScene().getWindow();
        stage1.close();
    }

    public void ChooseLogo(ActionEvent actionEvent) {
            JFileChooser fileChooser = new JFileChooser();
            int x = fileChooser.showDialog(null, "Відкрити файл");
            if(x == JFileChooser.APPROVE_OPTION){
                File photo = fileChooser.getSelectedFile();
                String logoFormat = FilenameUtils.getExtension(photo.getAbsolutePath());
                BufferedImage logoPhoto=null;
                try {
                    logoPhoto = ImageIO.read(photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    ImageIO.write(logoPhoto, logoFormat, baos);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] bytesPhoto = baos.toByteArray();
                requestForCreateRestaurantInfo.setLogo(bytesPhoto);
                requestForCreateRestaurantInfo.setLogoFormat(logoFormat);
                Image dishPhotoForFXML = new Image("/2157d55d35204d4.jpg");
                try {
                    dishPhotoForFXML = SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(bytesPhoto)), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                restaurantLogoFXML.setImage(dishPhotoForFXML);
        }
    }
}
