package studio.mobile_app.chamomile.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import studio.mobile_app.chamomile.ProductCategory
import studio.mobile_app.chamomile.R
import studio.mobile_app.chamomile.ui.products.ProductClickListener
import studio.mobile_app.chamomile.ui.products.ProductFragment
import studio.mobile_app.chamomile.ui.products.ProductsFragment

class FavouritesFragment : Fragment(), ProductClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_container, container, false)
        childFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, ProductsFragment.newInstance(ProductCategory.FAVOURITES))
            .addToBackStack("CATALOG")
            .commit()
        return root
    }

    override fun onProductClicked(product: String) {
        childFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, ProductFragment.newInstance(product))
            .addToBackStack("CATALOG")
            .commit()
    }
}
