package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.EnScore.FINAL
import com.example.myapplication.EnScore.MINI
import com.example.myapplication.adapters.CustomMutableListRecViewAdapter
import com.example.myapplication.databinding.FragmentFinalScoreBinding
import com.example.myapplication.databinding.FragmentItemGameScoreBinding
import com.example.myapplication.databinding.FragmentItemGameScoreDetailedBinding
import com.example.myapplication.databinding.RecyclerViewScoreBinding
import com.example.myapplication.dataclasses.Player
import com.example.myapplication.gaming.CardMissionConsequence

class GameScoreFragment(val miniScore: EnScore = FINAL) : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private var scoreAdapter: CustomMutableListRecViewAdapter<Player>? = null

    private var _binding: RecyclerViewScoreBinding? = null
    private var _binding2: FragmentFinalScoreBinding? = null

    private val binding get() = _binding!!
    private val binding2 get() = _binding2!!

    private lateinit var recView: RecyclerView
    private lateinit var list: MutableList<Player>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        return when (miniScore) {
            MINI -> {
                _binding = RecyclerViewScoreBinding.inflate(layoutInflater, container, false)
                binding.root
            }
            FINAL -> {
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
        setUpAdapter()
        recView.scheduleLayoutAnimation()

    }

    private fun setUpAdapter() {

        scoreAdapter = object : CustomMutableListRecViewAdapter<Player>(R.layout.fragment_item_game_score, list) {

            val max = list.maxOf { p: Player -> p.sumPointsFromListCards() }
            val icMore = getDrawable(requireContext(), R.drawable.ic_expand_more_24px)
            val icLess = getDrawable(requireContext(), R.drawable.ic_expand_less_black_18dp)

            override fun binder(containerView: View, item: Player, position: Int) {
                super.binder(containerView, item, position)

                val binding = FragmentItemGameScoreBinding.bind(containerView)

                binding.apply {
                    itemPlayerName.text = item.name
                    itemPlacement.text =
                        if ((miniScore == FINAL) and (item.sumPointsFromListCards() == max)) {
                            "WINNER"
                        } else {
                            ""
                        }
                    itemScore.text = "${item.sumPointsFromListCards()}"

                    when (miniScore) {
                        MINI -> {
                            buttonMoreLess.visibility = View.INVISIBLE
                            itemDetailedRecyclerView.adapter = null
                        }
                        FINAL -> {
                            buttonMoreLess.visibility = View.VISIBLE

                            if (item.isDetailedShow) {
                                buttonMoreLess.setImageDrawable(icLess)
                                itemDetailedRecyclerView.visibility = View.VISIBLE
                            } else {
                                buttonMoreLess.setImageDrawable(icMore)
                                itemDetailedRecyclerView.visibility = View.GONE
                            }

                            itemDetailedRecyclerView.apply {
                                layoutAnimation = AnimationUtils.loadLayoutAnimation(
                                    requireContext(),
                                    R.anim.recycler_view_layout_anim
                                )
                                adapter = detailedScoreAdapter(item.listOfCards)
                            }

                            containerView.setOnClickListener {
                                if (item.isDetailedShow) {
                                    item.isDetailedShow = false
                                    scoreAdapter!!.notifyItemChanged(position)
                                } else {
                                    item.isDetailedShow = true
                                    scoreAdapter!!.notifyItemChanged(position)
                                }
                            }
                        }
                    }

                }

            }

        }
        recView.adapter = scoreAdapter

    }

    private fun detailedScoreAdapter(listOfPlayerDetailedScore: MutableList<CardMissionConsequence>): CustomMutableListRecViewAdapter<CardMissionConsequence> {

        return object : CustomMutableListRecViewAdapter<CardMissionConsequence>(
            R.layout.fragment_item_game_score_detailed,
            listOfPlayerDetailedScore
        ) {

            override fun binder(containerView: View, item: CardMissionConsequence, position: Int) {
                super.binder(containerView, item, position)

                val binding = FragmentItemGameScoreDetailedBinding.bind(containerView)

                val round = item.getRound()
                val points = item.points
                val cardStr = item.listStr
                val cardType = resources.getString(item.getType())

                binding.apply {
                    itemDetailedRound.text = "$round"
                    itemDetailedPoints.text = "$points"
                    itemDetailedListString.text = cardStr
                    itemDetailedCardType.text = cardType
                }
            }

        }

    }


    private fun applyViewBinding() {

        when (miniScore) {
            MINI -> {
                recView = binding.recyclerViewScore
                setList(sharedViewModel.listOfPlayers)
            }
            FINAL -> {
                with(binding2) {
                    recView = recyclerViewFinalScore
                }
                setList(sharedViewModel.lopSortedByPoints)
            }
        }
    }

    private fun setList(sList: MutableList<Player>) {
        list = sList
    }


}