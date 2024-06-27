package com.example.spoonacular.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spoonacular.model.Item
import com.example.spoonacular.model.Recipe
import com.example.spoonacular.model.RecipeDetails
import com.example.spoonacular.model.RecipesResponse
import com.example.spoonacular.model.SimilarRecipe
import com.example.spoonacular.repository.SpoonacularRepository
import com.example.spoonacular.ui.activities.MainActivity.Companion.API_KEY

class RecipeViewModel(private val spoonacularRepository: SpoonacularRepository) : ViewModel() {

    suspend fun getRecipeInfo(apiKey: String, id: Int, callback: (Boolean, String) -> Unit) {
        spoonacularRepository.getRecipeInfo(apiKey, id, callback)
    }

    suspend fun getAllRecipes(callback: (Boolean, String) -> Unit) {
        spoonacularRepository.getAllRecipes(API_KEY, callback)

    }

    suspend fun getRecipes(callback: (Boolean, String) -> Unit) {
        spoonacularRepository.getRecipes(API_KEY, callback)

    }

    suspend fun autoComplete(query: String, apiKey: String, callback: (Boolean, String) -> Unit) {
        spoonacularRepository.autoCompleteRecipe(query, apiKey, callback)
    }

    suspend fun searchedRecipe(query: String, apiKey: String, callback: (Boolean, String) -> Unit) {
        spoonacularRepository.getSearchedRecipes(query, apiKey, callback)
    }

    val recipes: LiveData<List<Recipe>> get() = spoonacularRepository.recipes
    val autoComplete: LiveData<List<Item>> get() = spoonacularRepository.items
    val alRecipes: MutableLiveData<RecipesResponse> get() = spoonacularRepository.allRecipes
    val recipeInfo: LiveData<RecipeDetails> get() = spoonacularRepository.recipeInfo
    val similarRecipe: LiveData<List<SimilarRecipe>> get() = spoonacularRepository.similarRecipe
}
