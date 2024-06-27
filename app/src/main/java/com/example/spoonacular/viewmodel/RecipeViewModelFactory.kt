package com.example.spoonacular.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spoonacular.repository.SpoonacularRepository

class RecipeViewModelFactory(private val spoonacularRepository: SpoonacularRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecipeViewModel(spoonacularRepository) as T
    }
}
