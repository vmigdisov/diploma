package studio.mobile_app.chamomile.ui.products

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import studio.mobile_app.chamomile.DBManager
import studio.mobile_app.chamomile.Product
import studio.mobile_app.chamomile.ProductCategory

import studio.mobile_app.chamomile.R

class ProductFragment : Fragment() {

    var isFavourite = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_product, container, false)
        activity?.let { ctx ->
            DBManager(ctx).getProduct(arguments?.getString(ARG_PRODUCT) ?: "").let { product ->
                (root.findViewById(R.id.productArticulTextView) as TextView).text = product!!.id
                (root.findViewById(R.id.productNameTextView) as TextView).text = product.name
                (root.findViewById(R.id.productPriceTextView) as TextView).text = product.price.toString()
                if (product.image.size > 0) {
                    val bMap = BitmapFactory.decodeByteArray(product.image, 0, product.image.size)
                    (root.findViewById(R.id.productImageView) as ImageView).setImageBitmap(bMap)
                }
                val favouriteImage: ImageView = root.findViewById(R.id.favouriteImage)
                for (item in DBManager(ctx).getProducts(ProductCategory.FAVOURITES)) {
                    if(item.id == product.id) isFavourite = true
                }
                favouriteImage.setColorFilter(ContextCompat.getColor(context!!, (if (isFavourite) R.color.colorAccent else R.color.colorPrimary)), android.graphics.PorterDuff.Mode.SRC_IN)
                favouriteImage.setOnClickListener {
                    if(isFavourite) { DBManager(ctx).deleteFavourite(product) } else { DBManager(ctx).saveFavourite(product) }
                    isFavourite = !isFavourite
                    (it as ImageView).setColorFilter(ContextCompat.getColor(context!!, (if (isFavourite) R.color.colorAccent else R.color.colorPrimary)), android.graphics.PorterDuff.Mode.SRC_IN)
                }
                val basketButton: ImageButton = root.findViewById(R.id.basketButton)
                basketButton.setOnClickListener {
                    product.basketQuantity++
                    DBManager(ctx).saveBasket(product)
                    Toast.makeText(context, "Товар добавлен в корзину", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return root
    }


    companion object {
        private const val ARG_PRODUCT = "PRODUCT"
        @JvmStatic
        fun newInstance(product: String): ProductFragment {
            return ProductFragment().apply {
                arguments = Bundle().apply { putString(ARG_PRODUCT, product) }
            }
        }
    }

}
