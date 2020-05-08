package studio.mobile_app.chamomile.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import studio.mobile_app.chamomile.R
import java.text.FieldPosition

/**
 * A simple [Fragment] subclass.
 */
class EmptyFragment(position: Int = -1) : Fragment() {

    val pos = position

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_empty, container, false)
        val textView: TextView = root.findViewById(R.id.textView100)
        //textView.text = arguments?.getInt("section_number").toString()
        textView.text = pos.toString()
        return root
    }

//    companion object {
//        private const val ARG_SECTION_NUMBER = "section_number"
//        @JvmStatic
//        fun newInstance(sectionNumber: Int): GroupFragment {
//            return GroupFragment().apply {
//                arguments = Bundle().apply {
//                    putInt(ARG_SECTION_NUMBER, sectionNumber)
//                }
//            }
//        }
//    }

}
