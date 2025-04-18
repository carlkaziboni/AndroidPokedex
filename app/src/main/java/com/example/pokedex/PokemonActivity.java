package com.example.pokedex;

import static com.example.pokedex.PokedexAdapter.getPokemon;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PokemonActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView numberTextView;
    private TextView type1TextView;
    private TextView type2TextView;
    private String url;
    private RequestQueue requestQueue;
    private Button catchButton;

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
                    if (pokemon.isCaught()){
                        catchButton.setText("Release");
                    } else {
                        catchButton.setText("Catch");
                    }
                    numberTextView.setText(String.format("#%03d", response.getInt("id")));

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

    public void toggleCatch(View view) {
        TextView pokemonName = findViewById(R.id.pokemon_name);
        Pokemon pokemon = getPokemon(pokemonName.getText().toString());
        pokemon.setCaught(!pokemon.isCaught());
        if (pokemon.isCaught()) {
            catchButton.setText("Release");
        } else {
            catchButton.setText("Catch");
        }
    }
}
