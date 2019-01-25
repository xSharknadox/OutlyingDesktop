package ListObjects;

public class RequestForChangeDishHowMuch {
    private long dishId;
    private double howMuch;

    public RequestForChangeDishHowMuch(long dishId, double howMuch) {
        this.dishId = dishId;
        this.howMuch = howMuch;
    }

    public long getDishId() {
        return dishId;
    }

    public void setDishId(long dishId) {
        this.dishId = dishId;
    }

    public double getHowMuch() {
        return howMuch;
    }

    public void setHowMuch(double howMuch) {
        this.howMuch = howMuch;
    }
}
