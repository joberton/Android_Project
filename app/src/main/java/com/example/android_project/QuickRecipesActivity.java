package com.example.android_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.security.Provider;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class QuickRecipesActivity extends UtilityActivity {

    private SharedPreferences sharedPreferences;

    private static final int DRINK_REQUEST_LIMIT = 15;
    private static final int INSTRUCTIONS_MAX_LIMIT = 50;

    private static final String BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/";

    private boolean wifiConnection;
    private HttpUrl.Builder urlBuilder;
    private OkHttpClient client = new OkHttpClient();
    private Request request;
    private JSONObject apiResponse;

    private ArrayList<QuickRecipe> quickRecipeList = new ArrayList();
    private QuickRecipeAdapter quickRecipeAdapter;
    private ListView historyList;

    private ProgressBar progressBar;

    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = getSharedPreferences("myPerfs",MODE_PRIVATE);

        setTheme(sharedPreferences.getInt("theme",R.style.DarkAppTheme));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_recipes);

        wifiConnection = checkForWifiConnection();

        //test apis
        //https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=13060
        //https://swapi.co/api/people/1/
        urlBuilder = HttpRequestHelper.buildHttpUrl(BASE_URL + "filter.php?a=Alcoholic");
        request = HttpRequestHelper.buildNewHttpRequester(urlBuilder);

        historyList = findViewById(R.id.historyList);
        progressBar = findViewById(R.id.apiProgressBar);

        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                QuickRecipe quickRecipe = (QuickRecipe) adapterView.getItemAtPosition(i);
                Intent data = new Intent(getApplicationContext(),QuickRecipesDetailsActivity.class);

                String base64Data = encodeToBase64(quickRecipe.getImageData());

                data.putExtra("drinkName", quickRecipe.getDrinkName());
                data.putExtra("drinkDateCreated", quickRecipe.getDateCreated().toString());
                data.putExtra("drinkInstructions",quickRecipe.getDrinkInstructions());
                data.putExtra("drinkImage", decodeBase64(base64Data));

                startActivity(data);
            }
        });

        if(wifiConnection) {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("Request_Failure",e.getMessage());
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String stringResponse = response.body().string();
                    List<Request> drinkRequests;
                    try {
                        apiResponse = new JSONObject(stringResponse);
                        JSONArray jsonDrinkCollection = apiResponse.getJSONArray("drinks");
                        drinkRequests = HttpRequestHelper.buildDrinkRequests(jsonDrinkCollection);
                        quickRecipeList = HttpRequestHelper.fetchDrinks(client,drinkRequests);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                quickRecipeAdapter = new QuickRecipeAdapter(getApplicationContext(), R.layout.quick_recipes, quickRecipeList);
                                historyList.setAdapter(quickRecipeAdapter);
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        Log.e("Response_Failure",e.getMessage());
                    }
                }
            });
        }
    }

    private class QuickRecipeAdapter extends ArrayAdapter<QuickRecipe>
    {
        private ArrayList<QuickRecipe> quickRecipeList;
        private int textViewResourceId;

        public QuickRecipeAdapter(Context context, int textViewResourceId, ArrayList<QuickRecipe> quickRecipeList)
        {
            super(context,textViewResourceId, quickRecipeList);
            this.quickRecipeList = quickRecipeList;
            this.textViewResourceId = textViewResourceId;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            TextView drinkName,drinkInstructions,dateCreated;
            ImageView apiImageView;
            if(convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(textViewResourceId,null);;

            }
            QuickRecipe i = quickRecipeList.get(position);

            drinkName = view.findViewById(R.id.drinkApiName);
            drinkInstructions = view.findViewById(R.id.drinkApiInstructions);
            dateCreated = view.findViewById(R.id.apiDateCreated);
            apiImageView = view.findViewById(R.id.apiDrinkImage);

            String truncatedInstructions = i.getDrinkInstructions();

            apiImageView.setImageBitmap(Bitmap.createScaledBitmap(i.getImageData(),250,250,false));
            dateCreated.setText(getString(R.string.dateCreatedTextView).concat(i.getDateCreated().toString()));
            drinkName.setText(i.getDrinkName());
            drinkInstructions.setText(getString(R.string.instructionsTextView).concat(truncatedInstructions.length() < INSTRUCTIONS_MAX_LIMIT ? truncatedInstructions : truncatedInstructions.substring(0,INSTRUCTIONS_MAX_LIMIT) + "........"));
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

        public static ArrayList<QuickRecipe> fetchDrinks(OkHttpClient client, List<Request> drinkRequests)
        {
            ArrayList<QuickRecipe> drinkHistories = new ArrayList();
            for(Request drinkRequest : drinkRequests)
            {
                try {
                    Response drinkResponse = client.newCall(drinkRequest).execute();
                    String stringResponse = drinkResponse.body().string();
                    JSONObject drinkItem = new JSONObject(stringResponse);

                    URL imageUrl = new URL(getJsonDrinkKeyValue(drinkItem,"strDrinkThumb"));
                    Bitmap imageData = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());

                    String drinkName = getJsonDrinkKeyValue(drinkItem,"strDrink");
                    String drinkInstructions = getJsonDrinkKeyValue(drinkItem,"strInstructions");

                    SimpleDateFormat dateCreatedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date dateCreated = dateCreatedFormat.parse(getJsonDrinkKeyValue(drinkItem,"dateModified"));

                    drinkHistories.add(new QuickRecipe(drinkName,drinkInstructions,dateCreated,imageData));
                }
                catch (Exception e){
                    Log.e("Drink_Request_Error",e.getMessage());
                }
            }
            return drinkHistories;
        }

        public static List<Request> buildDrinkRequests(JSONArray drinks)
        {
            ArrayList<Request> drinkRequests = new ArrayList();
            try {
                for (int i = 0; i < drinks.length() && i < DRINK_REQUEST_LIMIT; i++) {
                    int randomDrink = (int) (Math.random() * drinks.length()) + 1;
                    HttpUrl.Builder drinkUrlBuild = buildHttpUrl(BASE_URL + "lookup.php?i=" +
                            drinks.getJSONObject(randomDrink).getString("idDrink"));
                    drinkRequests.add(buildNewHttpRequester(drinkUrlBuild));
                }
            }
            catch (Exception e)
            {
                Log.e("Drink_Build_Failed",e.getMessage());
            }
            return drinkRequests;
        }

        private static String getJsonDrinkKeyValue(JSONObject drinkItem,String value)
        {
            String drinkValue = null;
            try
            {
                drinkValue = drinkItem.getJSONArray("drinks").getJSONObject(0).getString(value);
            }
            catch (Exception e)
            {
                Log.e("Key_Fetch_Error",e.getMessage());
            }
            return drinkValue;
        }
    }
}
