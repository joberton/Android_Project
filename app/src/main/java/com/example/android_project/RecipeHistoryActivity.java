package com.example.android_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecipeHistoryActivity extends UtilityActivity {

    private SharedPreferences sharedPreferences;

    private boolean wifiConnection;
    private HttpUrl.Builder urlBuilder;
    private OkHttpClient client = new OkHttpClient();
    private Request request;
    private JSONObject apiResponse;

    private ArrayList<DrinkHistory> drinkHistoryList = new ArrayList();
    private DrinkHistoryAdapter drinkHistoryAdapter;
    private ListView historyList;

    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = getSharedPreferences("myPerfs",MODE_PRIVATE);

        setTheme(sharedPreferences.getInt("theme",R.style.DarkAppTheme));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_history);

        wifiConnection = checkForWifiConnection();

        //test apis
        //https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=13060
        //https://swapi.co/api/people/1/
        urlBuilder = HttpRequestHelper.buildHttpUrl("https://swapi.co/api/people/1/");
        request = HttpRequestHelper.buildNewHttpRequester(urlBuilder);

        historyList = findViewById(R.id.historyList);

        if(wifiConnection) {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("Request_Failure",e.getMessage());
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String stringResponse = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                apiResponse = new JSONObject(stringResponse);
                                String name = apiResponse.getString("name");
                                String height = apiResponse.getString("height");
                                drinkHistoryList.add(new DrinkHistory(name, height));
                                drinkHistoryAdapter = new DrinkHistoryAdapter(getApplicationContext(), R.layout.drink_history, drinkHistoryList);
                                historyList.setAdapter(drinkHistoryAdapter);
                            }
                            catch (Exception e)
                            {
                                Log.e("Response_Failure",e.getMessage());
                            }
                        }
                    });
                    Log.d("Response_Success",stringResponse);
                }
            });
        }
    }

    private class DrinkHistoryAdapter extends ArrayAdapter<DrinkHistory>
    {
        private ArrayList<DrinkHistory> drinkHistoryList;
        private int textViewResourceId;

        public DrinkHistoryAdapter(Context context, int textViewResourceId, ArrayList<DrinkHistory> drinkHistoryList)
        {
            super(context,textViewResourceId,drinkHistoryList);
            this.drinkHistoryList = drinkHistoryList;
            this.textViewResourceId = textViewResourceId;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            TextView drinkName,drinkHistory;
            if(convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(textViewResourceId,null);;

            }
            DrinkHistory i = drinkHistoryList.get(position);

            drinkName = view.findViewById(R.id.drinkName);
            drinkHistory = view.findViewById(R.id.drinkHistory);

            drinkName.setText(i.getDrinkName());
            drinkHistory.setText(getString(R.string.history).concat(i.getDrinkHistory()));
            return view;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                break;
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.just_settings_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private static class HttpRequestHelper
    {
        public static HttpUrl.Builder buildHttpUrl(String url)
        {
            return HttpUrl.parse(url).newBuilder();
        }

        public static Request buildNewHttpRequester(HttpUrl.Builder builder)
        {
            String url = builder.build().toString();
            return new Request.Builder().url(url).build();
        }
    }
}
