package com.example.incollege.main.ui.ormawa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.incollege.R
import kotlinx.android.synthetic.main.activity_add_pengurus.*
import kotlinx.android.synthetic.main.fragment_ormawa.*

class OrmawaFragment : Fragment(), ListenerOrmawa {
    private lateinit var Communicator: Communicator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_ormawa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val data = mutableListOf(
            DataClassOrmawa(
                image = R.drawable.ic_avatar1,
                title = "Universitas",
                desc = "Organisasi yang menaungi seluruh universitas seperti BEM-U dan MPM"
            ),
            DataClassOrmawa(
                image = R.drawable.ic_avatar2,
                title = "Unit Kegiatan Mahasiswa",
                desc = "Unit Kegiatan Mahasiswa adalah tempat organisasi untuk setiap fakultas, dengan berdasarkan kepada suatu aktivitas bersama."
            ),
            DataClassOrmawa(
                image = R.drawable.ic_avatar3,
                title = "Fakultas",
                desc = "Organisasi di Fakultas meliputi Kelompok Studi Mahasiswa yang berfokus pada bidang masing - masing"
            )
        )
        showList(data)


    }

    private fun showList(data: MutableList<DataClassOrmawa>) {
        rv_ormawa.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = OrmawaAdapter(data, this@OrmawaFragment)
        }
    }

    override fun clickItem(data: DataClassOrmawa) {
        Communicator = activity as Communicator
        when (data.title) {
            "Universitas" -> {
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    add(
                        R.id.navHostFragmentHomePage, NextOrmawaFragment(), "Ormawa"
                    )
                    addToBackStack(null)
                    commit()
                }
            }
            "Unit Kegiatan Mahasiswa" -> {
                Communicator.passdata("Unit Kegiatan Mahasiswa", UkmFragment())
            }
            "Fakultas" -> {
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    add(
                        R.id.navHostFragmentHomePage, NextOrmawaFragment()
                    )
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }
}