package sa_project.model;

public class ProductForm {
    private String productID;
    private String productType;
    private String productName;
    private String description;

    public ProductForm(String productID, String productType, String productName, String description) {
        this.productID = productID;
        this.productType = productType;
        this.productName = productName;
        this.description = description;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductID() {
        return productID;
    }

    public String getProductType() {
        return productType;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }
}

