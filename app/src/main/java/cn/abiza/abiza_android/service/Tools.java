package cn.abiza.abiza_android.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import org.json.*;

/**
 * Created by kent on 2015/12/8.
 */
public class Tools {
    // 执行远程API，并返回数据
    public void exec(String api, String service, JSONObject params,Handler MainHandler,UIProccessor backCall) throws Exception {

        execute execute_thd = new execute(api, service, params,MainHandler,backCall);
        execute_thd.start();

    }

    private class execute extends Thread
    {
        private String api;
        private String service;
        private JSONObject params;
        private Handler MainHandler;
        private UIProccessor backCall;

        public execute(String api, String service, JSONObject params,Handler MainHandler,UIProccessor backCall){
            this.api = api;
            this.service = service;
            this.params = params;
            this.MainHandler = MainHandler;
            this.backCall = backCall;
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
                conn.setRequestProperty("Authorization", "Basic " + encode("admin:adminpass"));

                conn.setDoOutput(true);// 是否输入参数

                StringBuffer params_form = new StringBuffer();

                JSONArray names = params.names();
                for (int i = 0;i < names.length(); ++i){

                    System.out.println(names.getString(i) + ":" + params.getString(names.getString(i)));

                    if (i!=0) params_form.append("&");
                    params_form.append(names.getString(i)).append("=").append(params.getString(names.getString(i)));

                }

                byte[] bypes = params_form.toString().getBytes();
                conn.getOutputStream().write(bypes);// 输入参数

                int code = conn.getResponseCode();
                System.out.println(code);
                Scanner scanner = new Scanner(conn.getInputStream());
                String data = scanner.useDelimiter("\\A").next();
                System.out.println(data);

                JSONObject jdata = new JSONObject(data);
                backCall.data = jdata;

                Message toMain = MainHandler.obtainMessage();
                toMain.obj = backCall;
                MainHandler.sendMessage(toMain);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
