package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentGameScoreListBinding
import kotlinx.android.synthetic.main.fragment_item_game_score.view.*
//TODO TA VÄCK SYNTETHICS ERS. MED viewBinding

class GameScoreFragment(val miniScore: Boolean) : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private var scoreAdapter: CustomMutableListRecViewAdapter<Player>? = null
    private var _binding: FragmentGameScoreListBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private lateinit var list: MutableList<Player>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        _binding = FragmentGameScoreListBinding.inflate(layoutInflater, container, false)
        sharedViewModel =  ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView

        list = when (miniScore) {
            true -> {
                sharedViewModel.listOfPlayers;
            }
            false -> {
                sharedViewModel.getPlayerFinalResultSorted()
            }
        }

        scoreAdapter = object: CustomMutableListRecViewAdapter<Player>(R.layout.fragment_item_game_score, list){

            override fun binder(containerView: View, item: Player, position: Int) {
                super.binder(containerView, item, position)

                containerView.apply {
                    item_player_name.text = item.name
                    item_score.text = item.sumPointsFromListPair().toString()
                }

            }

        }
        recyclerView.adapter = scoreAdapter
    }

}