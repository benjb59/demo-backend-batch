package fr.home.batch.domain;

import javax.persistence.Id;

import org.json.simple.JSONObject;


public class Questionnaire {

    @Id
    private String id;

    private JSONObject data;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JSONObject getData() {
        return this.data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
    

}
