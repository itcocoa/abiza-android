package cn.abiza.abiza_android.service;

import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import android.util.Base64;
import android.os.Handler;
import org.json.*;

/**
 * Created by kent on 2015/12/8.
 */
public class Tools {

    Handler MainHandler;

    public Tools( Handler MainHandler){
        this.MainHandler = MainHandler;
    }

    // 执行远程API，并返回数据
    public JSONObject exec(String api, String service, Object params) throws Exception {

        execute execute_thd = new execute(api, service, params);
        execute_thd.start();

        JSONObject rs = null;

        //System.out.println(data);

        return rs;
    }

    private class execute extends Thread
    {
        private String api;
        private String service;
        private Object params;

        public execute(String api, String service, Object params){
            this.api = api;
            this.service = service;
            this.params = params;
        }
        public String encode (String source) {
            return Base64.encodeToString(source.getBytes(),Base64.DEFAULT);
        }
        public void run()
        {
            try {

                URL url = new URL("http://www.malruco.cn/index.php?g=Service&m="+api+"&a="+service);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Basic " + encode( "admin:adminpass"));
                int code = conn.getResponseCode();
                System.out.println(code);
                Scanner scanner = new Scanner(conn.getInputStream());
                String data = scanner.useDelimiter("\\A").next();
                System.out.println(data);

                Message toMain = MainHandler.obtainMessage();
                toMain.obj = "exe_api_"+api+"_service_"+service;
                MainHandler.sendMessage(toMain);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
