package com.example.incollege.main.ui.ormawa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.incollege.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_ormawa.view.*

class OrmawaAdapter(
    private val list: MutableList<DataClassOrmawa>,
    private val listener: ListenerOrmawa
) : RecyclerView.Adapter<OrmawaAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_ormawa, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: DataClassOrmawa) {
            with(itemView) {
                Picasso.with(itemView.context).load(data.image).into(iv_avatar)
                tv_title.text = data.title
                tv_desc.text = data.desc
                cv_layout.setOnClickListener { listener.clickItem(data) }
            }
        }
    }
}