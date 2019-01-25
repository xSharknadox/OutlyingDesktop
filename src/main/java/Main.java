import Controllers.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private String serverAddress = "http://192.168.43.237:8090";

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("main_login.fxml"));
        Parent root = loader.load();
        MainLoginController mainLoginController = loader.getController();
        mainLoginController.setServerAddress(serverAddress);
        primaryStage.setTitle("Outlying");
        primaryStage.setScene(new Scene(root, 300, 240));
        primaryStage.show();
    }

}