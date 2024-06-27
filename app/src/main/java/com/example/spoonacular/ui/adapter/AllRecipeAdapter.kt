package com.example.spoonacular.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spoonacular.R
import com.example.spoonacular.model.Results
import com.example.spoonacular.ui.activities.InformationActivity
import com.google.android.gms.ads.AdRequest
import com.squareup.picasso.Picasso

class AllRecipeAdapter(
    private val context: Context,
    private val recipeList: MutableList<Results>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_RECIPE = 0
        private const val VIEW_TYPE_AD = 1
        private const val AD_FREQUENCY = 5
    }

    override fun getItemViewType(position: Int): Int {
        return if ((position + 1) % AD_FREQUENCY == 0) VIEW_TYPE_AD else VIEW_TYPE_RECIPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_AD) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ad, parent, false)
            AdViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.all_recipe_list, parent, false)
            RecipeViewHolder(view)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_AD) {
            val adViewHolder = holder as AdViewHolder
            val adRequest = AdRequest.Builder().build()
            adViewHolder.adView.loadAd(adRequest)
        } else {
            val recipeViewHolder = holder as RecipeViewHolder
            val recipePosition = position - position / AD_FREQUENCY
            val recipe = recipeList[recipePosition]

            recipeViewHolder.data.text = recipe.title
            Picasso.get().load(recipe.image).placeholder(R.drawable.img)
                .into(recipeViewHolder.recipeImage)

            recipeViewHolder.itemView.setOnClickListener {
                val intent = Intent(context, InformationActivity::class.java)
                intent.putExtra("recipeId", recipe.id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return recipeList.size + recipeList.size / (AD_FREQUENCY - 1)
    }

    class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val data: TextView = view.findViewById(R.id.all_recipe_name)
        val recipeImage: ImageView = view.findViewById(R.id.all_recipe_img)
    }

    class AdViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val adView: com.google.android.gms.ads.AdView = view.findViewById(R.id.adView)
    }
}
