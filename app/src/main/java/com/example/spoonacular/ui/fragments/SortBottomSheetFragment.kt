package com.example.spoonacular.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spoonacular.api.ApiInterface
import com.example.spoonacular.api.ApiUtilities
import com.example.spoonacular.databinding.BottomSheetSortBinding
import com.example.spoonacular.repository.SpoonacularRepository
import com.example.spoonacular.ui.activities.HomeScreenActivity
import com.example.spoonacular.ui.activities.IngredientsAdapter
import com.example.spoonacular.ui.activities.MainActivity
import com.example.spoonacular.ui.activities.MainActivity.Companion.API_KEY
import com.example.spoonacular.ui.adapter.NutrientAdapter
import com.example.spoonacular.viewmodel.RecipeViewModel
import com.example.spoonacular.viewmodel.RecipeViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SortBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var query: String
    private var toggle = false
    private val db = com.google.firebase.Firebase.firestore

    companion object {
        private const val ARG_QUERY = "query"

        fun newInstance(query: String): SortBottomSheetFragment {
            val fragment = SortBottomSheetFragment()
            val args = Bundle()
            args.putString(ARG_QUERY, query)
            fragment.arguments = args
            return fragment
        }
    }

    val auth = FirebaseAuth.getInstance().currentUser
    private lateinit var binding: BottomSheetSortBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetSortBinding.inflate(inflater, container, false)

        setupViewModel()
        getSearchedRecipes(query)
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())

        if (account != null && !auth?.uid.isNullOrEmpty()) {
            binding.bottomSheetFavIcon.setOnClickListener {
                recipeViewModel.recipeInfo.observe(this) {
                    binding.progressBar.visibility = View.VISIBLE
                    db.collection("Recipes").add(it).addOnCompleteListener { success ->
                        if (success.isSuccessful) {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), "Added To fav", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(), "Adding To fav is failed", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        } else {
            Toast.makeText(requireContext(), "Sign in To add Favourites", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }


        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        query = arguments?.getString(ARG_QUERY) ?: ""


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getSearchedRecipes(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            recipeViewModel.searchedRecipe(query, API_KEY) { success, message ->
                if (!success) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        recipeViewModel.alRecipes.observe(viewLifecycleOwner) {
            // Update UI with the recipes
            binding.sortTitle.text = query
            Picasso.get().load(it.results.first().image).into(binding.mainImg)

            CoroutineScope(Dispatchers.IO).launch {
                recipeViewModel.getRecipeInfo(API_KEY, it.results.first().id) { success, message ->
                    if (!success) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            recipeViewModel.recipeInfo.observe(viewLifecycleOwner) { recipeInfo ->
                binding.servingTime.text = recipeInfo.readyInMinutes.toString()
                binding.numberOfServings.text = recipeInfo.servings.toString()
                binding.price.text = recipeInfo.pricePerServing.toString()


                binding.instructions.text = recipeInfo.instructions
                binding.quickSummary.text = recipeInfo.summary

                // Ingredients RecyclerView setup
                val ingredientsAdapter = recipeInfo.extendedIngredients?.let { ingredients ->
                    IngredientsAdapter(ingredients.map { ingredient -> ingredient.name })
                }
                binding.bottomIngridientRecipeRecycler.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.bottomIngridientRecipeRecycler.adapter = ingredientsAdapter

                toggle(binding.arrowImageView, binding.bottomNutrientsRecycler, 1)
                toggle(binding.arrowImageView3, binding.bottomIngridientRecipeRecycler, 1)
                toggle(binding.arrowImageView2, binding.bottomIngridientRecipeRecycler, 0)


                // Nutrient Recycler
                val nutritionList = recipeInfo.nutrition.nutrients
                val nutritionAdapter = NutrientAdapter(nutritionList)
                binding.bottomNutrientsRecycler.layoutManager =
                    LinearLayoutManager(requireContext())
                binding.bottomNutrientsRecycler.adapter = nutritionAdapter

                binding.applyButton.setOnClickListener {
                    startActivity(Intent(requireContext(), HomeScreenActivity::class.java))
                }


            }
        }
    }

    private fun toggle(image: ImageView, recycler: RecyclerView, type: Int) {
        if (type == 0) {
            image.setOnClickListener {
                toggle = !toggle
                binding.x.visibility = if (toggle) View.VISIBLE else View.GONE
                binding.y.visibility = if (toggle) View.VISIBLE else View.GONE
                binding.quickSummary.visibility = if (toggle) View.VISIBLE else View.GONE
                binding.instructions.visibility = if (toggle) View.VISIBLE else View.GONE

            }
        } else {
            image.setOnClickListener {
                toggle = !toggle
                recycler.visibility = if (toggle) View.VISIBLE else View.GONE
            }
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
