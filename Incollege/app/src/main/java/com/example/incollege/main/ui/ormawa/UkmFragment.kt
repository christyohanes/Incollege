package com.example.incollege.main.ui.ormawa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.incollege.R
import com.example.incollege.main.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_ukm.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UkmFragment : Fragment(),ListenerOrmawa {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ukm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val name=arguments?.getString("title")
        tv_title.text=name
        Toast.makeText(activity, "$name", Toast.LENGTH_SHORT).show()
        getDataHttp(name!!)
    }
    private fun showList(data:MutableList<DataClassOrmawa>){
        rv_ormawa.apply {
            setHasFixedSize(true)
            layoutManager=LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
            adapter=OrmawaAdapter(data,this@UkmFragment)
        }
    }


    private fun getDataHttp(name:String) {
        RetrofitOrmawa().getRetroClientInstance("https://test.com.endpoint/").create(Get::class.java)
                .getData(name)
                .enqueue(object : Callback<ResponseOrmawa> {
                    override fun onResponse(call: Call<ResponseOrmawa>, response: Response<ResponseOrmawa>) {
                        val res = response.body() as MutableList<DataClassOrmawa>
                        showList(res)
                    }
                    override fun onFailure(call: Call<ResponseOrmawa>, t: Throwable) {
                        Toast.makeText(activity, "data gagal di ambil", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    override fun clickItem(data: DataClassOrmawa) {
        when(data.title){
            data.title -> {
                Toast.makeText(activity, "${data.title}", Toast.LENGTH_SHORT).show()
            }
        }
    }


}