package knt3tb.cs4720.uvalgbtq;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String scrapedInfo = null;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        EventActivity.context = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        scrapeFromWeb scraper = new scrapeFromWeb();
        scraper.execute();


    }

    private class scrapeFromWeb extends AsyncTask<String, Void, Boolean> {

        ArrayList<String> eventTitles = new ArrayList<String>();
        ArrayList<String> eventDescriptions = new ArrayList<String>();
        Elements links = new Elements();

        protected void onPreExecute() {
            CharSequence text = "Loading events feed...";
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(context, text, duration).show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Document doc = null;
            String result = null;
            try {
                Connection.Response response = Jsoup.connect("http://www.virginia.edu/deanofstudents/lgbtq/").execute();
                String body = response.body();
                doc = Jsoup.parse(body);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (doc != null) {
                Elements titles = doc.select(".post dt");
                System.out.println("Size of titles is: " + titles.size());

                for (Element x : titles) {

                    eventTitles.add(Html.fromHtml(x.toString()).toString());
                    System.out.println("Title element: " + eventTitles.get(0));
                }

                Elements desc = doc.select(".post dd");
                for (Element x : desc) {
                    eventDescriptions.add(Html.fromHtml(x.toString()).toString());
                }

                links = doc.select(".post dt a[href]");
                System.out.println(links.get(0).attr("href"));

                //String temp = businesses.toString();

                //result = temp;
            }
            //return result;
            return true;
        }

        protected void onPostExecute(Boolean result) {
            RelativeLayout relativeLayout =
                    (RelativeLayout) findViewById(R.id.content_event);
            ListView listView = (ListView) findViewById(R.id.listView);
            if (result == true) {

                //create the item mapping
                String[] row_value = new String[] {"titles", "descriptions"};
                int[] row_id = new int[] {R.id.firstLine, R.id.secondLine};

                //prepare the list of event things
                ArrayList<HashMap<String,String>> fillMaps = new ArrayList<HashMap<String,String>>();
                for(int i = 0; i < eventTitles.size(); i++) {
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("titles", eventTitles.get(i));
                    map.put("descriptions", eventDescriptions.get(i));
                    fillMaps.add(map);
                }

                SimpleAdapter adapter = new SimpleAdapter(context, fillMaps, R.layout.event_row, row_value, row_id);
                listView.setAdapter(adapter);

                listView.setClickable(true);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(links.get(position).attr("href")));

                        startActivity(browserIntent);
                    }
                });

//                TextView titleText = (TextView) findViewById(R.id.firstLine);
//                titleText.setTextColor(getResources().getColor(R.color.colorAccent));



//                System.out.println("Success!!!!!");
//                textView.setText(Html.fromHtml(result));
//                textView.setMovementMethod(LinkMovementMethod.getInstance());
//                relativeLayout.removeAllViews();
//                relativeLayout.addView(textView);

            }
        }



//        public String getResult() {
//            return toReturn;
//        }

    }




//    private void scrapeTest() {
//        try {
//            Document doc = Jsoup.connect("http://gaycville.org/businesses.html").get();
//            String title = doc.title();
//            System.out.println(title);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.event, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_bathroommap) {
            Intent intent = new Intent(this, MapActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_resources) {
            Intent intent = new Intent(this, ResourceActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_business) {
            Intent intent = new Intent(this, BusinessActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_feedback) {
            Intent intent = new Intent(this, FeedbackActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_policies) {
            Intent intent = new Intent(this, PolicyActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
