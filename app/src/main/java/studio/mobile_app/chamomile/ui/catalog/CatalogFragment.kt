package studio.mobile_app.chamomile.ui.catalog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import studio.mobile_app.chamomile.MainViewModel
import studio.mobile_app.chamomile.ProductGroup
import studio.mobile_app.chamomile.R
import java.util.ArrayList

class CatalogFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_catalog, container, false)
        activity?.let {
            val mainViewModel = ViewModelProviders.of(it).get(MainViewModel::class.java)
            val pagerAdapter = fragmentManager?.let { ProductGroupsPagerAdapter(it, mainViewModel.productGroups) }
            val viewPager: ViewPager = root.findViewById(R.id.viewPager)
            viewPager.adapter = pagerAdapter
            val tabs: TabLayout = root.findViewById(R.id.tabs)
            tabs.setupWithViewPager(viewPager)
        }
        return root
    }
}

class ProductGroupsPagerAdapter(fragmentManager: FragmentManager, productGroups: ArrayList<ProductGroup>): FragmentPagerAdapter(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var groups = productGroups

    override fun getItem(position: Int): Fragment {
        return GroupFragment.newInstance(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return groups[position].name
    }

    override fun getCount(): Int {
        return groups.size
    }
}
