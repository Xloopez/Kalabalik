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

//    inner class Observer: RecyclerView.AdapterDataObserver(){
//        override fun onChanged() {
//            Log.d("!", "ITEMCHANGED")
//            super.onChanged()
//        }
//
//        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
//            super.onItemRangeChanged(positionStart, itemCount)
//            Log.d("!", "ITEMCHANGED at $positionStart count $itemCount")
//
//        }
//
//        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
//            super.onItemRangeChanged(positionStart, itemCount, payload)
//        }
//
//        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
//            super.onItemRangeInserted(positionStart, itemCount)
//        }
//
//        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
//            super.onItemRangeRemoved(positionStart, itemCount)
//        }
//
//        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
//            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
//        }
//    }
//
//    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
//        rObserver = Observer()
//        this.registerAdapterDataObserver(rObserver!!)
//        //super.registerAdapterDataObserver(observer)
//    }
//
//    fun getObserver(): RecyclerView.AdapterDataObserver?{
//        return rObserver
//    }




}