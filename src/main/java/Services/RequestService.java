package Services;


import ListObjects.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class RequestService {

    public RequestService() {
    }

    public OrderObject[] getDepartmentOrders(String numberOfRestaurantDepartment, String url){
        OrderObject[] orders = new OrderObject[0];
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(numberOfRestaurantDepartment);
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse result = httpClient.execute(request);

            String json = EntityUtils.toString(result.getEntity(), "UTF-8");

            ObjectMapper mapper = new ObjectMapper();
            orders = mapper.readValue(json, OrderObject[].class);

        } catch (IOException ex) {
        }
        return orders;
    }

    public WorkerObject getOrderWorker(long orderId, String url){
        WorkerObject workerObject = new WorkerObject();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(orderId+"");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse result = httpClient.execute(request);

            String json = EntityUtils.toString(result.getEntity(), "UTF-8");

            ObjectMapper mapper = new ObjectMapper();
            workerObject = mapper.readValue(json, WorkerObject.class);

        } catch (IOException ex) {
        }
        return workerObject;
    }

    public UserObject getOrderUser(long userId, String url){
        UserObject userObject = new UserObject();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(userId+"");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse result = httpClient.execute(request);

            String json = EntityUtils.toString(result.getEntity(), "UTF-8");

            ObjectMapper mapper = new ObjectMapper();
            userObject = mapper.readValue(json, UserObject.class);

        } catch (IOException ex) {
        }
        return userObject;
    }

    public DishObject[] getDishesOfOrder(long orderId, String url){
        DishObject[] dishesOfOrder = new DishObject[0];
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(orderId+"");

            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse result = httpClient.execute(request);

            String json = EntityUtils.toString(result.getEntity(), "UTF-8");

            ObjectMapper mapper = new ObjectMapper();
            dishesOfOrder = mapper.readValue(json, DishObject[].class);

        } catch (IOException ex) {
        }
        return dishesOfOrder;
    }

    public MenuDishObject[] getMenuDishes(long restaurantDepartmentId, String url) {
        MenuDishObject[] dishes = new MenuDishObject[0];
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(restaurantDepartmentId+"");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse result = httpClient.execute(request);

            String json = EntityUtils.toString(result.getEntity(), "UTF-8");

            ObjectMapper mapper = new ObjectMapper();
            dishes = mapper.readValue(json, MenuDishObject[].class);

        } catch (IOException ex) {
        }
        return dishes;
    }

    public void changeDish(String jsonRequest, String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(jsonRequest, Charset.forName("UTF-8"));
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse result = httpClient.execute(request);

        } catch (IOException ex) {
        }
    }

    public long addNewDepartment(String jsonRequest, String url) {
        long restaurantDepartment=-1;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(jsonRequest, Charset.forName("UTF-8"));
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse result = httpClient.execute(request);

            String json = EntityUtils.toString(result.getEntity(), "UTF-8");

            ObjectMapper mapper = new ObjectMapper();
            restaurantDepartment = mapper.readValue(json, Long.class);
        } catch (IOException ex) {
        }
        return restaurantDepartment;
    }

    public RestaurantMenuCategoryObject[] getCatergories(long restaurantDepartmentId, String url){
        RestaurantMenuCategoryObject[] restaurantMenuCategoryObjects = new RestaurantMenuCategoryObject[0];
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(restaurantDepartmentId+"");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse result = httpClient.execute(request);

            String json = EntityUtils.toString(result.getEntity(), "UTF-8");

            ObjectMapper mapper = new ObjectMapper();
            restaurantMenuCategoryObjects = mapper.readValue(json, RestaurantMenuCategoryObject[].class);

        } catch (IOException ex) {
        }
        return restaurantMenuCategoryObjects;
    }

    public List<WorkerObject> getAllWorkers(long restaurantDepartmentId, String url){
        List<WorkerObject> workersObject = new ArrayList<>();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(restaurantDepartmentId+"");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse result = httpClient.execute(request);

            String json = EntityUtils.toString(result.getEntity(), "UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            WorkerObject[] workersArr = mapper.readValue(json, WorkerObject[].class);
            for (WorkerObject worker: workersArr){
                workersObject.add(worker);
            }

        } catch (IOException ex) {
        }
        return workersObject;
    }

    public boolean checkForUniqueDepartmentLogin(String url, String login){
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(login + "");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse result = httpClient.execute(request);

            String json = EntityUtils.toString(result.getEntity(), "UTF-8");
            if(json.equals("true")){
                return true;
            }else return false;
        } catch (IOException ex) {
        }
        return true;
    }

    public long registerNewRestaurant(String jsonRequest, String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(jsonRequest, Charset.forName("UTF-8"));
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse result = httpClient.execute(request);

            String json = EntityUtils.toString(result.getEntity(), "UTF-8");
            return Long.parseLong(json);
        } catch (IOException ex) {
        }
        return -1;
    }

    public DepartmentLoginObject DepartmentLogin(String jsonRequest, String url) {
        DepartmentLoginObject restaurantDepartment= new DepartmentLoginObject();
        restaurantDepartment.setDepartmentId(-2);
        restaurantDepartment.setTagId(-2);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(jsonRequest, Charset.forName("UTF-8"));
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse result = httpClient.execute(request);

            String json = EntityUtils.toString(result.getEntity(), "UTF-8");

            ObjectMapper mapper = new ObjectMapper();
            restaurantDepartment = mapper.readValue(json, DepartmentLoginObject.class);
        } catch (IOException ex) {
        }
        return restaurantDepartment;
    }

    public int AdminLogin(String jsonRequest, String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(jsonRequest, Charset.forName("UTF-8"));
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse result = httpClient.execute(request);

            String json = EntityUtils.toString(result.getEntity(), "UTF-8");

            ObjectMapper mapper = new ObjectMapper();
            return  mapper.readValue(json, Integer.class);
        } catch (IOException ex) {
        }
        return -2;
    }

    public long RestaurantLogin(String jsonRequest, String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(jsonRequest, Charset.forName("UTF-8"));
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse result = httpClient.execute(request);

            String json = EntityUtils.toString(result.getEntity(), "UTF-8");
            return Long.parseLong(json);
        } catch (IOException ex) {
        }
        return -2;
    }

    public List<DepartmentObject> getDepartments(String jsonRequest, String url) {
        List<DepartmentObject> restaurantDepartments= new ArrayList<>();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(jsonRequest, Charset.forName("UTF-8"));
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse result = httpClient.execute(request);

            String json = EntityUtils.toString(result.getEntity(), "UTF-8");

            ObjectMapper mapper = new ObjectMapper();
            DepartmentObject[] departments = mapper.readValue(json, DepartmentObject[].class);
            for(DepartmentObject dep: departments){
                restaurantDepartments.add(dep);
            }
            return restaurantDepartments;
        } catch (IOException ex) {
        }
        return restaurantDepartments;
    }

}
