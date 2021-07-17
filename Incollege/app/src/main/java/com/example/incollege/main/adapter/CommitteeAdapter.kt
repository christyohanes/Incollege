package com.example.incollege.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.incollege.R
import com.example.incollege.main.listener.DeletePengurusListener
import com.example.incollege.main.model.pengurus.PengurusData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_commitee.view.*


class CommitteeAdapter(
    private val list: List<PengurusData>,
    private val listenerPengurus: DeletePengurusListener
) : RecyclerView.Adapter<CommitteeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == R.layout.item_commitee) {
            return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_commitee, parent, false)
            )
        } else {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_pengurus_add, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == list.size) R.layout.item_pengurus_add else R.layout.item_commitee
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == list.size) {
            holder.bindButtonAdd(listenerPengurus)
        } else {
            holder.bindPengurus(list[position], listenerPengurus)
        }
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindPengurus(pengurus: PengurusData?, listenerPengurus: DeletePengurusListener) {
            with(itemView) {
                Picasso.with(context)
                    .load(pengurus?.photo)
                    .fit()
                    .centerCrop()
                    .into(civ_photo_pengurus)
                tv_jabatan_pengurus.text = pengurus?.jabatan
                tv_nama_pengurus.text = pengurus?.nama

                btn_delete_pengurus.setOnClickListener {
                    listenerPengurus.onDeletPengurusClicked(pengurus)
                }
            }
        }

        fun bindButtonAdd(listenerPengurus: DeletePengurusListener) {
            with(itemView) {
                setOnClickListener {
                    listenerPengurus.onAddPengurusClicked()
                }
            }
        }
    }
}
