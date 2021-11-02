package sa_project.model;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;

public class CategoryList {
    private ArrayList<Category> categories;

    public CategoryList() {
        this.categories = new ArrayList<>();
    }

    public void addCategory(Category category){
        categories.add(category);
    }

    public ArrayList<Category> toList(){return categories;}

    public void setMenuItem(MenuButton menu, EventHandler<ActionEvent> handler){
        for(Category ca : categories){
            MenuItem item = new MenuItem(ca.getInitial() + ":" + ca.getName());
            item.setOnAction(handler);
            menu.getItems().add(item);
        }
    }

}
