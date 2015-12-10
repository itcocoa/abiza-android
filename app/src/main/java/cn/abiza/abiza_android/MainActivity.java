package cn.abiza.abiza_android;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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


                if (msg.obj instanceof UIProccessor){
                    ((UIProccessor) msg.obj).proc();
                }else{
                    if ((String)msg.obj == "welcome_end"){
                        if (islogined()){
                            setContentView(R.layout.activity_main);
                        }else{
                            setContentView(R.layout.login);
                        }
                    }
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

        final TextView textView6 = (TextView)findViewById(R.id.textView6);
        textView6.setText("我被点击了！");
        Tools tool = new Tools();
        JSONObject data = tool.exec("User","login","",this.MainHandler,new UIProccessor(){
            @Override
            public void onDataloaded(JSONObject data){
                textView6.setText(data.toString());
            }
        });


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
