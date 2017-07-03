/*package com.example.asus.story;

import com.orm.SugarRecord;

/**
 * Created by asus on 6/28/2017.
 */
/*
public class Story extends SugarRecord {
    public String title,desc;
    public byte[] img;
    Story(){ }
    Story(String title,String desc,byte[] img)
    {
    }
    //getters and setters
    //public int getID(){return id;}
    public String getTitle(){
        return title;
    }
    public String getDesc(){
        return desc;
    }
    public byte[] getImg(){
        return img;
    }

    //public void setID(int id) {this.id = id;}

    public void setTitle(String title){
        this.title = title;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
    public void setImg(byte[] img){
        this.img = img;
    }

}*/

package com.example.asus.story;

/**
 * Created by asus on 6/19/2017.
 */

public class Story {

    //private variables
    int _id;
    String _caption, _desc;

    byte[] _img;

    // Empty constructor
    public Story(){

    }
    // constructor
    public Story(int id, String caption, byte[] img, String desc){
        this._id = id;
        this._caption = caption;
        this._img = img;
        this._desc = desc;
    }

    // constructor
    public Story(String caption, byte[] img, String desc){

        this._caption = caption;
        this._img = img;
        this._desc = desc;

    }

    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting first name
    public String getCaption(){
        return this._caption;
    }

    // setting first name
    public void setcaption(String caption){
        this._caption = caption;
    }

    // getting first name
    public String getDesc(){
        return this._desc;
    }

    // setting first name
    public void setdesc(String desc){
        this._desc = desc;
    }

    //getting profile pic
    public byte[] getImage(){
        return this._img;
    }

    //setting profile pic

    public void setImage(byte[] b){
        this._img=b;
    }
}

