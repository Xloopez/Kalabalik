package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentFinalScoreBinding
import com.example.myapplication.databinding.FragmentItemGameScoreBinding
import com.example.myapplication.databinding.RecyclerViewScoreBinding

class GameScoreFragment(val miniScore: Boolean) : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private var scoreAdapter: CustomMutableListRecViewAdapter<Player>? = null

    private var _binding: RecyclerViewScoreBinding? = null
    private var _binding2: FragmentFinalScoreBinding? = null

    private val binding get() = _binding!!
    private val binding2 get() = _binding2!!

    private lateinit var recView: RecyclerView

    private lateinit var list: MutableList<Player>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sharedViewModel =  ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        return when (miniScore) {
            true -> {
                _binding = RecyclerViewScoreBinding.inflate(layoutInflater, container, false)
                binding.root
            }
            false -> {
                _binding2 = FragmentFinalScoreBinding.inflate(layoutInflater, container, false)
                binding2.root
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyViewBinding()
        recView.layoutAnimation = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.recycler_view_layout_anim)
        determineList()
        setUpAdapter()
        recView.scheduleLayoutAnimation()

    }
    
    private fun setUpAdapter() {
        
        scoreAdapter = object : CustomMutableListRecViewAdapter<Player>(R.layout.fragment_item_game_score, list) {

            val max = list.maxOf { p: Player -> p.sumPointsFromListPair() }

            override fun binder(containerView: View, item: Player, position: Int) {
                super.binder(containerView, item, position)

                val binding = FragmentItemGameScoreBinding.bind(containerView)
                
                binding.apply {
                    itemPlayerName.text = item.name
                    itemPlacement.text = if(!miniScore and (item.sumPointsFromListPair() == max)){ "WINNER" } else { "" }
                    itemScore.text = "${item.sumPointsFromListPair()}"
                }
            }
        }
        recView.adapter = scoreAdapter
        

    }

    private fun applyViewBinding(){

        recView = when (miniScore) {
            true ->  binding.recyclerViewScore
            false ->  binding2.recyclerViewFinalScore
        }
    }

    private fun determineList() {
        list = when (miniScore) {
            true ->  sharedViewModel.listOfPlayers
            false ->  sharedViewModel.lopSortedByPoints
        }
    }

}