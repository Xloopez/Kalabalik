package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Animationz.flipCard
import com.example.myapplication.Util.disableViewClickTemp
import com.example.myapplication.databinding.FragmentGamingBinding
import kotlinx.android.synthetic.main.fragment_item_game_score.view.*

class GamingFragment : Fragment(), View.OnClickListener {

    lateinit var sharedViewModel: SharedViewModel
    lateinit var gamingViewModel: GamingViewModel

    private var _binding: FragmentGamingBinding? = null
    private val binding get() = _binding!!

    private lateinit var tvPlayerName: AppCompatTextView
    private lateinit var tvCurrRound: AppCompatTextView
    private lateinit var tvTotalRounds: AppCompatTextView
    private lateinit var tvCard: AppCompatTextView

    private lateinit var recView: RecyclerView

    private var scoreAdapter: GameScoreRecyclerViewAdapter<Player>? = null

    private lateinit var btnSuccess: AppCompatButton
    private lateinit var btnFail: AppCompatButton

    private val arrayOfEnRandoms = EnRandom.values()
    private val listOfConsequences by lazy { resources.getStringArray(R.array.Consequences) }
    private val listOfMissions by lazy { resources.getStringArray(R.array.Mission) }
    private val listOfConsequencesPoints by lazy { resources.getIntArray(R.array.ConsequencesPoints) }
    private val listOfMissionsPoints by lazy { resources.getIntArray(R.array.MissionPoints) }


    private var pCount = 0
    private var maxRounds = 0
    private var currRound = 0
    private var currTurn = 0
    private var pointsToAdd: Double = 0.0
    private var pointsToAdd2: Double = 0.0
    private var currentCardType: String = ""
    private var totalTurns = 0

    private lateinit var currPlayer: Player

    private enum class EnOperation { SUCCESS, FAIL; }
    private enum class EnRandom { CONSEQUENCES, MISSION;

        fun getEnumString(): String = when(this) {
            CONSEQUENCES -> "CONSEQUENCE"
            MISSION -> "MISSION"
        }
    }

    //TODO Button Cancel before done? Show final score?3
    //TODO Override backbutton?

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel =  ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        gamingViewModel =  ViewModelProvider(this).get(GamingViewModel::class.java) // SCOPE TO ACTIVITY? MAYBE..
        _binding = FragmentGamingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyViewBinding()
        btnSuccess.setOnClickListener(this)
        btnFail.setOnClickListener(this)

        setUpAdapter()

        pCount =  sharedViewModel.playerCount.value!!
        maxRounds = sharedViewModel.amountOfRounds.value!!
        totalTurns = maxRounds.times(pCount).plus(maxRounds)
        tvTotalRounds.apply { text = "out of $maxRounds" }
        gamingViewModel.currentTurn.value = 0
        nextRound()

        gamingViewModel.currentTurn.observe(this, {
            currTurn = it

            Log.d("!", "$currTurn $totalTurns")
            val v = currTurn % pCount.plus(1)

            when (it == totalTurns) {
                true -> {

                    tvPlayerName.apply { text = "GAME ENDED - TAKE ME TO SCORE" }

                    Util.apply {
                        viewApplyVis(btnSuccess, View.INVISIBLE)
                        viewApplyVis(btnFail, View.INVISIBLE) //GONE MAYBE..
                        viewApplyVis(tvCard, View.INVISIBLE)
                    }

                    logScores()

                }
                false -> {
                    when (v) {
                        0 -> {
                            //TODO SHOW CURRENT TOTALPOINTS?3
                            btnSuccess.apply {
                                text  = "NEXT ROUND"
                                setOnClickListener {
                                    nextRound()
                                }
                            }
                            tvPlayerName.apply { text = "CURRENT POINTS" }
                            scoreAdapter!!.notifyDataSetChanged()

                            Util.apply {
                                viewApplyVis(btnFail, View.INVISIBLE)
                                viewApplyVis(tvCard, View.GONE)
                                viewApplyVis(recView, null)
                            }

                        }
                        else -> {
                            btnSuccess.setOnClickListener(this)
                            Util.apply {
                                viewApplyVis(btnFail, null)
                               // viewApplyVis(tvCard, View.VISIBLE)
                                viewApplyVis(recView, View.GONE)
                            }
                            nextPlayerTurn()
                        }
                    }
                }
            }

        })

        gamingViewModel.currentRound.observe(this, {
            currRound = it
            val rnd = "ROUND $it"
            tvCurrRound.apply { text = rnd }
            if (isFinalRound()) { displayFinalRoundStarted() }
        })
    }



    private fun applyViewBinding(){

        binding.apply {

            /* TEXTVIEWS */
            tvPlayerName = textViewPlayerName
            tvCard = textViewGamingInfo
            tvCurrRound = textViewCurrentRound
            tvTotalRounds = textViewAmountOfRounds

            /* RECYCLERVIEW */
            recView = recyclerViewInc.recyclerView

            /* BUTTONS */
            btnSuccess = buttonSuccess
            btnFail = buttonFail

        }
    }

    private fun setUpAdapter() {

        scoreAdapter = object: GameScoreRecyclerViewAdapter<Player>(R.layout.fragment_item_game_score,
            sharedViewModel.listOfPlayers){

            override fun binder(containerView: View, item: Player, position: Int) {
                super.binder(containerView, item, position)

                containerView.apply {
                    item_player_name.text = item.name
                    item_score.text = item.sumPointsFromListPair().toString()
                }

            }

        }
        recView.adapter = scoreAdapter
    }

    private fun nextRound() {
        gamingViewModel.currentRound.postValue(gamingViewModel.currentRound.value?.plus(1))
        gamingViewModel.currentTurn.postValue(gamingViewModel.currentTurn.value?.plus(1))
        logScores()
    }

    private fun logScores() {
        for (p in sharedViewModel.listOfPlayers) {
            Log.d("!", "${p.name} ${p.listOfRoundAndPoints}")
        }
    }

    private fun isFinalRound(): Boolean = (currRound == maxRounds) && (currPlayer.playerNum == 1)

    private fun displayFinalRoundStarted(){
        Log.d("!", "Final Round")
    }

    private fun nextPlayerTurn(){

        val listOfViews = mutableListOf<View>(btnFail, btnSuccess, tvPlayerName)

        val newCard = randomizeCard() /* Generate new random card */
//        var nextPlayerName = (currTurn % pCount.plus(1)).toString()

        val strBuilder = StringBuilder().apply {
            append(currentCardType)
            appendLine()
            appendLine()
            append(newCard.first)
            appendLine()
            appendLine()
            append(newCard.second)
        }

        flipCard(requireContext(), tvCard, textToSet = strBuilder.toString(), listOfViews).start()

       // tvCard.apply { text = strBuilder } /* Update card-info */
        btnSuccess.apply { text = "SUCCESS" }
       // Log.d("!", "CURR PLAYER = ${calcPlayerTurn()}")
        currPlayer = sharedViewModel.listOfPlayers[calcPlayerTurn()] /* Return(Current Turn - 1) because of ListIndex starts from 0 = 1, 1 = 2 etc.   */
        tvPlayerName.text = currPlayer.name

    }

    private fun calcPlayerTurn(): Int =  (currTurn % (pCount.plus(1))) -1

    private fun randomizeCard(): Pair<String, String> = when (val ran = arrayOfEnRandoms.random()) {
        EnRandom.CONSEQUENCES -> {

            val r = (0 until listOfConsequences.count()).filter { it % 2 == 0 }.random()

            val r1 = listOfConsequences[r]
            pointsToAdd = listOfConsequencesPoints[r].toDouble()
            val r2 = listOfConsequences[r + 1]
            pointsToAdd2 = listOfConsequencesPoints[r+ 1].toDouble()

            currentCardType = ran.getEnumString()
            // val str = ("${listOfConsequences[r]} Con1: $pointsToAdd ---- $r2 Con2: $pointsToAdd2")
            // Log.d("!", "${listOfConsequences[r]} Con1: $pointsToAdd ---- $r2 Con2: $pointsToAdd2")
            Pair("1. $r1 ($pointsToAdd)", "2. $r2 ($pointsToAdd2)")

        }
        EnRandom.MISSION -> {

            val r = (0 until listOfMissions.count()).random()
            pointsToAdd = listOfMissionsPoints[r].toDouble()
            currentCardType = ran.getEnumString()
            Pair("${listOfMissions[r]} ($pointsToAdd)", "")
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_Success -> { doNext(EnOperation.SUCCESS) }
            R.id.button_Fail -> { doNext(EnOperation.FAIL) }
        }
    }

    private fun doNext(operation: EnOperation) {
        updatePlayerPoints(operation)
        gamingViewModel.currentTurn.postValue(gamingViewModel.currentTurn.value?.plus(1))
    }

    private fun updatePlayerPoints(operation: EnOperation) {
        currPlayer.listAddRoundAndPoints(
            when (operation) {
                EnOperation.SUCCESS -> Pair(currRound, pointsToAdd)
                EnOperation.FAIL -> Pair(currRound, 0.0)
            }
        )
    }


}