package com.example.pooja.newsgatewayapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private ArrayList<String> it = new ArrayList<>();
    private myadapter adap;


    private ArrayList<String> src = new ArrayList<>();
    private ArrayList<String> catg = new ArrayList<>();

    private boolean boolvar = false;
    private String cat = "null";
    String strrrrrrrrrrr = "";

    static final String ACTION = "ACTION_NEWS_STORY";
    private DrawerLayout drawerLayout;
    NetworkInfo networkInfo;
    private static final String TAG = "MainActivity";

    private ArrayList<String> nno = new ArrayList<>();
    private ArrayList<String> publisher = new ArrayList<>();


    private Menu menu;
    private ViewPager viewPager;
    private List<Fragment> frag_lst;
    private ActionBarDrawerToggle drawerToggle;



    static final String EXTRA = "DATA_EXTRA1";


    int c1=0 ;
    int c2=0 ;
    private ListView listView;
    private recieve_news nzsrcv;

    int c3 = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_main);
        listView = findViewById(R.id.drawer);
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.drawerlist, it));
        listView.setOnItemClickListener(new ListView.OnItemClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                viewPager.setBackground(null);
                selectedItem(position);
            }
        });
        drawerToggle = new ActionBarDrawerToggle
                (this,drawerLayout,R.string.open,R.string.close);

        // if these two lines are missing then hamburger might not show up
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        frag_lst = getFragments();
        adap = new myadapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager_);

        viewPager.setAdapter(adap);
        viewPager.setBackgroundResource(R.drawable.bck);

        // start a serrvice : service --> sender
        Intent intent = new Intent(MainActivity.this,newsgatewayservice.class);
        Log.d(TAG, "onCreate: HERE INTENT");
        startService(intent);

       // receiver
        // receiver object
        nzsrcv = new recieve_news();
        IntentFilter intentFilter = new IntentFilter(ACTION);
        Log.d(TAG, "onCreate: Intent filter");
        registerReceiver(nzsrcv,intentFilter);
        new newsSourceDownloader(MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"all");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putString("cate", cat);
        outState.putString("title", getTitle().toString());
        outState.putInt("count", c1);
        outState.putInt("index", viewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // sync the toggle to the current state so that your drawer is synced
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        setTitle(savedInstanceState.getString("title"));
        String hi = savedInstanceState.getString("cate");
        if (savedInstanceState.getInt("count") != 0) {
            if (!(hi.equals("null"))) {
                new newsSourceDownloader(MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "Category", hi);
            }
            c2 = savedInstanceState.getInt("count");
            c3 = savedInstanceState.getInt("index");
            viewPager.setBackground(null);
            executeFrag();
        }
        else {
            viewPager.setBackgroundResource(R.drawable.bck);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
    private void executeFrag()
    {int i=0; int j=0;
        while(i<adap.getCount())
        {
            adap.notifyChangeInPosition(i);
            i++;
        }
        frag_lst.clear();
        while(j<c2)
        {
            frag_lst.add(fragmentnewsgateway.NewInstance(j));
            j++;
        }adap.notifyDataSetChanged();
        viewPager.setCurrentItem(c3);
    }




    private void selectedItem(int pos)
    {
        it = src;
        setTitle(it.get(pos));
        Log.d(TAG, "selectedItem: "+it);
        strrrrrrrrrrr = nno.get(pos);
        Intent intent = new Intent();
        Log.d(TAG, "selectedItem: "+pos);
        intent.setAction(newsgatewayservice.ACTION_MSG);
        intent.putExtra(EXTRA,strrrrrrrrrrr);
        Log.d(TAG, "selectedItem: "+strrrrrrrrrrr);
        sendBroadcast(intent);
        drawerLayout.closeDrawer(listView);
    }

    private List<Fragment> getFragments()
    {
        List<Fragment> fragmentList1 = new ArrayList<Fragment>();
        return fragmentList1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menupage,menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)==true)
            return true;
        if(item.toString().equals("all")==true)
            new newsSourceDownloader(MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"all");
        else
        {
            cat = item.toString();
            new newsSourceDownloader(MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"Category",item.toString());
        }
        return true;
    }

    public void gettingNewsSrcs(ArrayList<String> arraysrc, ArrayList<String> arraycatg, ArrayList<String> arraynno)
    {

        // clearing the source and the catg list
        src.clear();
        catg.clear();


        // news source
        src = arraysrc;


        // news catgory
        catg = arraycatg;


        // news id
        nno = arraynno;
        // sorting the news category
        Collections.sort(catg);

        if(boolvar == false)
        {
            int i=0;

            // adding menu
            menu.add(R.menu.menupage, Menu.NONE,0,"all");

            while(i<catg.size())
            {
                menu.add(R.menu.menupage,Menu.NONE,0,catg.get(i));
                // incrementing i
                i++;
            }
            // setting the boolean var true
            boolvar = true;
        }
        listView.setAdapter(new ArrayAdapter<>(this,R.layout.drawerlist,src));
    }

    @Override
    protected void onDestroy()
    {

        // on destroy --> unregister the receiver and stop the service
        unregisterReceiver(nzsrcv);
        Log.d(TAG, "onDestroy: DEAD");
        Intent intent = new Intent(MainActivity.this,newsgatewayservice.class);
        stopService(intent);
        super.onDestroy();
    }

    private class myadapter extends FragmentPagerAdapter
    {
        private long base = 0;

        @Override
        public long getItemId(int position) {
            return base+position;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "getItem: "+position);
            Log.d(TAG, "getItem: "+frag_lst.size());
            return frag_lst.get(position);
        }

        @Override
        public int getCount() {
            return frag_lst.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void notifyChangeInPosition(int n) {
            base += getCount() + n;
        }

        public myadapter(FragmentManager fm) {
            super(fm);
        }
    }

    class recieve_news extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(intent.getAction().equals(ACTION) && intent.hasExtra(newsgatewayservice.SERVICE_DATA))
            { int i=0;
                publisher = intent.getStringArrayListExtra(newsgatewayservice.SERVICE_DATA);
                c1 = publisher.size();
                frag_lst.clear();

                while(i<c1)
                {
                    frag_lst.add(fragmentnewsgateway.NewInstance(i));
                    i++;
                }
                adap.notifyDataSetChanged();
                viewPager.setCurrentItem(0);
            }
        }
    }

}
