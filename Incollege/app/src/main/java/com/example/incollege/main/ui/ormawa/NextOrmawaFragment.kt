package com.example.incollege.main.ui.ormawa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.incollege.R
import kotlinx.android.synthetic.main.fragment_next_ormawa.*


class NextOrmawaFragment : Fragment(), ListenerOrmawa {
    private lateinit var Communicator: Communicator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_next_ormawa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tv_title.text = "Fakultas"
        val data = mutableListOf(
            DataClassOrmawa(
                image = R.drawable.ic_avatar1,
                title = "Fakultas Ilmu Komputer",
                desc = "Fakultas Ilmu Komputer UPN Veteran Jakarta merupakan salah satu Fakultas yang berada di dalam lingkup UPN Veteran Jakarta."
            ),
            DataClassOrmawa(
                image = R.drawable.ic_avatar2,
                title = "Fakultas Teknik",
                desc = "Unit Kegiatan Mahasiswa adalah tempat organisasi untuk setiap fakultas, dengan berdasarkan kepada suatu aktivitas bersama."
            ),
            DataClassOrmawa(
                image = R.drawable.ic_avatar3,
                title = "Fakultas Kedokteran",
                desc = "Organisasi di Fakultas meliputi Kelompok Studi Mahasiswa yang berfokus pada bidang masing - masing"
            ),
            DataClassOrmawa(
                image = R.drawable.ic_avatar1,
                title = "Fakultas Hukum",
                desc = "Organisasi yang menaungi seluruh universitas seperti BEM-U dan MPM"
            ),
            DataClassOrmawa(
                image = R.drawable.ic_avatar2,
                title = "Fakultas Ekonomi & Bisnis",
                desc = "Unit Kegiatan Mahasiswa adalah tempat organisasi untuk setiap fakultas, dengan berdasarkan kepada suatu aktivitas bersama."
            ),
            DataClassOrmawa(
                image = R.drawable.ic_avatar3,
                title = "Fakultas Ilmu Kesehatan",
                desc = "Organisasi di Fakultas meliputi Kelompok Studi Mahasiswa yang berfokus pada bidang masing - masing"
            ), DataClassOrmawa(
                image = R.drawable.ic_avatar1,
                title = "Fakultas Ilmu Sosial Ilmu Politik",
                desc = "Organisasi yang menaungi seluruh universitas seperti BEM-U dan MPM"
            )

        )
        rv_ormawa.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = OrmawaAdapter(data, this@NextOrmawaFragment)
        }
    }

    override fun clickItem(data: DataClassOrmawa) {
        Communicator=activity as Communicator
        when (data.title) {

            data.title -> {
                Communicator.passdata(data.title,UkmFragment())
            }
        }
    }




}