package studio.mobile_app.chamomile.ui.catalog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
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
        val root = inflater.inflate(R.layout.fragment_group, container, false)
        var mainViewModel = activity?.let { ViewModelProviders.of(it).get(MainViewModel::class.java) }
        var productGroup = mainViewModel?.productGroups?.get(arguments?.getInt(ARG_SECTION_NUMBER) ?: -1)
        if (mainViewModel != null) {
            Log.d("WWWWW_onCreateView", "${ arguments?.getInt(ARG_SECTION_NUMBER) } : ${ mainViewModel.productGroups[arguments?.getInt(ARG_SECTION_NUMBER) ?: -1].id }")
        }
        var positionView: TextView = root.findViewById(R.id.positionView)
        positionView.text = "Position: ${arguments?.getInt(ARG_SECTION_NUMBER) ?: -1} "
        //var fragment: Fragment = root.findViewById(R.id.catalogContainer)
        //fragment.arguments?.putInt("section_number", 15)
        fragmentManager?.let {
            it
                .beginTransaction()
                .replace(R.id.catalogContainer, CategoriesFragment(productGroup ?: ProductGroup(-1, ""), this))
                //.replace(R.id.catalogContainer, EmptyFragment(this.arguments?.getInt(ARG_SECTION_NUMBER) ?: -2))
                .addToBackStack("CATALOG")
                .commit()
        }
        return root
    }

    override fun onListItemClicked(categoty: ProductCategory) {
        fragmentManager?.let {
            it
                .beginTransaction()
                .replace(R.id.catalogContainer, ProductsFragment(categoty, this))
                .addToBackStack("CATALOG")
                .commit()
        }
    }

    override fun onListItemClicked(product: Product) {
        //TODO("Not yet implemented")
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        @JvmStatic
        fun newInstance(sectionNumber: Int): GroupFragment {
            return GroupFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

}


