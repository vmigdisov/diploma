package studio.mobile_app.chamomile.ui.basket

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import studio.mobile_app.chamomile.*
import studio.mobile_app.chamomile.ui.products.ProductClickListener

class BasketFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_basket, container, false)
        var recyclerView: RecyclerView = root.findViewById(R.id.basketRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        var products: ArrayList<Product>
        activity?.let {
            products = DBManager(it).getProducts(ProductCategory.BASKET)
            recyclerView.adapter = BasketAdapter(products, it)
        }
        return root
    }

    internal class BasketAdapter(val products: ArrayList<Product>, val context: Context) : RecyclerView.Adapter<BasketAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView: View = LayoutInflater.from(context).inflate(R.layout.basket_cell, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.productNameTextView.setText(products[position].name)
            holder.productPriceTextView.setText(products[position].price.toString())
            holder.quantityEditText.setText(products[position].basketQuantity.toString())
            if (products[position].icon.size > 0) {
                val bMap = BitmapFactory.decodeByteArray(products[position].icon, 0, products[position].icon.size)
                holder.productIcon.setImageBitmap(bMap)
            }
            holder.plusButton.setOnClickListener {
                holder.quantityEditText.setText((holder.quantityEditText.text.toString().toInt() + 1).toString())
                products[position].basketQuantity++
                DBManager(context).saveBasket(products[position])
            }
            holder.minusButton.setOnClickListener {
                if(products[position].basketQuantity > 0) {
                    holder.quantityEditText.setText((holder.quantityEditText.text.toString().toInt() - 1).toString())
                    products[position].basketQuantity--
                    DBManager(context).saveBasket(products[position])
                }
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
            var quantityEditText: EditText
            var productIcon: ImageView
            var plusButton: Button
            var minusButton: Button
            init {
                productNameTextView = itemView.findViewById(R.id.productNameTextView)
                productPriceTextView = itemView.findViewById(R.id.productPriceTextView)
                quantityEditText = itemView.findViewById(R.id.quantityEditText)
                productIcon = itemView.findViewById(R.id.productIconView)
                plusButton = itemView.findViewById(R.id.plusButton)
                minusButton = itemView.findViewById(R.id.minusButton)
            }
        }
    }
}

