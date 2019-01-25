package ListObjects;

public class RequestForAddNewDepartment {
    private long tagRestaurant;
    private String login, password;
    private String country, region, city, street;
    private int house;

    public RequestForAddNewDepartment(long tagRestaurant, String login, String password, String country, String region, String city, String street, int house) {
        this.tagRestaurant = tagRestaurant;
        this.login = login;
        this.password = password;
        this.country = country;
        this.region = region;
        this.city = city;
        this.street = street;
        this.house = house;
    }

    public long getTagRestaurant() {
        return tagRestaurant;
    }

    public void setTagRestaurant(long tagRestaurant) {
        this.tagRestaurant = tagRestaurant;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouse() {
        return house;
    }

    public void setHouse(int house) {
        this.house = house;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
