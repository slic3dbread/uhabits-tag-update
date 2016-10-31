package org.isoron.uhabits.activities.habits.edit;

/**
 * Created by tpootool on 30/10/16.
 */

public class Tag {
    private int id;
    private String name;
    private int color;

    public Tag() {

    }

    public Tag( int newID, String newName, int newColor){
        this.id = newID;
        this.name = newName;
        this.color = newColor;
    }

    public Tag( String newName, int newColor){
        this.name = newName;
        this.color = newColor;
    }

    // Setter functions
    public void setId(int newId){
        this.id = newId;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public void setColor(int newColor){
        this.color = newColor;
    }

    // Getter functions
    public int getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public int getColor(){
        return this.color;
    }

}
