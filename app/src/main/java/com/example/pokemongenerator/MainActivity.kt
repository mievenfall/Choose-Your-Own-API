package com.example.pokemongenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

@GlideModule
class GlideApp : AppGlideModule()

class MainActivity : AppCompatActivity() {
    var pokemonImageURL = ""
    var pokemonName = ""
    var pokemonID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getPokemonImageURL()
        val button = findViewById<Button>(R.id.bRandom)
        val imageView = findViewById<ImageView>(R.id.ivPokemon)
        val name = findViewById<TextView>(R.id.tvName)
        val id = findViewById<TextView>(R.id.tvID)

        getNextImage(button, imageView, name, id)
    }

    private fun getPokemonImageURL() {
        val client = AsyncHttpClient()
        val rnds = (0..1025).random()
        val str = "https://pokeapi.co/api/v2/pokemon-form/$rnds"
        client[str, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                pokemonImageURL = json.jsonObject.getJSONObject("sprites").getString("front_default")
                pokemonName = json.jsonObject.getString("name")
                pokemonID = json.jsonObject.getString("id")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Dog", errorResponse)
            }
        }]
    }

    private fun getNextImage(button: Button, imageView: ImageView, name: TextView, id: TextView) {
        button.setOnClickListener {
            getPokemonImageURL()

            Glide.with(this)
                .load(pokemonImageURL)
                .fitCenter()
                .into(imageView)

            name.text = pokemonName
            id.text = pokemonID
        }
    }
}