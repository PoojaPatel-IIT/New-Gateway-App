package com.example.pooja.newsgatewayapp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class newsgatewayservice extends Service {

    private HashMap<String,ArrayList<String>> arrayListHashMap = new HashMap<>();
    static final String SERVICE_DATA = "SERVICE_DATA";
    private ArrayList<String> hd = new ArrayList<>();


    private static final String TAG = "newsgatewayservice";
    private boolean ssss = true;
    static final String ACTION_MSG = "ACTION_MSG_TO_SERVICE";
    private recieve_news rcv;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        ssss = false;
        unregisterReceiver(rcv);
        super.onDestroy();
    }


    class recieve_news extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            String src = "";
            if(intent.getAction().equals(ACTION_MSG))
            {
                if(intent.hasExtra(MainActivity.EXTRA))
                {
                    src = intent.getStringExtra(MainActivity.EXTRA);
                    src = src.replaceAll("\\s","");
                    new newsArticlesDownloader(newsgatewayservice.this).
                            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,src);
                }
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        IntentFilter intentFilter = new IntentFilter(ACTION_MSG);
        int cu = 0;
        rcv = new recieve_news();
        cu++;
        registerReceiver(rcv,intentFilter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(ssss == true)
                {
                    if(arrayListHashMap.isEmpty()==true)
                    {
                        try
                        {
                            Thread.sleep(250);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else if(arrayListHashMap.isEmpty()==false)
                    {
                        Intent intent1 = new Intent();
                        intent1.setAction(MainActivity.ACTION);
                        int cu = 0;
                        intent1.putStringArrayListExtra(SERVICE_DATA,arrayListHashMap.get("Author"));
                        cu++;
                        sendBroadcast(intent1);
                        arrayListHashMap.clear();
                    }
                }
            }
        }).start();
        return Service.START_STICKY;
    }
    public void newsset(HashMap<String,ArrayList<String>> s)
    {
        arrayListHashMap.clear();

        arrayListHashMap.putAll(s);

        hd.addAll(arrayListHashMap.get("Title"));
    }



}
