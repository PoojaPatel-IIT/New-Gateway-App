package com.example.pooja.newsgatewayapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by pooja on 12,November,2018
 */
public class fragmentnewsgateway extends Fragment
{
    private static final String TAG = "fragmentnewsgateway";

    private ImageView imview;



    public static final String delExtratxt = "EXTRA_MESSAGE";

    public static String ind;
    View vw;
    public static final Fragment NewInstance(int m)
    {
        fragmentnewsgateway fragobj = new fragmentnewsgateway();
        Bundle bundle = new Bundle(1);
        bundle.putInt(delExtratxt,m);
        ind = Integer.toString(m);
        fragobj.setArguments(bundle);
        return fragobj;
    }


    public int gettingindex()
    {
        return getArguments().getInt(delExtratxt,0);
    }



    @Override
    public View onCreateView(LayoutInflater in, @Nullable ViewGroup container, Bundle savedInstanceState) {
        int delvtxt;

        // inflating the fragment

        vw = in.inflate(R.layout.eachfrag,container,false);

        delvtxt = getArguments().getInt(delExtratxt);
        TextView tv = vw.findViewById(R.id.headline_title);
        if(news.get(gettingindex()).gethead().equals("null"))
        {
            tv.setText("");
        } else if(!news.get(gettingindex()).gethead().equals("null"))
        {
            tv.setText(news.get(gettingindex()).gethead());
        }
        TextView textView1 = vw.findViewById(R.id.NAauthor);
        if(news.get(gettingindex()).getnaauthor().equals("null"))
        {
            textView1.setText("");
        }
        else if(!news.get(gettingindex()).getnaauthor().equals("null"))
        {
            textView1.setText(news.get(gettingindex()).getnaauthor());
        }
        TextView NewsArtText = vw.findViewById(R.id.NAtext);
        if(news.get(gettingindex()).getnatext().equals("null"))
        {
            NewsArtText.setText("");
        }
        else if(!news.get(gettingindex()).getnatext().equals("null"))
        {
            NewsArtText.setText(news.get(gettingindex()).getnatext());
        }


// setting footer
        NewsArtText.setMovementMethod(new ScrollingMovementMethod());
        TextView footertext = vw.findViewById(R.id.footer);
        footertext.setText(Integer.toString(delvtxt+1)+" of "+news.get(gettingindex()).getct());


    // setting news article date

        TextView NAdate = vw.findViewById(R.id.NADate);
        if(news.get(gettingindex()).getnatime().equals("null")==false)
            NAdate.setText(news.get(gettingindex()).getnatime());
        else
            NAdate.setText("");
        // setting onclicklistener
        tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Uri uri = Uri.parse(news.get(gettingindex()).getweb());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });



        imview = vw.findViewById(R.id.newspic);
        Bitmap bitmap = news.get(gettingindex()).getbm();
        imview.setImageBitmap(bitmap);


        imview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Uri uri = Uri.parse(news.get(gettingindex()).getweb());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

       // setting on click listener

        NewsArtText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Uri uri = Uri.parse(news.get(gettingindex()).getweb());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        // returning view
        return vw;
    }


}

