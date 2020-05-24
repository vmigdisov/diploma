package studio.mobile_app.chamomile.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import studio.mobile_app.chamomile.*
import studio.mobile_app.chamomile.ui.products.ProductClickListener
import studio.mobile_app.chamomile.ui.products.ProductFragment
import studio.mobile_app.chamomile.ui.products.ProductsFragment


interface CategoryClickListener {
    fun onCategoryClicked(categoty: Int)
}

class GroupFragment : Fragment(), CategoryClickListener, ProductClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_container, container, false)
        childFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, CategoriesFragment.newInstance(arguments?.getInt(ARG_POSITION) ?: 0))
                .addToBackStack("CATALOG")
                .commit()
        return root
    }

    override fun onCategoryClicked(categoty: Int) {
        childFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, ProductsFragment.newInstance(categoty))
            .addToBackStack("CATALOG")
            .commit()
    }

    override fun onProductClicked(product: String) {
        childFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, ProductFragment.newInstance(product))
            .addToBackStack("CATALOG")
            .commit()
    }

    companion object {
        private const val ARG_POSITION = "POSITION"
        @JvmStatic
        fun newInstance(position: Int): GroupFragment {
            return GroupFragment().apply {
                arguments = Bundle().apply { putInt(ARG_POSITION, position) }
            }
        }
    }

}


