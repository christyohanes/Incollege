package com.example.incollege.main.utils

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.incollege.R
import com.example.incollege.main.listener.BottomSheetItemListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bts_alert.*

class BottomSheetAlertFragment(private val listener: BottomSheetItemListener) :
    BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bts_alert, container)
    }

    private var title = ""
    private var subTitle = ""
    private var subText = ""
    private var color = 0
    private var textYesBtn = ""
    private var textNoBtn = ""

    fun setTitle(text: String) {
        title = text
    }

    fun setSubTitle(text: String, subText: String = "") {
        subTitle = text
        this.subText = subText
    }

    fun setSubTitleTextColor(color: Int) {
        this.color = color
    }

    fun setTextYesBtn(text: String) {
        textYesBtn = text
    }

    fun setTextNoBtn(text: String) {
        textNoBtn = text
    }

    private fun setSubtitleTextColor(
        tv: TextView,
        fulltext: String,
        subText: String,
        color: Int,
        context: Context?
    ) {
        tv.setText(fulltext, TextView.BufferType.SPANNABLE)
        val text = tv.text as Spannable
        val index = fulltext.indexOf(subText)
        if (subText.isNotEmpty().and(color == 0)) {
            text.setSpan(
                ForegroundColorSpan(context?.let {
                    ContextCompat.getColor(
                        it,
                        R.color.colorPrimary
                    )
                } ?: 0),
                index,
                index + subText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else if (subText.isNotEmpty()) {
            text.setSpan(
                StyleSpan(Typeface.BOLD),
                index,
                index + subText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_title_bts_alert.text = title
        setSubtitleTextColor(tv_sub_title_bts_alert, subTitle, subText, color, view.context)
        btn_yes_bts_alert.text = textYesBtn
        btn_no_bts_alert.text = textNoBtn
        btn_no_bts_alert.setOnClickListener {
            dismiss()
        }
        btn_yes_bts_alert.setOnClickListener {
            listener.getUserChoice(true)
            dismiss()
        }
    }

}
