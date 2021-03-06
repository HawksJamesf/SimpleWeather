package com.jamesfchen.uicomponent

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_rn.*

class BottomUpDialogFragmentForSheet : BottomSheetDialogFragment() {
    companion object {

        @JvmStatic
        fun newInstance(): BottomUpDialogFragmentForSheet {
            val args = Bundle()
            val fragment = BottomUpDialogFragmentForSheet()
            fragment.arguments = args
            return fragment
        }
        const val TAG = "RNFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_rn, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        view.setOnClickListener {
            parentFragmentManager.beginTransaction()
                    .remove(this)
                    .commitAllowingStateLoss()

        }
        repeat(20) {
            val textView = TextView(view.context)
            textView.text = "asdfasfsafaf"
            val divider=View(view.context)
            divider.setBackgroundColor(Color.RED)
            ll_container.addView(divider,ViewGroup.LayoutParams.MATCH_PARENT,2)
            ll_container.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT, 400)
        }
    }
//    fun show(fragmentManager: FragmentManager, tag: String) {
//        fragmentManager.beginTransaction()
////                .add(android.R.id.content, tag)
//                .add(android.R.id.content,this)
//                .commitAllowingStateLoss()
//    }
}