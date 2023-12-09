package com.example.raggingcomplaint;

public class Model {
    String id,title,date,description,complaintid;

    public Model() {
    }
public void setcompid(String compid){
        this.complaintid=compid;
}
public String getcompid(){
        return complaintid;
}
    public Model(String id, String title, String date, String description) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
