package ListObjects;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class OrderListObject {
    private String orderNumber;
    private String userName;
    private String userSurname;

    public OrderListObject(String orderNumber, String userName, String userSurname) {
        this.orderNumber = orderNumber;
        this.userName = userName;
        this.userSurname = userSurname;
    }

    public AnchorPane getItemView(){
        Label label = new Label();
        label.setText(orderNumber);
        Label label2 = new Label();
        label2.setText(userName+userSurname);
        AnchorPane anchorPane = new AnchorPane();
        VBox vBox = new VBox();
        vBox.getChildren().addAll(label, label2);
        anchorPane.getChildren().add(vBox);
        return anchorPane;
    }
}
