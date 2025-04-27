package com.example.pokedex;

import static com.example.pokedex.PokedexAdapter.getPokemon;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class PokemonActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView numberTextView;
    private TextView type1TextView;
    private TextView type2TextView;
    private ImageView imageView;
    private TextView descriptionTextView;
    private String url;
    private RequestQueue requestQueue;
    private Button catchButton;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pokemon);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        url = getIntent().getStringExtra("url");
        nameTextView = findViewById(R.id.pokemon_name);
        numberTextView = findViewById(R.id.pokemon_number);
        type1TextView = findViewById(R.id.pokemon_type1);
        type2TextView = findViewById(R.id.pokemon_type2);
        catchButton = findViewById(R.id.toggle_catch);
        imageView = findViewById(R.id.pokemon_image);
        descriptionTextView = findViewById(R.id.pokemon_description);
        load();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    public void load(){
        type2TextView.setText("");
        type1TextView.setText("");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    nameTextView.setText(response.getString("name"));
                    Pokemon pokemon = getPokemon(response.getString("name"));
                    SharedPreferences sharedPreferences = getSharedPreferences("Pokemon", Context.MODE_PRIVATE);
                    boolean caught = sharedPreferences.getBoolean(nameTextView.getText().toString(), false);
                    pokemon.setCaught(caught);
                    JSONObject sprites = response.getJSONObject("sprites");
                    imageUrl = sprites.getString("front_default");
                    new DownloadSpriteTask().execute(imageUrl);
                    if (pokemon.isCaught()){
                        catchButton.setText("Release");
                    } else {
                        catchButton.setText("Catch");
                    }
                    numberTextView.setText(String.format("#%03d", response.getInt("id")));
                    loadDescription(response.getInt("id"), descriptionTextView);

                    JSONArray entries = response.getJSONArray("types");
                    for (int i = 0; i < entries.length(); i++) {
                        JSONObject typeEntry = entries.getJSONObject(i);
                        int slot = typeEntry.getInt("slot");
                        String type = typeEntry.getJSONObject("type").getString("name");

                        if (slot == 1){
                            type1TextView.setText(type);
                        }
                        if (slot == 2){
                            type2TextView.setText(type);
                        }
                    }
                } catch (JSONException e) {
                    Log.e("cs50", "Pokemon Json error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cs50", "Pokemon details error");
            }
        });
        requestQueue.add(request);
    }

    public void loadDescription(int species, TextView textView){
        String url = "https://pokeapi.co/api/v2/pokemon-species/" + species;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String description = (response.getJSONArray("flavor_text_entries").getJSONObject(0).getString("flavor_text"));
                    textView.setText(description);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cs50", "Pokemon description error");
            }
        });
        requestQueue.add(request);
    }

    public void toggleCatch(View view) {
        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("Pokemon", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean caught = sharedPreferences.getBoolean(nameTextView.getText().toString(), false);
        TextView pokemonName = findViewById(R.id.pokemon_name);
        Pokemon pokemon = getPokemon(pokemonName.getText().toString());
        pokemon.setCaught(caught);
        if (!caught) {
            editor.putBoolean(nameTextView.getText().toString(), true);
            editor.apply();
            catchButton.setText("Release");
        } else {
            editor.putBoolean(nameTextView.getText().toString(), false);
            editor.apply();
            catchButton.setText("Catch");
        }
    }

    private class DownloadSpriteTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                return BitmapFactory.decodeStream(url.openStream());
            }
            catch (Exception e){
                Log.e("cs50", "Sprite error", e);
                return null;
            }
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView = findViewById(R.id.pokemon_image);
            imageView.setImageBitmap(bitmap);
        }

    }
}
