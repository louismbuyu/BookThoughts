package com.example.louisnelsonlevoride.bookthoughts.Onboarding;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.louisnelsonlevoride.bookthoughts.NavigationActivity;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.CurrentUserSingleton;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    SlideViewAdapter slideViewAdapter;
    CircleIndicator indicator;
    private String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewPage_id);
        indicator = findViewById(R.id.circleIndicator);
        slideViewAdapter = new SlideViewAdapter(this, this);
        viewPager.setAdapter(slideViewAdapter);
        indicator.setViewPager(viewPager);

        try {
            final Socket socket;
            socket = IO.socket("https://guarded-ridge-59458.herokuapp.com/");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.i(TAG,"EVENT_CONNECT");
                    //socket.emit("foo", "hi");
                    //socket.disconnect();
                }

            }).on("event", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.i(TAG,"");
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.i(TAG,"");
                }

            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String username = CurrentUserSingleton.getInstance().getUsername(this);
        String userId = CurrentUserSingleton.getInstance().getUserId(this);
        if (username != null && userId != null) {
            Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
            startActivity(intent);
        }
    }

    public void enterApp(){
        Intent intent = new Intent(this, IntroActivity.class);
        this.startActivity (intent);
    }

    public void nextAction(){
        int i = viewPager.getCurrentItem();
        i++;
        viewPager.setCurrentItem(i,true);
    }
}
