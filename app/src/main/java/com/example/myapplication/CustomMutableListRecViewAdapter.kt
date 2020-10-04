package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class CustomMutableListRecViewAdapter<T>(
    private val layoutId: Int,
    private val mutableList: MutableList<T>?) :
    RecyclerView.Adapter<CustomMutableListRecViewAdapter<T>.ViewHolder>() {

    var rObserver: RecyclerView.AdapterDataObserver? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder((LayoutInflater.from(parent.context).inflate(layoutId, parent, false)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        ViewHolder(holder.containerView).bind(mutableList!![position], position)
    }

    override fun getItemCount(): Int = mutableList?.size ?: 0

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: T, position: Int){
            binder(containerView, item, position)
        }
    }

    open fun binder(containerView: View, item: T, position: Int){}
    
    open fun registerObserver(observer: RecyclerView.AdapterDataObserver){ registerAdapterDataObserver(observer) }
    
    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.registerAdapterDataObserver(observer)
    }
    
}