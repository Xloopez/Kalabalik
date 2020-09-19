package com.example.myapplication

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer

abstract class GameScoreRecyclerViewAdapter(private val values: MutableList<Player>,) : RecyclerView.Adapter<GameScoreRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_game_score, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        ViewHolder(holder.containerView).bind(values[position], position)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: Player, position: Int){
            binder(containerView, item, position)
        }
    }

    open fun binder(containerView: View, item: Player, position: Int){}


}