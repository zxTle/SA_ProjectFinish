package sa_project.model;

import java.util.ArrayList;

public class ProductsDocList {
    private ArrayList<ProductDoc> productList;

    public ProductsDocList() {
        this.productList = new ArrayList<>();
    }

    public void addProduct(ProductDoc product){
        productList.add(product);
    }
    public ArrayList<ProductDoc> toList(){return productList;}

    public String toInsert (String rqNo){
        String text="";
        for(ProductDoc prDoc : productList){
            if(prDoc.getItemNum() == productList.size()){
                text += "("+ prDoc.getItemNum()+","+"'"+rqNo+"'"+","+"'"+prDoc.getProductId()+"'"+","+prDoc.getQuantity()+")"+";";
            }
            else{
                text += "("+ prDoc.getItemNum()+","+"'"+rqNo+"'"+","+"'"+prDoc.getProductId()+"'"+","+prDoc.getQuantity()+")"+",";
            }
        }
        return text;
    }

    public void toReceive (int inItem, String textField){
        for(ProductDoc prDoc : productList) {
            if(prDoc.getItemNum() == inItem) {
                prDoc.setReceiveQty(Integer.valueOf(textField));
                prDoc.setBadQty(prDoc.getQuantity()-Integer.valueOf(textField));
                prDoc.setReceiveStr(textField);
            }
        }
    }
    public void toSetReason(int num,String textField){
        for (ProductDoc pr : productList){
            if(pr.getItemNum() == num) {
                pr.setClaimsReason(textField);
            }
        }
    }
    public String toInsertReceive(String inNo){
        String text ="";
        for (ProductDoc prDoc : productList){
            if(prDoc.getItemNum() == productList.size()){
                text += "("+ prDoc.getItemNum()+","+"'"+inNo+"'"+","+"'"+prDoc.getProductId()+"'"+","+prDoc.getReceiveQty()+")"+";";
            }
            else{
                text += "("+ prDoc.getItemNum()+","+"'"+inNo+"'"+","+"'"+prDoc.getProductId()+"'"+","+prDoc.getReceiveQty()+")"+",";
            }
        }
        return text;
    }

    public void setToTableIn (){
        for(ProductDoc pr : productList){
            pr.setReceiveStr(String.valueOf(pr.getReceiveQty()));
        }
    }

    public boolean checkIfScrap(){
        for (ProductDoc productDoc : productList){
            if(productDoc.getBadQty() > 0){
                return true;
            }
        }
        return false;
    }
    public ProductsDocList getrtFormList(){
        ProductsDocList rtList =  new ProductsDocList();
        int i =1;
        for (ProductDoc pr : productList){
            if(pr.getBadQty() > 0){
                pr.setItemNum(i);
                rtList.addProduct(pr);
            }
            i++;
        }
        return rtList;
    }

    public String toInsertClaim (String rtNo){
        String text="";
        for(ProductDoc prDoc : productList){
            if(prDoc.getItemNum() == productList.size()){
                text += "("+ prDoc.getItemNum()+","+"'"+rtNo+"'"+","+"'"+prDoc.getProductId()+"'"+","+prDoc.getBadQty()+
                        ","+"''"+")"+";";
            }
            else{
                text += "("+ prDoc.getItemNum()+","+"'"+rtNo+"'"+","+"'"+prDoc.getProductId()+"'"+","+prDoc.getBadQty()+
                        ","+"''"+")"+",";
            }
        }
        return text;
    }
    public boolean checkIfEnough(){
        for (ProductDoc productDoc : productList){
            if(productDoc.getItemNumForecast() < 0){
                return false;
            }
        }
        return true;
    }

}
