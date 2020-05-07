package studio.mobile_app.chamomile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import studio.mobile_app.chamomile.MainViewModel
import studio.mobile_app.chamomile.R

class BasketFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_basket, container, false)
        val textView: TextView = root.findViewById(R.id.text_basket)
        getActivity()?.let { mainViewModel = ViewModelProviders.of(it).get(MainViewModel::class.java) }
        textView.text = mainViewModel.productGroups[0].name
        return root
    }
}

