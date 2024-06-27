package com.example.spoonacular.model

data class GoogleSignInDetails(
    var firebaseUserId: String = "",
    var displayName: String = "",
    var googleId: String = "",
    var fullName: String = "",
    var email: String = "",
    var photoUrl: String = "",
)