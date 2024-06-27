package com.example.spoonacular.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spoonacular.databinding.FragmentFavouritesBinding
import com.example.spoonacular.model.RecipeDetails
import com.example.spoonacular.ui.activities.MainActivity.Companion.TAG
import com.example.spoonacular.ui.adapter.FavouriteAdapter
import com.example.spoonacular.viewmodel.FavouritesViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class FavouritesFragment : Fragment() {
    private val recipeList = mutableListOf<RecipeDetails>()
    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var favouritesViewModel: FavouritesViewModel
    private val db = FirebaseFirestore.getInstance()
    private lateinit var favouriteAdapter: FavouriteAdapter
    private val auth = FirebaseAuth.getInstance().currentUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        favouriteAdapter = FavouriteAdapter(requireContext(), recipeList)
        setupRecyclerViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favouritesViewModel = ViewModelProvider(this)[FavouritesViewModel::class.java]
        fetchRecipes()
    }

    private fun setupRecyclerViews() {
        binding.favRecipesRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favouriteAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchRecipes() {
        try {
            binding.homeProgress.visibility = View.VISIBLE
            db.collection("Recipes").whereEqualTo("uid", auth?.uid).get()
                .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                    val recipes = querySnapshot.toObjects(RecipeDetails::class.java)
                    recipeList.clear()
                    recipeList.addAll(recipes)
                    favouriteAdapter.notifyDataSetChanged()
                    binding.homeProgress.visibility = View.GONE
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error fetching recipes: ", e)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Exception fetching recipes: ", e)
        }
    }
}
