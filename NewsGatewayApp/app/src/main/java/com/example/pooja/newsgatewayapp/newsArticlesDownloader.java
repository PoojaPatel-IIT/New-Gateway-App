package com.example.pooja.newsgatewayapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by pooja on 12,November,2018
 */
public class newsArticlesDownloader  extends AsyncTask<String,Void,String>
{
    private static final String TAG = "MyAsyncArticle";
    private ArrayList<String> web1 = new ArrayList<>();
    private ArrayList<String> t = new ArrayList<>();
    private final String NewsURL = "https://newsapi.org/v2/top-headlines";
    private newsgatewayservice serviceNews;
    private final String mykey = "b479b23e95bd42b58b432d0e98eb18cc";

    public HashMap<String, ArrayList<String>> arrayListHashMap = new HashMap<>();
    private ArrayList<String> art = new ArrayList<>();
    private ArrayList<String> pub = new ArrayList<>();


    private ArrayList<String> website = new ArrayList<>();
    private Bitmap bitmap1;
    private String s = "";
    private ArrayList<String> head = new ArrayList<>();
    private ArrayList<String> natext1 = new ArrayList<>();
    @Override
    protected String doInBackground(String... strings)
    {
        s = strings[0];
        Uri.Builder builder = Uri.parse(NewsURL).buildUpon();
        builder.appendQueryParameter("sources",strings[0]);
        builder.appendQueryParameter("apiKey",mykey);
        String urluse = builder.build().toString();
        Log.d(TAG, "doInBackground: THE URL FOR NEW ARTICLES : " +urluse);
        StringBuilder stringBuilder = new StringBuilder();
        try
        {
            String strrrr;
            // building the url
            URL url1 = new URL(urluse);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(inputStream));

            while((strrrr = bufferedReader.readLine())!=null)
            {
                stringBuilder.append(strrrr);
                stringBuilder.append('\n');
            }
        }
        catch (Exception e)
        {
            return null;
        }
        // to parse to json
        newsarticlejson(stringBuilder.toString());
        return null;
    }

    public newsArticlesDownloader(newsgatewayservice srv)
    {
        serviceNews = srv;
    }

    @Override
    protected void onPostExecute(String s)
    {
        serviceNews.newsset(arrayListHashMap);
    }

    private void newsarticlejson(String s2)
    {
        news.al.clear();
        String newsstr="";
        String string2;
        try
        { JSONObject jsonObject = new JSONObject(s2);
            JSONArray jsonArray = (JSONArray) jsonObject.get("articles");
            int i=0;
            while(i<jsonArray.length())
            { art.add(jsonArray.getString(i));
                JSONObject jsonObject1 = new JSONObject(art.get(i));
                head.add(jsonObject1.getString("title"));
                pub.add(jsonObject1.getString("author"));
                natext1.add((jsonObject1.getString("description")));
                web1.add(jsonObject1.getString("urlToImage"));
                website.add(jsonObject1.getString("url"));
                try
                { if(jsonObject1.getString("publishedAt").equals("null")==false)
                    { newsstr ="";
                        if(jsonObject1.getString("publishedAt").contains("+00:00"))
                        { string2 = jsonObject1.getString("publishedAt").replace("+00:00","Z"); }
                        else { string2 = jsonObject1.getString("publishedAt"); }
                        try { SimpleDateFormat simpleDateFormat =
                                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                            // date format
                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMMM dd, yyyy HH:mm");
                            Date date1 = simpleDateFormat.parse(string2);

                            newsstr = simpleDateFormat1.format(date1);
                        }
                        catch (Exception e)
                        {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                            String eeee= "right twice";

                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMMM dd, yyyy HH:mm");

                            Date date1 = simpleDateFormat.parse(string2);
                            String eeee1 = "right thrice";
                            newsstr = simpleDateFormat1.format(date1);
                        }
                        t.add(newsstr);
                    }
                    else
                    {
                        newsstr = "null";
                    }String picurl = jsonObject1.getString("urlToImage");
                    if(s.equals("buzzfeed"))
                    {
                        String newurl = picurl.replace("http:","https:");

                        java.net.URL url1 = new java.net.URL(newurl);
                        HttpURLConnection httpURLConnection1 =
                                (HttpURLConnection)url1.openConnection();
                        httpURLConnection1.setDoInput(true);

                        httpURLConnection1.connect();
                        InputStream inputStream1 = httpURLConnection1.getInputStream();
                        bitmap1 = BitmapFactory.decodeStream(inputStream1);
                    }
                    else
                    {
                        java.net.URL url1 = new java.net.URL(picurl);
                        HttpURLConnection httpcon =
                                (HttpURLConnection)url1.openConnection();

                        httpcon.setDoInput(true);
                        httpcon.connect();

                        InputStream inputStream1 = httpcon.getInputStream();
                        bitmap1 = BitmapFactory.decodeStream(inputStream1);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                news.nafornews(jsonObject1.getString("title"),jsonObject1.
                        getString("author"), bitmap1,jsonObject1.getString("description"),
                        jsonArray.length(),newsstr, jsonObject1.getString("url"));
                i++;
            }

            // putting the publisher, heading, text and website and photourl in a hashMap

            arrayListHashMap.put("Author",pub);
            arrayListHashMap.put("Title",head);

            arrayListHashMap.put("Description",natext1);
            arrayListHashMap.put("URL",web1);

            arrayListHashMap.put("Time",t);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}

