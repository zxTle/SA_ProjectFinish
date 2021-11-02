package sa_project.model;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;

public class ProductsList {

    ArrayList<Products> products;

    public ProductsList() {
        products = new ArrayList<>();
    }

    public void addProduct(Products product){
        products.add(product);
    }

    public void setMenuItem(MenuButton menu, EventHandler<ActionEvent> handler){
        for(Products pr : products){
            MenuItem item = new MenuItem(pr.getProductId()+ ":" +pr.getProductName());
            item.setOnAction(handler);
            menu.getItems().add(item);
        }
    }

    public String getDescription(String id){
        for(Products pr : products){
            if(pr.getProductId().equals(id)){
                return pr.getDescription();
            }
        }
        return null;
    }
}
