package studio.mobile_app.chamomile.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import studio.mobile_app.chamomile.DBManager
import studio.mobile_app.chamomile.Product
import studio.mobile_app.chamomile.ProductCategory
import studio.mobile_app.chamomile.R
import studio.mobile_app.chamomile.ui.catalog.ProductClickListener

/**
 * A simple [Fragment] subclass.
 */
class ProductsFragment(categoty: ProductCategory, listner: ProductClickListener?) : Fragment() {

    val productCategory = categoty
    val productClickListener = listner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_products, container, false)
        var recyclerView: RecyclerView = root.findViewById(R.id.productsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        var products: ArrayList<Product>
        context?.let {
            products = DBManager(it).getProducts(productCategory.id)
            recyclerView.adapter = ProductsAdapter(products, it, productClickListener)
        }
        return root
    }

    internal class ProductsAdapter(data: ArrayList<Product>, val context: Context, listner: ProductClickListener?) : RecyclerView.Adapter<ProductsAdapter.MyViewHolder>() {
        var products: ArrayList<Product> = data
        var clickListner: ProductClickListener? = listner

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView: View =
                LayoutInflater.from(context).inflate(R.layout.product_cell, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.productNameTextView.setText(products[position].name)
            holder.productPriceTextView.setText(products[position].price.toString())
            holder.itemView.setOnClickListener {
                clickListner?.onListItemClicked(products[position])
            }
        }

        override fun getItemViewType(position: Int): Int {
            return super.getItemViewType(position)
        }

        override fun getItemCount(): Int {
            return products.count()
        }

        internal inner class MyViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var productNameTextView: TextView
            var productPriceTextView: TextView
            init {
                productNameTextView = itemView.findViewById(R.id.productNameTextView)
                productPriceTextView = itemView.findViewById(R.id.productPriceTextView)
            }
        }
    }

}