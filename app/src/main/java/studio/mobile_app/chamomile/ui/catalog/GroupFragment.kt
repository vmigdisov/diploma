package studio.mobile_app.chamomile.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import studio.mobile_app.chamomile.*
import studio.mobile_app.chamomile.ui.ProductsFragment


interface CategoryClickListener {
    fun onListItemClicked(categoty: ProductCategory)
}

interface ProductClickListener {
    fun onListItemClicked(product: Product)
}

class GroupFragment : Fragment(), CategoryClickListener, ProductClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_container, container, false)
        childFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, CategoriesFragment.newInstance((arguments?.getInt(ARG_POSITION) ?: 0), this))
                .addToBackStack("CATALOG")
                .commit()
        return root
    }

    override fun onListItemClicked(categoty: ProductCategory) {
//        fragmentManager?.let { it
//                .beginTransaction()
//                .replace(R.id.fragmentContainer, ProductsFragment(categoty, this))
//                .addToBackStack("CATALOG")
//                .commit()
//        }
        childFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, ProductsFragment(categoty, this))
            .addToBackStack("CATALOG")
            .commit()
    }

    override fun onListItemClicked(product: Product) {
        //TODO("Not yet implemented")
    }

    companion object {
        private const val ARG_POSITION = "POSITION"
        @JvmStatic
        fun newInstance(position: Int): GroupFragment {
            return GroupFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POSITION, position)
                }
            }
        }
    }

}


