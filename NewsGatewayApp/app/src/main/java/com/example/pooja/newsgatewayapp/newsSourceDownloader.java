package com.example.pooja.newsgatewayapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by pooja on 12,November,2018
 */
public class newsSourceDownloader extends AsyncTask<String,Void,String>
{
    private static final String TAG = "newsSourceDownloader";
    private final String mykey = "b479b23e95bd42b58b432d0e98eb18cc";



    private String catstr = "";
    private MainActivity mainActivity;
    private ArrayList<String> sou = new ArrayList<>();
    private ArrayList<String> source = new ArrayList<>();
    private ArrayList<String> ctgy = new ArrayList<>();
    private ArrayList<String> id =new ArrayList<>();
    private final String NewsURL = "https://newsapi.org/v2/sources?country=us";

    @Override
    protected String doInBackground(String... strings)
    {
        String z = strings[0];
        if(z.equals("Category"))
        { catstr = strings[1];
            Uri.Builder builder = Uri.parse(NewsURL).buildUpon();
            builder.appendQueryParameter("category",catstr);

            builder.appendQueryParameter("apikey",mykey);
            String urlUse = builder.build().toString();
            StringBuilder stringBuilder = new StringBuilder();
            try
            {
                URL url = new URL(urlUse);
                Log.d(TAG, "doInBackground: "+urlUse);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String l;
                while((l=bufferedReader.readLine())!=null)
                {
                    Log.d(TAG, "doInBackground: "+l);
                    stringBuilder.append(l);
                    stringBuilder.append('\n');
                }
            }
            catch (Exception e)
            {
                return null;
            }
            parser(stringBuilder.toString());
        }
        else if(z.equals("Category")==false)
        {
            Uri.Builder builder = Uri.parse(NewsURL).buildUpon();
            builder.appendQueryParameter("apikey",mykey);
            String urluse = builder.build().toString();
            Log.d(TAG, "doInBackground: THE URL FOR NEW SOURCES : " +urluse);
            StringBuilder stringBuilder = new StringBuilder();
            try
            {
                URL url =new URL(urluse);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String l;
                while((l=bufferedReader.readLine())!=null) {
                    stringBuilder.append(l);
                    stringBuilder.append('\n');
                }
            }
            catch (Exception e)
            {
                return null;
            }
            parser(stringBuilder.toString());
        }
        return null;
    }
    private void parser(String s)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = (JSONArray) jsonObject.get("sources");
            int i=0;
            while (i<jsonArray.length())
            {
                sou.add(jsonArray.getString(i));
                int j1 = 0;
                JSONObject jsonObject1 = new JSONObject(sou.get(i));
                j1++;
                source.add(jsonObject1.getString("name"));
                j1++;
                id.add(jsonObject1.getString("id"));
                if(!(ctgy.contains(jsonObject1.getString("category"))))
                {
                    ctgy.add(jsonObject1.getString("category"));
                }
                i++;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        mainActivity.gettingNewsSrcs(source,ctgy,id);
        Log.d(TAG, "onPostExecute: sources " +source);
        Log.d(TAG, "onPostExecute: sources " +ctgy);
        Log.d(TAG, "onPostExecute: sources " +id);


    }
    public newsSourceDownloader(MainActivity ma)
    {
        mainActivity = ma;
    }
}

