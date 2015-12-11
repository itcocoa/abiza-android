package cn.abiza.abiza_android;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.abiza.abiza_android.service.*;
import org.json.*;

public class MainActivity extends AppCompatActivity
{
    Handler MainHandler;

    public static final String USER_PREFS = "USER_PREFS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainHandler = new Handler()
        {

            @Override
            public void handleMessage( Message msg) {


                if (msg.obj instanceof UIProccessor){
                    // Tools.exec() 回调
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

        EditText editText = (EditText)findViewById(R.id.editText);
        EditText editText2 = (EditText)findViewById(R.id.editText2);
        JSONObject user_data = new JSONObject();
        user_data.put("username", editText.getText());
        user_data.put("password",MD5Util.string2MD5(editText2.getText().toString()));

        tool.exec("User", "login", user_data, this.MainHandler, new UIProccessor() {
            @Override
            public void onDataloaded(JSONObject data) {
                try {
                    if (data.getString("type").equals("error")) {
                        textView6.setText("登录失败" + data.getString("type"));
                    } else {
                        textView6.setText("登录成功" + data.getString("type"));
                        setLogined(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        //String t = data.getJSONObject("data").getString("username");

        //System.out.println(t);

    }

    private boolean islogined(){
        SharedPreferences user_prefs = getSharedPreferences(USER_PREFS, 0);
        boolean logined = user_prefs.getBoolean("logined", false);
        return logined;
    }

    private boolean setLogined( boolean flag){
        SharedPreferences user_prefs = getSharedPreferences(USER_PREFS, 0);
        SharedPreferences.Editor editor = user_prefs.edit();
        editor.putBoolean("logined", flag);

        // Commit the edits!
        editor.commit();

        return flag;
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
