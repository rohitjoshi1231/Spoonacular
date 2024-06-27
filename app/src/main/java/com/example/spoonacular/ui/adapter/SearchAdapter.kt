package com.example.spoonacular.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spoonacular.R
import com.example.spoonacular.model.Item
import com.example.spoonacular.ui.fragments.SortBottomSheetFragment


class SearchAdapter(
    private val fragmentManager: FragmentManager,
    private val ingredients: List<Item>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.searched_query, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = ingredients[position]
        holder.name.text = item.name

        holder.itemView.setOnClickListener {
            val sortBottomSheet = SortBottomSheetFragment()
            sortBottomSheet.show(fragmentManager, sortBottomSheet.tag)
            listener.onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.searchbar_ingredient_name)
    }
}

