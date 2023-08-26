package com.cmc.recipe.presentation.ui.upload

import com.cmc.recipe.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.cmc.recipe.data.model.Ingredient
import com.cmc.recipe.data.model.response.Ingredients
import java.util.*

class IngredientListAdapter(
    context: Context,
    private val data: List<Ingredients>
) : ArrayAdapter<Ingredients>(context, 0, data) {

    var filteredData: List<Ingredients> = listOf()

    override fun getCount(): Int = filteredData.size

    override fun getItem(position: Int): Ingredients = filteredData[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView

        if (view == null) view = LayoutInflater.from(context).inflate(R.layout.item_ingredient_list, parent, false)

        val tvName = view?.findViewById<TextView>(R.id.tv_name)
        val tvType = view?.findViewById<TextView>(R.id.tv_type)

        tvName?.text = filteredData[position].ingredient_name
        if(filteredData[position].ingredient_type == "INGREDIENTS"){
            tvType?.text = "재료"
        }else{ // SAUCE
            tvType?.text = "양념"
        }

        return view!!
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.toLowerCase(Locale.ROOT) ?: ""

                // Filter the original data list based on the user's search query
                filteredData = data.filter { item ->
                    item.ingredient_name.toLowerCase(Locale.ROOT).contains(query)
                }

                val filterResults = FilterResults()
                filterResults.values = filteredData
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredData = results?.values as? List<Ingredients> ?: emptyList()

                notifyDataSetChanged()
            }
        }
    }
}