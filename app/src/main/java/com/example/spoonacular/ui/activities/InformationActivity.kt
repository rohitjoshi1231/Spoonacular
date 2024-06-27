package com.example.spoonacular.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spoonacular.R
import com.example.spoonacular.api.ApiInterface
import com.example.spoonacular.api.ApiUtilities
import com.example.spoonacular.databinding.ActivityInformationBinding
import com.example.spoonacular.model.Nutrient
import com.example.spoonacular.repository.SpoonacularRepository
import com.example.spoonacular.ui.activities.MainActivity.Companion.API_KEY
import com.example.spoonacular.ui.activities.MainActivity.Companion.TAG
import com.example.spoonacular.ui.adapter.NutrientAdapter
import com.example.spoonacular.viewmodel.FavouritesViewModel
import com.example.spoonacular.viewmodel.RecipeViewModel
import com.example.spoonacular.viewmodel.RecipeViewModelFactory
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InformationActivity : AppCompatActivity() {
    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var binding: ActivityInformationBinding
    private lateinit var favouritesViewModel: FavouritesViewModel
    private val db = Firebase.firestore
    private var toggle = false
    private val nutritionList = mutableListOf<Nutrient>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()

        favouritesViewModel = ViewModelProvider(this)[FavouritesViewModel::class.java]

        binding.arrowImageView.setOnClickListener {
            toggle = !toggle
            binding.nutrientsRecycler.visibility = if (toggle) View.VISIBLE else View.GONE
        }

        val recipeId = intent.getIntExtra("recipeId", -1)
        if (recipeId != -1) {
            // Fetch recipe information
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    recipeViewModel.getRecipeInfo(API_KEY, recipeId) { success, message ->
                        if (!success) {
                            runOnUiThread {
                                Toast.makeText(
                                    this@InformationActivity, message, Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error fetching recipe info: ${e.message}")
                }
            }

            // Observe the LiveData
            recipeViewModel.recipeInfo.observe(this) { information ->
                information?.let { recipe ->
                    binding.fav.setOnClickListener {
                        try {
                            db.collection("Recipes").add(recipe).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        this@InformationActivity,
                                        "Added To Favorites",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@InformationActivity,
                                        "Failed to add to Favorites",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error adding to favorites: $e")
                        }
                    }
                }

                binding.servingTime.text = information.readyInMinutes.toString()
                binding.numberOfServings.text = information.servings.toString()
                binding.price.text = information.pricePerServing.toString()
                Picasso.get().load(information.image).into(binding.imageView)
                binding.popularRecipeName.text = information.title
                binding.instructions.text = information.instructions
                binding.quickSummary.text = information.summary

                // Nutrition RecyclerView
                nutritionList.clear()
                nutritionList.addAll(information.nutrition.nutrients)
                val nutritionAdapter = NutrientAdapter(nutritionList)
                binding.nutrientsRecycler.layoutManager = LinearLayoutManager(this)
                binding.nutrientsRecycler.adapter = nutritionAdapter

                // Ingredients RecyclerView setup
                val ingredientsAdapter = information.extendedIngredients?.let { ingredients ->
                    IngredientsAdapter(ingredients.map { it.name })
                }
                binding.ingridientsRecyler.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                binding.ingridientsRecyler.adapter = ingredientsAdapter
            }
        } else {
            Log.d(TAG, "Invalid recipeId")
        }
    }

    private fun setupViewModel() {
        val apiInterface = ApiUtilities.getInstance().create(ApiInterface::class.java)
        val spoonacularRepository = SpoonacularRepository(apiInterface)
        recipeViewModel = ViewModelProvider(
            this, RecipeViewModelFactory(spoonacularRepository)
        )[RecipeViewModel::class.java]
    }
}

class IngredientsAdapter(private val ingredients: List<String>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.bind(ingredient)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ingredientTextView: TextView = itemView.findViewById(R.id.ingredient_name)

        fun bind(ingredient: String) {
            ingredientTextView.text = ingredient
        }
    }
}
