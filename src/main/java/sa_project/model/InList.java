package sa_project.model;

import java.util.ArrayList;

public class InList {
    private ArrayList<InForm> inList;

    public InList() { inList = new ArrayList<>(); }
    public void addInList(InForm inForm) { inList.add(inForm); }
    public ArrayList<InForm> toList() { return inList; }
}
