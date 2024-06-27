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
import com.example.spoonacular.model.Recipe
import com.example.spoonacular.ui.activities.InformationActivity
import com.squareup.picasso.Picasso

class HomeAdapter(
    private val context: Context,
    private val recipeList: MutableList<Recipe>
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newRecipes: List<Recipe>) {
        recipeList.clear()
        recipeList.addAll(newRecipes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.popular_recipe_list, parent, false)
        return HomeViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val recipe = recipeList[position]
        holder.data.text = recipe.title
        holder.cookingTime.text = "${recipe.readyInMinutes} minutes"

        holder.itemView.setOnClickListener {
            val intent = Intent(context, InformationActivity::class.java)
            intent.putExtra("recipeId", recipe.id)
            context.startActivity(intent)
        }

        if (recipe.image.isNotEmpty()) {
            Picasso.get().load(recipe.image).placeholder(R.drawable.img).into(holder.recipeImage)
        } else {
            holder.recipeImage.setImageResource(R.drawable.img)
        }
    }

    override fun getItemCount(): Int = recipeList.size

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val data: TextView = view.findViewById(R.id.popular_recipe_name)
        val cookingTime: TextView = view.findViewById(R.id.popular_recipe_time)
        val recipeImage: ImageView = view.findViewById(R.id.imageView)
    }
}
