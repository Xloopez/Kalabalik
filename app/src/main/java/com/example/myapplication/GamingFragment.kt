package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentGamingBinding
import kotlinx.android.synthetic.main.fragment_item_game_score.view.*
import java.time.temporal.ValueRange

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

    private lateinit var rgPlayers: RadioGroup
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
    private var currentCardType: String = ""
    private var totalTurns = 0

    private lateinit var currPlayer: Player

    private enum class EnOperation { SUCCESS, FAIL; }
    private enum class EnRandom { CONSEQUENCES, MISSION;

        fun getEnumString(): String = when(this) {
            CONSEQUENCES -> "Konsekvens"
            MISSION -> "Uppdrag"
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

                    tvPlayerName.text = "GAME ENDED - TAKE ME TO SCORE"
                    btnSuccess.visibility = View.INVISIBLE
                    btnFail.visibility = View.INVISIBLE
                    tvCard.visibility = View.INVISIBLE
                    rgPlayers.visibility = View.INVISIBLE
                    logScores()

                }
                false -> {
                    when (v) {
                        0 -> {
                            //TODO SHOW CURRENT TOTALPOINTS?3
                            btnSuccess.text = "NEXT ROUND"
                            btnSuccess.setOnClickListener {
                                nextRound()
                            }
                            tvPlayerName.text = "CURRENT POINTS"
                            scoreAdapter!!.notifyDataSetChanged()

                            Util.apply {
                                viewApplyVis(btnFail, View.INVISIBLE)
                                viewApplyVis(tvCard, View.GONE)
                                viewApplyVis(recView, View.VISIBLE)
                            }

                        }
                        else -> {
                            btnSuccess.setOnClickListener(this)
                            Util.apply {
                                viewApplyVis(btnFail, View.VISIBLE)
                                viewApplyVis(tvCard, View.VISIBLE)
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
            tvCurrRound.apply { text = "ROUND $it" }
            if (isFinalRound()) {
                displayFinalRoundStarted()
            }
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

            /* RADIOGROUPS */
            rgPlayers = radioGroupPlayers

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

        val newCard = randomizeCard() /* Generate new random card */
//        var nextPlayerName = (currTurn % pCount.plus(1)).toString()

        Animationz.slideOutRightSlideInLeft(tvCard)

        val strBuilder = StringBuilder().apply {
            append(currentCardType)
            appendLine()
            append(newCard)
        }

        tvCard.apply { text = strBuilder } /* Update card-info */
        btnSuccess.apply { text = "SUCCESS" }
        Log.d("!", "CURR PLAYER = ${calcPlayerTurn()}")
        currPlayer = sharedViewModel.listOfPlayers[calcPlayerTurn()] /* Return(Current Turn - 1) because of ListIndex starts from 0 = 1, 1 = 2 etc.   */
        tvPlayerName.text = currPlayer.name

    }

   private fun calcPlayerTurn(): Int =  (currTurn % (pCount.plus(1))) -1

    private fun randomizeCard() = when (val ran = arrayOfEnRandoms.random()) {
        EnRandom.CONSEQUENCES -> {

            val r = (0 until listOfConsequences.count()).random()
            pointsToAdd = listOfConsequencesPoints[r].toDouble()
            currentCardType = ran.getEnumString()
            //Log.d("!", "$r Con: $pointsToAdd")
            listOfConsequences[r]

        }
        EnRandom.MISSION -> {

            val r = (0 until listOfMissions.count()).random()
            pointsToAdd = listOfMissionsPoints[r].toDouble()
            currentCardType = ran.getEnumString()
            //Log.d("!", "$r Miss: $pointsToAdd")
            listOfMissions[r]

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
                EnOperation.FAIL -> Pair(currRound, -1.0)
            }
        )
    }


}