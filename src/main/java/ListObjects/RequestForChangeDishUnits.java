package ListObjects;

public class RequestForChangeDishUnits {
    private long dishId;
    private String units;

    public RequestForChangeDishUnits(long dishId, String units) {
        this.dishId = dishId;
        this.units = units;
    }

    public long getDishId() {
        return dishId;
    }

    public void setDishId(long dishId) {
        this.dishId = dishId;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
