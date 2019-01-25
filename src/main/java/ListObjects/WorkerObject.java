package ListObjects;

public class WorkerObject {
    private long worderId;
    private String name, surname;
    private double rating;
    private boolean admin;

    public WorkerObject() {
    }

    public long getWorderId() {
        return worderId;
    }

    public void setWorderId(long worderId) {
        this.worderId = worderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
