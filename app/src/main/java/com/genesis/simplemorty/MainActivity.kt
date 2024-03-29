package com.genesis.simplemorty

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.genesis.simplemorty.databinding.ActivityMainBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Suppress("UNREACHABLE_CODE")
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val rickAndMortyService: RickAndMortyService =
            retrofit.create(RickAndMortyService::class.java)

        rickAndMortyService.getCharacterById(765)
            .enqueue(object : Callback<GetCharacterByIdResponse> {
                override fun onResponse(
                    call: Call<GetCharacterByIdResponse>,
                    response: Response<GetCharacterByIdResponse>
                ) {
                    Log.i("MainActivity", response.toString())
                    when (response.isSuccessful) {
                        true -> {
                            Toast.makeText(
                                this@MainActivity,
                                "Unsuccessful network call!!",
                                Toast.LENGTH_SHORT
                            ).show()

                            val body = response.body()!!

                            binding?.run {
                                nameTextView.text = body.name
                                aliveTextView.text = body.status
                                speciesTextView.text = body.species
                                originTextView.text = body.origin.name

                                when (body.gender.equals("male", true)) {
                                    true -> {
                                        genderImageView.setImageResource(R.drawable.ic_male_24)
                                    }
                                    false -> {
                                        genderImageView.setImageResource(R.drawable.ic_famale_24)
                                    }
                                }

                                Picasso.get().load(body.image).into(headerImageView)
                            }
                        }
                        false -> {
                            Toast.makeText(
                                this@MainActivity,
                                "Unsuccessful network call!!",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }
                    }

                }

                override fun onFailure(call: Call<GetCharacterByIdResponse>, t: Throwable) {
                    Log.i("MainActivity", t.message ?: "Null message")
                }
            })
    }
}