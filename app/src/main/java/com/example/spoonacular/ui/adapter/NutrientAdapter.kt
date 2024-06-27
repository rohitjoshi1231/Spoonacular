package com.example.spoonacular.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spoonacular.R
import com.example.spoonacular.model.Nutrient

class NutrientAdapter(private val ingredients: List<Nutrient>) :
    RecyclerView.Adapter<NutrientAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.nutrition_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.name.text = ingredient.name
        holder.amount.text = ingredient.amount.toString()
        holder.unit.text = ingredient.unit
        holder.percent.text = ingredient.percentOfDailyNeeds.toString()
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nutrient_name)
        val amount: TextView = itemView.findViewById(R.id.nutrient_amount)
        val unit: TextView = itemView.findViewById(R.id.nutrient_unit)
        val percent: TextView = itemView.findViewById(R.id.nutrient_percentOfDailyNeeds)

    }
}