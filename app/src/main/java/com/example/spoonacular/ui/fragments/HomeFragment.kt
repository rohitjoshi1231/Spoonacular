package com.example.spoonacular.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spoonacular.api.ApiInterface
import com.example.spoonacular.api.ApiUtilities
import com.example.spoonacular.databinding.FragmentHomeBinding
import com.example.spoonacular.model.Item
import com.example.spoonacular.model.Recipe
import com.example.spoonacular.model.Results
import com.example.spoonacular.repository.SpoonacularRepository
import com.example.spoonacular.ui.activities.MainActivity.Companion.API_KEY
import com.example.spoonacular.ui.activities.MainActivity.Companion.TAG
import com.example.spoonacular.ui.adapter.AllRecipeAdapter
import com.example.spoonacular.ui.adapter.HomeAdapter
import com.example.spoonacular.ui.adapter.SearchAdapter
import com.example.spoonacular.viewmodel.RecipeViewModel
import com.example.spoonacular.viewmodel.RecipeViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), SearchAdapter.OnItemClickListener {

    private lateinit var binding: FragmentHomeBinding
    private val recipeList = mutableListOf<Recipe>()
    private val allRecipeList = mutableListOf<Results>()
    private val queryList = mutableListOf<Item>()
    private lateinit var verticalAdapter: AllRecipeAdapter
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var horizontalAdapter: HomeAdapter
    private lateinit var recipeViewModel: RecipeViewModel
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Initialize Adapters
        searchAdapter = SearchAdapter(parentFragmentManager, queryList, this)
        verticalAdapter = AllRecipeAdapter(requireContext(), allRecipeList)
        horizontalAdapter = HomeAdapter(requireActivity(), recipeList)


        // Setup RecyclerViews
        setupRecyclerViews()

        // Setup ViewModel
        setupViewModel()

        // Setup SearchView
        setupSearchView()

        // Display greeting
        binding.greetings.text = "\uD83D\uDC4BHey " + (auth.currentUser?.displayName ?: "Guest")

        // Fetch recipes
        getPopularRecipe()
        getAllRecipes()

        return binding.root
    }

    private fun setupRecyclerViews() {
        // Vertical RecyclerView
        binding.allRecipeRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = verticalAdapter
        }

        binding.searchRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
        }

        // Horizontal RecyclerView
        binding.popularRecipeRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = horizontalAdapter
        }
    }

    private fun setupViewModel() {
        val apiInterface = ApiUtilities.getInstance().create(ApiInterface::class.java)
        val spoonacularRepository = SpoonacularRepository(apiInterface)
        recipeViewModel = ViewModelProvider(
            this, RecipeViewModelFactory(spoonacularRepository)
        )[RecipeViewModel::class.java]
    }

    private fun getPopularRecipe() {
        CoroutineScope(Dispatchers.IO).launch {
            recipeViewModel.getRecipes { success, message ->
                if (!success) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.homeProgress.visibility = View.VISIBLE
        recipeViewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            recipes?.let {
                horizontalAdapter.updateData(it)
                binding.homeProgress.visibility = View.GONE
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getAllRecipes() {
        CoroutineScope(Dispatchers.IO).launch {
            recipeViewModel.getAllRecipes { success, message ->
                if (!success) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.homeProgress.visibility = View.VISIBLE
        recipeViewModel.alRecipes.observe(viewLifecycleOwner) { recipesResponse ->
            Log.d(TAG, "getAllRecipes: $recipesResponse")
            recipesResponse?.let {
                allRecipeList.clear()
                allRecipeList.addAll(it.results)
                verticalAdapter.notifyDataSetChanged()
                binding.homeProgress.visibility = View.GONE
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun filterRecipes(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            recipeViewModel.autoComplete(query, API_KEY) { success, message ->
                if (!success) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.homeProgress.visibility = View.VISIBLE
        recipeViewModel.autoComplete.observe(viewLifecycleOwner) { recipes ->
            recipes?.let {
                queryList.clear()
                queryList.addAll(it)
                searchAdapter.notifyDataSetChanged()
                binding.homeProgress.visibility = View.GONE
            }
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    if (newText.isNotEmpty()) {
                        binding.searchRecycler.visibility = View.VISIBLE
                        binding.titleHeading1.visibility = View.GONE
                        binding.titleHeading2.visibility = View.GONE
                        binding.allRecipeRecycler.visibility = View.GONE
                        binding.popularRecipeRecycler.visibility = View.GONE
                        filterRecipes(newText)
                    } else {
                        getAllRecipes()
                    }
                }
                return false
            }
        })
    }


    override fun onItemClick(item: Item) {
        val bottomSheetFragment = SortBottomSheetFragment.newInstance(item.name)
        bottomSheetFragment.show(parentFragmentManager, "SortBottomSheetFragment")
    }
}
