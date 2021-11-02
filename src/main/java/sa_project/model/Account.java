package sa_project.model;

public class Account {
    private String username;
    private String name;

    public Account(String username, String name) {
        this.username = username;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return  "name=" + username;
    }
}
