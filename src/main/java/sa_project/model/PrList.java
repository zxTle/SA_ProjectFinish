package sa_project.model;

import java.util.ArrayList;

public class PrList {
    private ArrayList<PrForm> prList;

    public PrList() {
        prList = new ArrayList<>();
    }
    public void addPrList(PrForm prForm){prList.add(prForm);}
    public ArrayList<PrForm> toList(){ return prList;}
}
