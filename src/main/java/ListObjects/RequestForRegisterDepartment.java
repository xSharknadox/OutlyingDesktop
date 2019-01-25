package ListObjects;

public class RequestForRegisterDepartment {
    private long tagRestaurantId;
    private String login, password;

    public RequestForRegisterDepartment(long tagRestaurantId, String login, String password) {
        this.tagRestaurantId = tagRestaurantId;
        this.login = login;
        this.password = password;
    }

    public long getTagRestaurantId() {
        return tagRestaurantId;
    }

    public void setTagRestaurantId(long tagRestaurantId) {
        this.tagRestaurantId = tagRestaurantId;
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
