package studio.mobile_app.chamomile.ui.catalog

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import studio.mobile_app.chamomile.MainViewModel
import studio.mobile_app.chamomile.ProductCategory
import studio.mobile_app.chamomile.R


class CategoriesFragment() : Fragment() {

    companion object {
        private const val ARG_POSITION = "POSITION"
        @JvmStatic
        fun newInstance(position: Int): CategoriesFragment {
            return CategoriesFragment().apply {
                arguments = Bundle().apply { putInt(ARG_POSITION, position) }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_categories, container, false)
        var recyclerView: RecyclerView = root.findViewById(R.id.categoriesRecycleView)
        //recyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = GridLayoutManager(activity,3)
        recyclerView.addItemDecoration(ItemOffsetDecoration(context!!, R.dimen.item_offset))
        var categories: ArrayList<ProductCategory>
        activity?.let {
            var mainViewModel = ViewModelProviders.of(it).get(MainViewModel::class.java)
            var productGroup = mainViewModel.productGroups[arguments?.getInt(ARG_POSITION) ?: -1]
            categories = mainViewModel.productCategories.filter { it.productGroup == productGroup.id } as ArrayList<ProductCategory>
            recyclerView.adapter = CategoriesAdapter(categories, it, parentFragment as? CategoryClickListener)
        }
        return root
    }

    internal class CategoriesAdapter(val categories: ArrayList<ProductCategory>, val context: Context, val clickListner: CategoryClickListener?) : RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView: View = LayoutInflater.from(context).inflate(R.layout.category_cell, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.categoryCaption.setText(categories[position].name)
            holder.itemView.setOnClickListener { clickListner?.onCategoryClicked(categories[position].id) }
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

    class ItemOffsetDecoration(private val mItemOffset: Int) : ItemDecoration() {

        constructor(context: Context, @DimenRes itemOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect[mItemOffset, mItemOffset, mItemOffset] = mItemOffset
        }

    }

}
