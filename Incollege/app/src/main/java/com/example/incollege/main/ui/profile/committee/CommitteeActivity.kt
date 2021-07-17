package com.example.incollege.main.ui.profile.committee

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.incollege.R
import com.example.incollege.main.adapter.CommitteeAdapter
import com.example.incollege.main.listener.DeletePengurusListener
import com.example.incollege.main.model.pengurus.Jabatan
import com.example.incollege.main.model.pengurus.PengurusData
import com.example.incollege.main.model.pengurus.PengurusResponse
import com.example.incollege.main.model.userphoto.EditPhotoResponse
import com.example.incollege.main.network.api.ApiClient
import com.example.incollege.main.network.api.ApiInterface
import com.example.incollege.main.sharedpreferences.SessionManager
import kotlinx.android.synthetic.main.activity_committee.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommitteeActivity : AppCompatActivity(), DeletePengurusListener, View.OnClickListener {

    private var loginData: HashMap<String, String>? = null
    private var userId: String? = null
    private var listPengurusBPH: List<PengurusData>? = ArrayList()
    private var listPengurusOther: List<PengurusData>? = ArrayList()

    private var isBPHOpen: Boolean = false
    private var isOtherOpen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_committee)

        initateData()
    }

    private fun initateData() {
        loginData = SessionManager(this).getUserData()
        userId = loginData?.get("userid")
        getPengurusBPHData(userId, Jabatan.JABATAN_BPH)
        getPengurusOtherData(userId, Jabatan.JABATAN_PENGURUS)
        initiateUI()
    }

    private fun getPengurusOtherData(userId: String?, tj: String) {
        val apiInterface: ApiInterface = ApiClient.instance
        val pengurusDataCall = apiInterface.getPengurusData(userId, tj)
        pengurusDataCall.enqueue(object : Callback<PengurusResponse> {
            override fun onResponse(
                call: Call<PengurusResponse>,
                response: Response<PengurusResponse>
            ) {
                if (response.isSuccessful) {
                    listPengurusOther = response.body()?.data as List<PengurusData>?
                    showRecyclerListOther()
                } else {
                    val responseError =
                        JSONObject(
                            response.errorBody()?.charStream()?.readText() ?: "Error"
                        )
                    Toast.makeText(
                        this@CommitteeActivity,
                        responseError.getString("message"),
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    rv_other_com.visibility = View.INVISIBLE
                    if (isOtherOpen) {
                        btn_add_pengurus_other_extension.visibility = View.VISIBLE
                    } else {
                        btn_add_pengurus_other_extension.visibility = View.GONE
                    }

                }
            }

            override fun onFailure(call: Call<PengurusResponse>, t: Throwable) {
                Toast.makeText(this@CommitteeActivity, "Gagal load data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showRecyclerListOther() {
        rv_other_com.layoutManager = GridLayoutManager(this@CommitteeActivity, 3)
        val pengurusAdapter =
            CommitteeAdapter(listPengurusOther ?: ArrayList(), this@CommitteeActivity)
        rv_other_com.adapter = pengurusAdapter
    }

    private fun getPengurusBPHData(userId: String?, tj: String) {
        val apiInterface: ApiInterface = ApiClient.instance
        val pengurusDataCall = apiInterface.getPengurusData(userId, tj)
        pengurusDataCall.enqueue(object : Callback<PengurusResponse> {
            override fun onResponse(
                call: Call<PengurusResponse>,
                response: Response<PengurusResponse>
            ) {
                if (response.isSuccessful) {
                    listPengurusBPH = response.body()?.data as List<PengurusData>?
                    showRecyclerListBPH()
                } else {
                    val responseError =
                        JSONObject(
                            response.errorBody()?.charStream()?.readText() ?: "Error"
                        )
                    Toast.makeText(
                        this@CommitteeActivity,
                        responseError.getString("message"),
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    rv_bph_com.visibility = View.INVISIBLE
                    if (isBPHOpen) {
                        btn_add_pengurus_bph_extension.visibility = View.VISIBLE
                    } else {
                        btn_add_pengurus_bph_extension.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<PengurusResponse>, t: Throwable) {
                Toast.makeText(this@CommitteeActivity, "Gagal load data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initiateUI() {
        rv_bph_com.isFocusable = false
        rv_bph_com.setHasFixedSize(true)
        btn_back_com.setOnClickListener { onBackPressed() }
        btn_bph_com.setOnClickListener(this)
        btn_other_com.setOnClickListener(this)
        btn_add_pengurus_other_extension.setOnClickListener(this)
        btn_add_pengurus_bph_extension.setOnClickListener(this)
    }

    private fun showRecyclerListBPH() {
        rv_bph_com.layoutManager = GridLayoutManager(this@CommitteeActivity, 3)
        val pengurusAdapter =
            CommitteeAdapter(listPengurusBPH ?: ArrayList(), this@CommitteeActivity)
        rv_bph_com.adapter = pengurusAdapter
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_bph_com -> {
                if (!isBPHOpen) {
                    initateData()
                    rv_bph_com.visibility = View.VISIBLE
                    btn_bph_com.icon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_up)
                    isBPHOpen = true
                } else {
                    initateData()
                    rv_bph_com.visibility = View.GONE
                    btn_bph_com.icon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_down)
                    isBPHOpen = false
                }
            }
            R.id.btn_other_com -> {
                if (!isOtherOpen) {
                    initateData()
                    rv_other_com.visibility = View.VISIBLE
                    btn_other_com.icon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_up)
                    isOtherOpen = true
                } else {
                    initateData()
                    rv_other_com.visibility = View.GONE
                    btn_other_com.icon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_down)
                    isOtherOpen = false
                }
            }
            R.id.btn_add_pengurus_bph_extension -> startActivity(
                Intent(
                    this,
                    AddPengurusActivity::class.java
                )
            )
            R.id.btn_add_pengurus_other_extension -> startActivity(
                Intent(
                    this,
                    AddPengurusActivity::class.java
                )
            )
        }
    }

    override fun onDeletPengurusClicked(item: PengurusData?) {
        val idPengurus = item?.id
        deletePengurus(idPengurus)
    }

    override fun onAddPengurusClicked() {
        startActivity(Intent(this, AddPengurusActivity::class.java))
    }

    private fun deletePengurus(idPengurus: String?) {
        val apiInterface: ApiInterface = ApiClient.instance
        val deletePengurusCall = apiInterface.deletePengurusData(idPengurus, "DELETE")
        deletePengurusCall.enqueue(object : Callback<EditPhotoResponse> {
            override fun onResponse(
                call: Call<EditPhotoResponse>,
                response: Response<EditPhotoResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@CommitteeActivity,
                        response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    getPengurusBPHData(userId, Jabatan.JABATAN_BPH)
                    getPengurusOtherData(userId, Jabatan.JABATAN_PENGURUS)
                } else {
                    val responseError =
                        JSONObject(
                            response.errorBody()?.charStream()?.readText() ?: "Error"
                        )
                    Toast.makeText(
                        this@CommitteeActivity,
                        responseError.getString("message"),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<EditPhotoResponse>, t: Throwable) {
                Toast.makeText(
                    this@CommitteeActivity,
                    "Gagal menghapus pengurus",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


}