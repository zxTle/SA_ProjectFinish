package sa_project.model;

public class Products {
    private String productId;
    private String productType;
    private String productName;
    private String description;

    public Products(String productId, String productType, String productName, String description) {
        this.productId = productId;
        this.productType = productType;
        this.productName = productName;
        this.description = description;
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
