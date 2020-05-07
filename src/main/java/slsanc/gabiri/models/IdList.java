package slsanc.gabiri.models;

import java.util.ArrayList;


/* This class exists to create objects that help the program to pass lists of applicants and positions. It can contain
many IDs in an ArrayList. It can also store a single ID as an int in order to model a many-to-one relationship.*/

public class IdList {

    private ArrayList<Integer> idList;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getIdList() {
        return idList;
    }

    public void setIdList(ArrayList<Integer> idList) {
        this.idList = idList;
    }

    public IdList(int id) {
        this.id = id;
    }

    public IdList() {}
}
