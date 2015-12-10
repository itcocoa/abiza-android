package cn.abiza.abiza_android.service;

import org.json.JSONObject;

/**
 * Created by kent on 2015/12/10.
 */
public class UIProccessor{

    public JSONObject data;

    public void proc(){
        onDataloaded(data);
    };
    public void onDataloaded(JSONObject data){
        System.out.println("Not override !");
    };
}
