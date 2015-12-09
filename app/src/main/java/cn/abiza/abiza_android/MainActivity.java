package cn.abiza.abiza_android;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.abiza.abiza_android.service.*;
import org.json.*;

public class MainActivity extends AppCompatActivity
{
    Handler MainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainHandler = new Handler()
        {

            @Override
            public void handleMessage( Message msg) {
                if ((String)msg.obj == "welcome_end"){
                    if (islogined()){
                        setContentView(R.layout.activity_main);
                    }else{
                        setContentView(R.layout.login);
                    }
                }

                if ((String)msg.obj == "exe_api_User_service_login") {
                    TextView textView6 = (TextView)findViewById(R.id.textView6);
                    textView6.setText("哈哈");
                }
            }

        };

        setContentView(R.layout.welcome);

        CountThread count_thd = new CountThread();
        count_thd.start();

    }

    public void login(View view) throws Exception {
        System.out.println("我被点击了！");
        // 这里开始验证用户名密码

        Tools tool = new Tools(MainHandler);
        JSONObject data = tool.exec("User","login","");


        //String t = data.getJSONObject("data").getString("username");

        //System.out.println(t);

    }

    private boolean islogined(){
        return !true;
    }

    private class CountThread extends Thread
    {
        public void run()
        {
            try {
                sleep(3000);
                Message toMain = MainHandler.obtainMessage();
                toMain.obj = "welcome_end";
                MainHandler.sendMessage(toMain);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
