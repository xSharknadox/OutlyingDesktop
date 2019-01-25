package ListObjects;

public class RequestForChangeDishPhoto {
    private long dishId;
    private byte[] photoBytes;
    private String photoFormat;

    public RequestForChangeDishPhoto(long dishId, byte[] photoBytes, String photoFormat) {
        this.dishId = dishId;
        this.photoBytes = photoBytes;
        this.photoFormat = photoFormat;
    }

    public long getDishId() {
        return dishId;
    }

    public void setDishId(long dishId) {
        this.dishId = dishId;
    }

    public byte[] getPhotoBytes() {
        return photoBytes;
    }

    public void setPhotoBytes(byte[] photoBytes) {
        this.photoBytes = photoBytes;
    }

    public String getPhotoFormat() {
        return photoFormat;
    }

    public void setPhotoFormat(String photoFormat) {
        this.photoFormat = photoFormat;
    }
}
