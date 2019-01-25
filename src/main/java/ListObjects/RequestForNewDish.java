package ListObjects;

public class RequestForNewDish {
    private long restaurantDepartmentId;
    private byte[] photo;
    private String photoFormat;
    private String name, ingredients;
    private double howMuch;
    private String units;
    private long category;
    private double price;

    public RequestForNewDish() {
    }

    public long getRestaurantDepartmentId() {
        return restaurantDepartmentId;
    }

    public void setRestaurantDepartmentId(long restaurantDepartmentId) {
        this.restaurantDepartmentId = restaurantDepartmentId;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoFormat() {
        return photoFormat;
    }

    public void setPhotoFormat(String photoFormat) {
        this.photoFormat = photoFormat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public double getHowMuch() {
        return howMuch;
    }

    public void setHowMuch(double howMuch) {
        this.howMuch = howMuch;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
