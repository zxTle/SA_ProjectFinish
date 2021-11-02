package sa_project.model;

public class Category {
    private String name;
    private String initial;

    public Category(String name, String initial) {
        this.name = name;
        this.initial = initial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getInitial() {
        return initial;
    }
}
