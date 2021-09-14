package com.example.calendarandtasks.util.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarandtasks.R
import com.example.calendarandtasks.data.model.DetailsTask
import com.google.android.material.button.MaterialButton

class DetailAddTaskAdapter(val listDetail : ArrayList<DetailsTask>): RecyclerView.Adapter<DetailAddTaskAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val detail = itemView.findViewById<TextView>(R.id.title_detail)
        val btn = itemView.findViewById<MaterialButton>(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.detail_item_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val detail = listDetail[position]
        holder.detail.text = detail.details
        holder.btn.setOnClickListener {
            listDetail.remove(listDetail[position])
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return listDetail.size
    }
}