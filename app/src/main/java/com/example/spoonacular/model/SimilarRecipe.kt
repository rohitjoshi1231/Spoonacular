package com.example.spoonacular.model

data class SimilarRecipe(
    val id: Int,
    val imageType: String,
    val title: String,
    val readyInMinutes: Int,
    val servings: Int,
    val sourceUrl: String
)
