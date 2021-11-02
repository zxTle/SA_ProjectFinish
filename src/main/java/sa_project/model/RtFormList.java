package sa_project.model;

import java.util.ArrayList;

public class RtFormList {
    private ArrayList<RtForm> returnFormList;

    public RtFormList() {
        returnFormList =  new ArrayList<>();
    }

    public void addRtForm(RtForm returnForm){returnFormList.add(returnForm);}
    public ArrayList<RtForm> toList(){return  returnFormList;}
}
