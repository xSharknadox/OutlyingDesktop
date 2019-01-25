package ListObjects;

public class RequestForChangeDishCategory {
    private long dishId, categoryId;

    public RequestForChangeDishCategory(long dishId, long categoryId) {
        this.dishId = dishId;
        this.categoryId = categoryId;
    }

    public long getDishId() {
        return dishId;
    }

    public void setDishId(long dishId) {
        this.dishId = dishId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
}
