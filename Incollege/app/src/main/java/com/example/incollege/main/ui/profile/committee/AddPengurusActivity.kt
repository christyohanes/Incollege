package com.example.incollege.main.ui.profile.committee

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.incollege.R
import kotlinx.android.synthetic.main.activity_add_pengurus.*

class AddPengurusActivity : AppCompatActivity(), View.OnClickListener {

    private var textNama: String? = null
    private var textjabatan: String? = null
    private var tipeJabatan: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pengurus)

        initiateUI()
    }

    private fun initiateUI() {
        btn_back_pengurus_add.setOnClickListener { onBackPressed() }
        btn_user_photo_pengurus_add.setOnClickListener(this)
        btn_save_pengurus_add.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_user_photo_pengurus_add -> {

            }
            R.id.btn_save_pengurus_add -> {
                textNama = et_nama_pengurus_add.text.toString().trim()
                textjabatan = et_jabatan_pengurus_add.text.toString().trim()
                tipeJabatan = getTipeJabatan()
                Toast.makeText(this, "$tipeJabatan, $textNama, $textjabatan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getTipeJabatan(): Int? {
        return when {
            rb_bph_pengurus_add.isChecked -> {
                1
            }
            rb_pengurus_add.isChecked -> {
                2
            }
            else -> {
                null
            }
        }
    }
}