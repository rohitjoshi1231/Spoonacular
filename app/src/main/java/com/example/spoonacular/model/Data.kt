package com.example.spoonacular.model

import kotlinx.serialization.Serializable


data class Spoonacular(
    var recipes: List<Recipe>
)

@Serializable
data class Nutrition(
    val nutrients: List<Nutrient> = emptyList(),
    val properties: List<Property> = emptyList(),
    val flavonoids: List<Flavonoid> = emptyList()
) {

    constructor() : this(emptyList<Nutrient>(), emptyList(), emptyList())

}

data class Item(
    val name: String, val image: String
)


@Serializable
data class Nutrient(
    val name: String = "",
    val amount: Double = 0.0,
    val unit: String = "",
    val percentOfDailyNeeds: Double = 0.0
) {
    constructor() : this("", 0.0, "", 0.0)
}

@Serializable
data class Property(
    val name: String = "", val amount: Double = 0.0, val unit: String = ""
) {
    constructor() : this("", 0.0, "")
}

@Serializable
data class Flavonoid(
    val name: String = "", val amount: Double = 0.0, val unit: String = ""
) {
    constructor() : this("", 0.0, "")
}

@Serializable
data class Measures(
    var us: Measure = Measure(), var metric: Measure = Measure()
) {
    // No-argument constructor for Firestore deserialization
    constructor() : this(Measure(), Measure())
}

@Serializable
data class ExtendedIngredient(
    var id: Int = 0,
    var aisle: String? = null,  // Nullable
    val image: String? = null,
    var consistency: String? = null,  // Nullable
    var name: String = "",  // Nullable
    var original: String? = null,  // Nullable
    var originalString: String? = null,  // Nullable
    var originalName: String? = null,  // Nullable
    var amount: Double = 0.0,
    var unit: String? = null,  // Nullable
    var meta: List<String> = emptyList(),
    var measure: Measure? = null
) {
    // No-argument constructor for Firestore deserialization
    constructor() : this(0, null, null, null, "", null, null, null, 0.0, null, emptyList(), null)
}

@Serializable
data class Recipe(
    var vegetarian: Boolean = false,
    var vegan: Boolean = false,
    var glutenFree: Boolean = false,
    var dairyFree: Boolean = false,
    var veryHealthy: Boolean = false,
    var cheap: Boolean = false,
    var veryPopular: Boolean = false,
    var sustainable: Boolean = false,
    var lowFodmap: Boolean = false,
    var weightWatcherSmartPoints: Int = 0,
    var gaps: String = "",
    var preparationMinutes: Int = 0,
    var cookingMinutes: Int = 0,
    var aggregateLikes: Int = 0,
    var healthScore: Int = 0,
    var creditsText: String = "",
    var sourceName: String = "",
    var pricePerServing: Double = 0.0,
    var extendedIngredients: List<ExtendedIngredient> = emptyList(),
    var id: Int = 0,
    var title: String = "",
    var readyInMinutes: Int = 0,
    var servings: Int = 0,
    var sourceUrl: String = "",
    var image: String = "",
    var imageType: String = "",
    var summary: String = "",
    var cuisines: List<String> = emptyList(),
    var dishTypes: List<String> = emptyList(),
    var diets: List<String> = emptyList(),
    var occasions: List<String> = emptyList(),
    var instructions: String = "",
    var analyzedInstructions: List<AnalyzedInstruction> = emptyList(),
    var originalId: Int? = null,
    var spoonacularScore: Double = 0.0,
    var spoonacularSourceUrl: String = ""
) {
    constructor() : this(
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        0,
        "",
        0,
        0,
        0,
        0,
        "",
        "",
        0.0,
        emptyList(),
        0,
        "",
        0,
        0,
        "",
        "",
        "",
        "",
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList(),
        "",
        emptyList(),
        null,
        0.0,
        ""
    )
}


