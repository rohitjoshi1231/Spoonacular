package com.example.spoonacular.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.spoonacular.api.ApiInterface
import com.example.spoonacular.model.Item
import com.example.spoonacular.model.Recipe
import com.example.spoonacular.model.RecipeDetails
import com.example.spoonacular.model.RecipesResponse
import com.example.spoonacular.model.SimilarRecipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SpoonacularRepository(private val apiInterface: ApiInterface) {
    val recipes = MutableLiveData<List<Recipe>>()

    val allRecipes = MutableLiveData<RecipesResponse>()

    private val _recipeInfo = MutableLiveData<RecipeDetails>()
    val recipeInfo: LiveData<RecipeDetails> get() = _recipeInfo

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> get() = _items


    val similarRecipe = MutableLiveData<List<SimilarRecipe>>()


    suspend fun getRecipes(apiKey: String, callback: (Boolean, String) -> Unit) {
        withContext(Dispatchers.IO) {
            val result = apiInterface.getRecipes(apiKey).await()
            if (result.isSuccessful && result.body() != null) {
                recipes.postValue(result.body()!!.recipes)
                callback(true, "")
            } else {
                callback(false, result.message().toString())
                Log.e("SpoonacularRepository", "API Error: ${result.errorBody()}")

            }
        }
    }

    suspend fun autoCompleteRecipe(
        query: String, apiKey: String, callback: (Boolean, String) -> Unit
    ) {
        val result = apiInterface.autoComplete(query, apiKey).await()
        if (result.isSuccessful && result.body() != null) {
            _items.postValue(result.body())
            callback(true, "")
        } else {
            callback(false, result.message().toString())
            // Handle the error
            Log.e("SpoonacularRepository", "API Error: ${result.errorBody()}")
        }
    }


    suspend fun getRecipeInfo(apiKey: String, id: Int, callback: (Boolean, String) -> Unit) {
        withContext(Dispatchers.IO) {
            val result = apiInterface.getRecipeInformation(id, true, apiKey).await()
            if (result.isSuccessful && result.body() != null) {
                Log.d("SpoonacularRepository", "API Response: ${result.body()}")
                _recipeInfo.postValue(result.body())
                callback(true, "")
            } else {
                // Handle the error
                callback(false, result.message().toString())
                Log.e("SpoonacularRepository", "API Error: ${result.message()}")
            }
        }
    }

    suspend fun getAllRecipes(apiKey: String, callback: (Boolean, String) -> Unit) {
        try {
            val response = apiInterface.getAllRecipes(apiKey).await()
            if (response.isSuccessful && response.body() != null) {
                allRecipes.postValue(response.body())
                callback(true, "")
            } else {
                // Handle unsuccessful response
                callback(false, response.message().toString())
            }
        } catch (e: Exception) {
            // Handle network or coroutine exceptions
            callback(false, e.message.toString())
        }
    }

    suspend fun getSearchedRecipes(
        query: String, apiKey: String, callback: (Boolean, String) -> Unit
    ) {
        try {
            val response = apiInterface.searchRecipes(query, apiKey).await()
            if (response.isSuccessful && response.body() != null) {
                allRecipes.postValue(response.body())
                callback(true, "")
            } else {
                // Handle unsuccessful response
                callback(false, response.message().toString())
            }
        } catch (e: Exception) {
            callback(false, e.message.toString())
            // Handle network or coroutine exceptions
        }
    }
}
