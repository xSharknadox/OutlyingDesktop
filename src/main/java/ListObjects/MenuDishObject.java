package ListObjects;

public class MenuDishObject{
    private long dishId, tagRestaurantId, menuCategoryId;
    private String name, ingredients, units, menuCategoryName;
    private double price, howMuch;
    private byte[] photo;

    public MenuDishObject() {
    }

    public long getDishId() {
        return dishId;
    }

    public void setDishId(long dishId) {
        this.dishId = dishId;
    }

    public long getTagRestaurantId() {
        return tagRestaurantId;
    }

    public void setTagRestaurantId(long tagRestaurantId) {
        this.tagRestaurantId = tagRestaurantId;
    }

    public long getMenuCategoryId() {
        return menuCategoryId;
    }

    public void setMenuCategoryId(long menuCategoryId) {
        this.menuCategoryId = menuCategoryId;
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

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getHowMuch() {
        return howMuch;
    }

    public void setHowMuch(double howMuch) {
        this.howMuch = howMuch;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getMenuCategoryName() {
        return menuCategoryName;
    }

    public void setMenuCategoryName(String menuCategoryName) {
        this.menuCategoryName = menuCategoryName;
    }
}
