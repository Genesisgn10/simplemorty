package com.genesis.simplemorty

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.genesis.simplemorty.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private val viewModel: SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel.refreshCharacter(300)
        viewModel.characterByIdLiveData.observe(this) { response ->
            if (response == null) {
                Toast.makeText(
                    this@MainActivity,
                    "Unsuccessful network call!!",
                    Toast.LENGTH_SHORT
                ).show()
                return@observe
            }

            setupUi(response)

        }
    }

    private fun setupUi(response: GetCharacterByIdResponse) {
        binding?.run {
            nameTextView.text = response.name
            aliveTextView.text = response.status
            speciesTextView.text = response.species
            originTextView.text = response.origin.name

            when (response.gender.equals("male", true)) {
                true -> {
                    genderImageView.setImageResource(R.drawable.ic_male_24)
                }
                false -> {
                    genderImageView.setImageResource(R.drawable.ic_famale_24)
                }
            }

            Picasso.get().load(response.image).into(headerImageView)
        }
    }
}