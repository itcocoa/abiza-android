package cn.abiza.abiza_android.service;

import org.json.*;

/**
 * Created by kent on 2015/12/8.
 */
public class Tools {

    public JSONObject exec(String api, String service, Object params)
    {
        JSONObject rs = null;
        try {
            rs = new JSONObject("{\"type\":\"error\",\"data\":{\"username\":\"\\u4f60\\u597d\",\"password\":\"abc123\"}}");
        }catch (Exception e) {

        }

        return rs;
    }

}
