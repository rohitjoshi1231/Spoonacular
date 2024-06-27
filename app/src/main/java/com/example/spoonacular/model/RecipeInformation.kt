package com.example.spoonacular.model

import com.google.firebase.auth.FirebaseAuth
import kotlinx.serialization.Serializable

private val auth = FirebaseAuth.getInstance()

@Serializable
data class RecipeDetails(
    var uid: String = auth.currentUser?.uid.toString(),
    var vegetarian: Boolean? = false,
    var vegan: Boolean? = false,
    var glutenFree: Boolean? = false,
    var dairyFree: Boolean? = false,
    var veryHealthy: Boolean? = false,
    var cheap: Boolean? = false,
    var veryPopular: Boolean? = false,
    var sustainable: Boolean? = false,
    var lowFodmap: Boolean? = false,
    var weightWatcherSmartPoints: Int? = 0,
    var gaps: String? = "",
    var preparationMinutes: Int? = 0,
    var cookingMinutes: Int? = 0,
    var aggregateLikes: Int? = 0,
    var healthScore: Int? = 0,
    var creditsText: String? = "",
    var sourceName: String? = "",
    var pricePerServing: Double? = 0.0,
    var extendedIngredients: List<ExtendedIngredient>? = emptyList(),
    var id: Int? = 0,
    var title: String? = "",
    var readyInMinutes: Int? = 0,
    var servings: Int? = 0,
    var sourceUrl: String? = "",
    var image: String? = "",
    var imageType: String? = "",
    var summary: String? = "",
    var cuisines: List<String>? = emptyList(),
    var dishTypes: List<String>? = emptyList(),
    var diets: List<String>? = emptyList(),
    var occasions: List<String>? = emptyList(),
    var instructions: String? = "",
    var analyzedInstructions: List<AnalyzedInstruction>? = emptyList(),
    var nutrition: Nutrition = Nutrition()
) {
    constructor() : this(
        auth.currentUser?.uid.toString(),
        false, false, false, false, false, false, false, false,
        false, 0, "", 0, 0, 0, 0, "", "", 0.0,
        emptyList(), 0, "", 0, 0, "", "", "", "",
        emptyList(), emptyList(), emptyList(), emptyList(), "", emptyList()
    )
}

@Serializable
data class Results(
    var id: Int = 0,
    var title: String = "",
    var image: String = "",
    var imageType: String = ""
) {
    constructor() : this(0, "", "", "")
}


@Serializable
data class RecipesResponse(
    var results: List<Results> = emptyList(),
    var offset: Int = 0,
    var number: Int = 0,
    var totalResults: Int = 0
) {
    constructor() : this(emptyList(), 0, 0, 0)
}

@Serializable
data class Measure(
    var us: MeasureInfo = MeasureInfo(),
    var metric: MeasureInfo = MeasureInfo()
) {
    constructor() : this(MeasureInfo(), MeasureInfo())
}

@Serializable
data class MeasureInfo(
    var amount: Double = 0.0,
    var unitShort: String = "",
    var unitLong: String = ""
) {
    // No-argument constructor for Firestore deserialization
    constructor() : this(0.0, "", "")
}

@Serializable
data class AnalyzedInstruction(
    var name: String = "",
    var steps: List<Step> = emptyList()
) {
    // No-argument constructor for Firestore deserialization
    constructor() : this("", emptyList())
}

@Serializable
data class Step(
    var number: Int = 0,
    var step: String = "",
    var ingredients: List<Ingredient> = emptyList(),
    var equipment: List<Equipment> = emptyList(),
    var length: MeasureLength? = null
) {
    constructor() : this(0, "", emptyList(), emptyList(), null)
}

@Serializable
data class Ingredient(
    var id: Int = 0,
    var name: String = "",
    var localizedName: String = "",
    var image: String = ""
) {
    constructor() : this(0, "", "", "")
}

@Serializable
data class Equipment(
    var id: Int = 0,
    var name: String = "",
    var localizedName: String = "",
    var image: String = ""
) {
    constructor() : this(0, "", "", "")
}

@Serializable
data class MeasureLength(
    var number: Int = 0,
    var unit: String = ""
) {
    constructor() : this(0, "")
}

