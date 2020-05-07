package studio.mobile_app.chamomile.ui.catalog

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import studio.mobile_app.chamomile.MainViewModel
import studio.mobile_app.chamomile.ProductCategory
import studio.mobile_app.chamomile.ProductGroup
import studio.mobile_app.chamomile.R

class CategoriesFragment(group: ProductGroup = ProductGroup(-1, ""), listner: CategoryClickListener? = null) : Fragment() {

    var productGroup = group
    var categoryClickListener = listner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_categories, container, false)
        var recyclerView: RecyclerView = root.findViewById(R.id.categoriesRecycleView)
        recyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        var categories: ArrayList<ProductCategory>
        activity?.let {
            categories = (ViewModelProviders.of(it).get(MainViewModel::class.java)).productCategories.filter { it.productGroup == productGroup.id } as ArrayList<ProductCategory>
            recyclerView.adapter = CategoriesAdapter(categories, it, categoryClickListener)
        }
        return root
    }

    internal class CategoriesAdapter(data: ArrayList<ProductCategory>, val context: Context, listner: CategoryClickListener?) : RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>() {
        var categories: ArrayList<ProductCategory> = data
        var clickListner: CategoryClickListener? = listner
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView: View = LayoutInflater.from(context).inflate(R.layout.category_cell, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.categoryCaption.setText(categories[position].name)
            holder.itemView.setOnClickListener {
                holder.itemView.setBackgroundColor(Color.YELLOW);
                clickListner?.onListItemClicked(categories[position])

            }
        }

        override fun getItemViewType(position: Int): Int {
            return super.getItemViewType(position)
        }

        override fun getItemCount(): Int {
            return categories.size
        }

        internal inner class MyViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var categoryCaption: TextView
            init {
                categoryCaption = itemView.findViewById(R.id.categoryCaption)
            }
        }

    }

}
