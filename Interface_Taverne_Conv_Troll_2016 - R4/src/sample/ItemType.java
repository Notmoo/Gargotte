package sample;

import java.util.ArrayList;

/**
 * Created by Guillaume on 07/08/2016.
 */
public class ItemType extends ArrayList {

    private String name;

    public ItemType() {
        super();
        name = "error";
    }

    public ItemType(String name){
        super();
        this.name = name;
    }

    public void setName(String name){ this.name = name;}
    public String getName(){ return name; }
}
