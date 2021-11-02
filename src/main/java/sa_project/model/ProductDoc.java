package sa_project.model;

public class ProductDoc {
    private int itemNum;
    private String productId;
    private String productName;
    private String description;
    private int quantity;
    private String productType;
    private int onHand;
    private int itemNumForecast;
    private String claimsReason;
    private int receiveQty;
    private String receiveStr = String.valueOf(receiveQty);
    private int badQty;

    public ProductDoc(int itemNum, String productId, String productName, String description, int quantity, String productType, int onHand, int itemNumForecast, String claimsReason) {
        this.itemNum = itemNum;
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
        this.productType = productType;
        this.onHand = onHand;
        this.itemNumForecast = itemNumForecast;
        this.claimsReason = claimsReason;
    }

    public ProductDoc(int itemNum, String productId, String productName, String description, int quantity, int receiveQty,int badQty) {
        this.itemNum = itemNum;
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
        this.receiveQty = receiveQty;
        receiveStr = "";
        this.badQty = badQty;
    }
    public ProductDoc(int itemNum, String productId, String productName, String description, int quantity, String productType, int onHand, int itemNumForecast) {
        this.itemNum = itemNum;
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
        this.productType = productType;
        this.onHand = onHand;
        this.itemNumForecast = itemNumForecast;
    }

    public String getReceiveStr() {
        return receiveStr;
    }

    public void setReceiveStr(String receiveStr) {
        this.receiveStr = receiveStr;
    }

    public int getReceiveQty() {
        return receiveQty;
    }

    public void setReceiveQty(int receiveQty) {
        this.receiveQty = receiveQty;
    }

    public int getBadQty() {
        return badQty;
    }

    public void setBadQty(int badQty) {
        this.badQty = badQty;
    }

    public String getClaimsReason() {
        return claimsReason;
    }

    public void setClaimsReason(String claimsReason) {
        this.claimsReason = claimsReason;
    }

    public void setOnHand(int onHand) {
        this.onHand = onHand;
    }

    public void setItemNumForecast(int itemNumForecast) {
        this.itemNumForecast = itemNumForecast;
    }

    public int getOnHand() {
        return onHand;
    }

    public int getItemNumForecast() {
        return itemNumForecast;
    }

    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getAvailableProduct(){
        return itemNum - quantity;
    }
}
