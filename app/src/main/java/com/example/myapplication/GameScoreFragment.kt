package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.CustomMutableListRecViewAdapter
import com.example.myapplication.databinding.FragmentFinalScoreBinding
import com.example.myapplication.databinding.FragmentItemGameScoreBinding
import com.example.myapplication.databinding.RecyclerViewScoreBinding
import com.example.myapplication.dataclasses.Player
import com.example.myapplication.viewmodels.SharedViewModel

class GameScoreFragment(val miniScore: EnScore = EnScore.FINAL) : Fragment() {

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
            EnScore.MINI -> {
                _binding = RecyclerViewScoreBinding.inflate(layoutInflater, container, false)
                binding.root
            }
            EnScore.FINAL -> {
                _binding2 = FragmentFinalScoreBinding.inflate(layoutInflater, container, false)
                binding2.root
            }


        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyViewBinding()
        recView.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.recycler_view_layout_anim)
        setUpAdapter() //TODO Detailed list-adapter or list?
        recView.scheduleLayoutAnimation()

    }

    //TODO DISPLAY DETAILED LIST ONCLICK?
    private fun setUpAdapter() {
        
        scoreAdapter = object : CustomMutableListRecViewAdapter<Player>(R.layout.fragment_item_game_score, list) {

            val max = list.maxOf { p: Player -> p.sumPointsFromListCards() }

            override fun binder(containerView: View, item: Player, position: Int) {
                super.binder(containerView, item, position)

                val binding = FragmentItemGameScoreBinding.bind(containerView)

                binding.apply {
                    itemPlayerName.text = item.name
                    itemPlacement.text =
                        if ((miniScore == EnScore.FINAL) and (item.sumPointsFromListCards() == max)) {
                            "WINNER"
                        } else {
                            ""
                        }
                    itemScore.text = "${item.sumPointsFromListCards()}"
                  //  "${item.getCardsList().forEach { Log.d("!", "$it") }}"
                }
            }

        }
        recView.adapter = scoreAdapter

    }

    private fun applyViewBinding() {

        when (miniScore) {
            EnScore.MINI -> {
                recView = binding.recyclerViewScore
                setList(sharedViewModel.listOfPlayers)
            }
            EnScore.FINAL -> {
                recView = binding2.recyclerViewFinalScore
                setList(sharedViewModel.lopSortedByPoints)
            }
        }
    }

    private fun setList(sList: MutableList<Player>) {
        list = sList
    }

}