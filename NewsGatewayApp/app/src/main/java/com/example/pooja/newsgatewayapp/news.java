package com.example.pooja.newsgatewayapp;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pooja on 12,November,2018
 */
public class news  implements Serializable
{
    private static final String TAG = "news";

   // declaring all variables
    private int ct;
    private String NewArticletime  = "";

    public static ArrayList<news> al = new ArrayList<>();
    private String web = "";
    private String headline ="";
    private String naauthor = "";

    private String natext = "";
    private Bitmap bm;
    public news(String headline, String naauthor, Bitmap bitmap, String natext, int ct, String NewArticletime,String web) {
        this.headline = headline;
        this.natext = natext;

        this.naauthor = naauthor;
        this.ct = ct;
        this.NewArticletime = NewArticletime;
        this.web = web;
        this.bm = bitmap;

    }
    public static void nafornews(String t,String au, Bitmap bi, String d, int ct, String tim,String u)
    {

        news newart = new news(t,au,bi,d,ct,tim,u);
        al.add(newart);
    }




    public String getweb() {
        return web;
    }

    public void setweb(String web) {
        this.web = web;
    }


    public String gethead() {
        return headline;
    }

    public Bitmap getbm() {
        return bm;
    }
    public static news get(int id) {
        return al.get(id);
    }
    public void setbm(Bitmap bitmap) {
        this.bm = bitmap;
    }

    public void sethead(String headline) {
        this.headline = headline;
    }



    public String getnatext() {
        return natext;
    }

    public void setnatext(String natext) {
        this.natext = natext;
    }

    public int getct() {
        return ct;
    }


    public String getnaauthor() {
        return naauthor;
    }

    public void setnaauthor(String naauthor) {
        this.naauthor = naauthor;
    }

    public void setct(int ct) {
        this.ct = ct;
    }

    public String getnatime() {
        return NewArticletime;
    }

    public void setnatime(String time)
    {
        this.NewArticletime = time;
    }


}

