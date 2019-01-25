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
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

public class MainDepartmentController {
    @FXML
    VBox vboxOrders;

    @FXML
    VBox vboxDetailOrder;

    @FXML
    ImageView clientImage;

    @FXML
    Label orderNumberFXML, clientNameAndSurnameFXML, workersNameAndSurnameFXML, tableNumberFXML, orderDateFXML, workerRatingFXML, numberOfClientsFXML, orderStageFXML;

    @FXML
    Menu adminMenuFXML;

    private String serverAddress;
    private long tagRestaurant = 2;
    private long numberOfRestaurantDepartment=5;
    private RequestService service = new RequestService();
    private List<Map<String, Integer>> imagesOfClients = new ArrayList<>();
    private List<Image> clientsPhotos = new ArrayList<>();
    private List<UserObject> clientsOfOrders = new ArrayList<>();
    private List<WorkerObject> workerOfOrders = new ArrayList<>();
    private List<OrderObject> orders = new ArrayList<>();
    private DishObject[] dishesOfOrder = new DishObject[0];
    private long selectedOrderId = 0;
    private boolean adminIn = false;


    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setTagRestaurant(long tagRestaurant) {
        this.tagRestaurant = tagRestaurant;
    }

    public void setNumberOfRestaurantDepartment(long numberOfRestaurantDepartment) {
        this.numberOfRestaurantDepartment = numberOfRestaurantDepartment;
    }

    public void setAdminIn(boolean adminIn) {
        this.adminIn = adminIn;
    }

    public void AdminLogin(ActionEvent actionEvent){
        Stage stage = new Stage();
        try {
            startAdminLoginFXML(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAdminMenuAble(boolean able){
       adminMenuFXML.setDisable(!able);
    }

    public void Refresh(ActionEvent actionEvent) {
        vboxOrders.getChildren().clear();
        if(adminIn){
            System.out.println("adminIn");
        }
        else {
            System.out.println("admin Error");
        }
        imagesOfClients = new ArrayList<>();
        clientsPhotos = new ArrayList<>();
        clientsOfOrders = new ArrayList<>();
        workerOfOrders = new ArrayList<>();
        orders = new ArrayList<>();
        UserObject client = new UserObject();
        WorkerObject worker = new WorkerObject();

        OrderObject[] ordersObjects = service.getDepartmentOrders(numberOfRestaurantDepartment+"",serverAddress+"/get_orders_r");

        ObservableList<AnchorPane> observableList = FXCollections.<AnchorPane>observableArrayList();
        for(OrderObject order: ordersObjects){
            worker = service.getOrderWorker(order.getOrderId(), serverAddress+"/get_worker_of_order");
            client = service.getOrderUser(order.getUserId(), serverAddress+"/get_user_of_order");
            Image userPhoto = new Image("/2157d55d35204d4.jpg");
            try {
                userPhoto = SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(client.getPhoto())), null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("orderId", (int)order.getOrderId());
            map.put("clientPhotoListIndex", clientsPhotos.size());
            map.put("clientListIndex", clientsOfOrders.size());
            map.put("workerListIndex", workerOfOrders.size());
            map.put("orderListIndex", orders.size());
            imagesOfClients.add(map);
            clientsPhotos.add(userPhoto);
            clientsOfOrders.add(client);
            workerOfOrders.add(worker);
            orders.add(order);
            observableList.add(getOrderListItemView(order.getOrderId()+"", client.getName()+"", client.getSurname()+"", 228+"", worker.getName(), worker.getSurname()));
        }

        ListView<AnchorPane> listView = new ListView<AnchorPane>(observableList);
        listView.setPrefHeight(900);
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AnchorPane>() {
            @Override
            public void changed(ObservableValue<? extends AnchorPane> observable, AnchorPane oldValue, AnchorPane newValue) {
                seasonChanged(observable, oldValue, newValue);
            }
        });

        vboxOrders.setSpacing(10);
        vboxOrders.getChildren().addAll(listView);

    }


    public void seasonChanged(ObservableValue<? extends AnchorPane> observable,AnchorPane oldValue,AnchorPane newValue)
    {
        ListView<AnchorPane> list;

        for (Map<String, Integer> map: imagesOfClients){
            if((selectedOrderId=map.get("orderId"))== Integer.parseInt(newValue.getId())){
                clientImage.setImage(clientsPhotos.get(map.get("clientPhotoListIndex")));
                WorkerObject worker = workerOfOrders.get(map.get("workerListIndex"));
                UserObject client = clientsOfOrders.get(map.get("clientListIndex"));
                OrderObject order = orders.get(map.get("orderListIndex"));
                setOrderDetailsView(map.get("orderId"),client.getName(), client.getSurname(), worker.getName(), worker.getSurname(), order.getTableNumber(), order.getDate(), 5.0, worker.getRating(), order.getNumberOfPeople(), getReadines(order.getReadines()));
            }
        }
        dishesOfOrder = service.getDishesOfOrder(Long.parseLong(newValue.getId()), serverAddress+"/get_dishes_of_order");

        ObservableList<AnchorPane> orderDishesList = FXCollections.<AnchorPane>observableArrayList();
        for(DishObject dishObject: dishesOfOrder){
            orderDishesList.add(getDishListItemView(dishObject.getDishName(), dishObject.getNumberOfDish()+""));
        }

        list = new ListView(orderDishesList);
        list.setOrientation(Orientation.VERTICAL);
        list.setPrefHeight(900.0);
        vboxDetailOrder.setSpacing(0);
        vboxDetailOrder.getChildren().clear();
        vboxDetailOrder.getChildren().addAll( list);
    }


    private AnchorPane getDishListItemView(String dishName, String howMuchDish){
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(30.0);
        anchorPane.setId(dishName);

        Label label = new Label();
        label.setText(dishName);
        label.setPrefSize(200.0, 30.0);
        AnchorPane.setTopAnchor(label, 0.0);
        AnchorPane.setLeftAnchor(label, 0.0);
        AnchorPane.setRightAnchor(label, 110.0);
        anchorPane.getChildren().add(label);

        label = new Label();
        label.setText("Кількість: "+howMuchDish);
        label.setPrefSize(100.0, 30.0);
        AnchorPane.setTopAnchor(label, 0.0);
        AnchorPane.setRightAnchor(label, 0.0);
        AnchorPane.setLeftAnchor(label, 210.0);
        anchorPane.getChildren().add(label);

        return anchorPane;
    }


    private AnchorPane getOrderListItemView(String orderNumber, String userName, String userSurname, String tableNumber, String waiterName, String waiterSurname){
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(50.0);
        anchorPane.setId(orderNumber);

        Label label = new Label();
        label.setText("Номер замовлення: "+orderNumber);
        label.setPrefSize(225.0, 25.0);
        AnchorPane.setTopAnchor(label, 0.0);
        AnchorPane.setLeftAnchor(label, 0.0);
        anchorPane.getChildren().add(label);

        label = new Label();
        label.setPrefSize(225.0, 25.0);
        AnchorPane.setBottomAnchor(label, 0.0);
        AnchorPane.setLeftAnchor(label, 0.0);
        label.setText("Номер столика: "+tableNumber);
        anchorPane.getChildren().add(label);

        label = new Label();
        label.setText("Клієнт: "+userName+" "+userSurname);
        label.setPrefSize(225.0, 25.0);
        AnchorPane.setTopAnchor(label, 0.0);
        AnchorPane.setRightAnchor(label, 0.0);
        anchorPane.getChildren().add(label);

        label = new Label();
        label.setText("Офіціант: "+waiterName+" "+waiterSurname);
        label.setPrefSize(225.0, 25.0);
        AnchorPane.setBottomAnchor(label, 0.0);
        AnchorPane.setRightAnchor(label, 0.0);
        anchorPane.getChildren().add(label);

        return anchorPane;
    }

    public void setOrderDetailsView(long orderId, String clientName, String clientSurname, String workerName, String workerSurname, int tableNumber, String date, double clientRating, double workerRating, int numberOfClients, String orderStage){
        orderNumberFXML.setText("Номер замовлення: "+orderId);
        clientNameAndSurnameFXML.setText("Клієнт: "+clientName+" "+clientSurname);
        workersNameAndSurnameFXML.setText("Офіціант: "+workerName+" "+workerSurname);
        tableNumberFXML.setText("Номер столика: "+tableNumber);
        orderDateFXML.setText("Дата: "+date);
        workerRatingFXML.setText("Оцінка офіціанта: "+workerRating);
        numberOfClientsFXML.setText("Кількість чоловік: "+numberOfClients);
        orderStageFXML.setText("Стадія заказу: "+orderStage);
    }

    @FXML
    protected void startAdminLoginFXML(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("administrator_login.fxml"));
        Parent root = (Parent) loader.load();
        AdministratorLoginController administratorLoginController = loader.getController();
        administratorLoginController.setServerAddress(serverAddress);
        administratorLoginController.setNumberOfRestaurantDepartment(numberOfRestaurantDepartment);
        administratorLoginController.setTagRestaurant(tagRestaurant);
        administratorLoginController.setMainWindow((Stage) orderNumberFXML.getScene().getWindow());
        stage.setTitle("Hello World");
        stage.setScene(new Scene(root, 240, 300));
        stage.show();
    }

    public void AddNewDepartmentWorkers(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            startAddNewDepartmentWorkersFXML(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void startAddNewDepartmentWorkersFXML(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("add_new_department_workers.fxml"));
        Parent root = (Parent) loader.load();
        AddNewDepartmentWorkersController addNewDepartmentWorkersController = loader.getController();
        addNewDepartmentWorkersController.dataInit(tagRestaurant, numberOfRestaurantDepartment, serverAddress);
        stage.setTitle("Hello World");
        stage.setScene(new Scene(root, 310, 590));
        stage.show();
    }

    private String getReadines(int readinesNumber){
        switch (readinesNumber){
            case 1: return "Нове замовлення";
            case 2: return "Замовлення підтверджено";
            case 3: return "Замовлення готове";
            default: return "помилка";
        }
    }

    public void ChangeTable(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Зміна столика");
        dialog.setHeaderText("Введіть номер столика");
        dialog.setContentText("Введіть номер столика:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            RequestForChangeDishCategory request = new RequestForChangeDishCategory(selectedOrderId, Long.parseLong(result.get()));
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                service.changeDish(objectMapper.writeValueAsString(request),serverAddress+"/change_table_of_order");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public void ChangeWorker(ActionEvent actionEvent) {
    }

    public void ChangeDate(ActionEvent actionEvent) {
    }

    public void ChangeReadines(ActionEvent actionEvent) {
        List<String> choices = new ArrayList<>();
        choices.add("Нове замовлення");
        choices.add("Замовлення підтверджено");
        choices.add("Замовлення готове");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Нове замовлення", choices);
        dialog.setTitle("Готовність замовлення");
        dialog.setHeaderText("Готовність замовлення");
        dialog.setContentText("Виберіть готовність замовлення");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            int res=-1;
            for (int x=0; x<choices.size(); x++) {
                if(choices.get(x).equals(result.get())){
                    res=x+1;
                }
            }
            RequestForChangeDishCategory request = new RequestForChangeDishCategory(selectedOrderId, res);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                service.changeDish(objectMapper.writeValueAsString(request),serverAddress+"/change_readines_of_order");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public void CancelOrder(ActionEvent actionEvent) {
    }
}
