package com.lee.mystudy.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.blankj.utilcode.util.LogUtils
import com.lee.mystudy.R
import com.lee.mystudy.util.LogUtil


class MainAdapter : RecyclerView.Adapter<MainAdapter.MyViewHolder> {
    private var itemClickListener : onItemClickListener? = null
    private var itemLongClickListener : onItemLongClickListener? = null
    private var mContext : Context?= null
    private var dataList : ArrayList<String> ?= null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) :MyViewHolder{
        val view = LayoutInflater.from(mContext!!).inflate(R.layout.item_main, parent, false)

        return MyViewHolder(view)
    }
    override fun getItemCount(): Int {
       return dataList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder,position: Int){

        holder.name.text =  dataList!![position]
        LogUtil.d(dataList!![position])

        if(position%2 == 0){
            holder.itemView.setBackgroundResource(R.drawable.btn_bg_main_green)
        }else{
            holder.itemView.setBackgroundResource(R.drawable.btn_bg_main_yellow)

        }
        holder.itemView.setOnClickListener{

            val layoutPosition = holder.layoutPosition

            itemClickListener?.onItemClick(holder.itemView,layoutPosition)

        }


    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name= itemView.findViewById<TextView>(R.id.tv_titleName)
    }

    constructor (context: Context, list : ArrayList<String>){
        mContext = context
        dataList = list

    }


    interface onItemClickListener  {
        fun onItemClick(view: View,position: Int)
    }
    interface onItemLongClickListener  {
        fun onItemLongClick(view: View,position: Int)
    }
    fun setOnItemClickListener(item: onItemClickListener){
        itemClickListener  = item
    }

    fun setOnItemLongClickListener(item: onItemLongClickListener){
        itemLongClickListener  = item
    }
}