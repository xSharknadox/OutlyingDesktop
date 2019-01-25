package ListObjects;

public class RequestForCreateRestaurantInfo {
    private long tagId;
    private String name;
    private byte[] photo;
    private String photoFormat;
    private byte[] logo;
    private String logoFormat;

    public RequestForCreateRestaurantInfo() {
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoFormat() {
        return logoFormat;
    }

    public void setLogoFormat(String logoFormat) {
        this.logoFormat = logoFormat;
    }
}
