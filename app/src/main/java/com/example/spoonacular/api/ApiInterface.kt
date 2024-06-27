package com.example.spoonacular.api

import com.example.spoonacular.model.Item
import com.example.spoonacular.model.RecipeDetails
import com.example.spoonacular.model.RecipesResponse
import com.example.spoonacular.model.SimilarRecipe
import com.example.spoonacular.model.Spoonacular
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("recipes/random")
    fun getRecipes(
        @Query("apiKey") apiKey: String, @Query("number") number: Int = 10
    ): Deferred<Response<Spoonacular>>


    @GET("/food/ingredients/autocomplete")
    fun autoComplete(
        @Query("query") query: String,
        @Query("apiKey") apiKey: String,
        @Query("number") number: Int = 5
    ): Deferred<Response<List<Item>>>


    @GET("recipes/{id}/information/")
    fun getRecipeInformation(
        @Path("id") id: Int,
        @Query("includeNutrition") includeNutrition: Boolean = true,
        @Query("apiKey") apiKey: String
    ): Deferred<Response<RecipeDetails>>


    @GET("recipes/complexSearch")
    fun getAllRecipes(
        @Query("apiKey") apiKey: String,
    ): Deferred<Response<RecipesResponse>>

    @GET("recipes/complexSearch")
    fun searchRecipes(
        @Query("query") query: String,
        @Query("apiKey") apiKey: String,
    ): Deferred<Response<RecipesResponse>>

    @GET("recipes/{id}/similar")
    fun getSimilarRecipe(
        @Query("id") id: Int, @Query("apiKey") apiKey: String
    ): Deferred<Response<List<SimilarRecipe>>>
}
