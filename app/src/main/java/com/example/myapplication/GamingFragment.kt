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
import com.example.myapplication.Util.viewApplyVis
import com.example.myapplication.Util.viewApplyVisFromList
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


    private var currentCardType: String = ""
    private var currRound = 0
    private var currTurn = 0
    private var maxRounds = 0
    private var pCount = 0
    private var pointsToAdd2: Double = 0.0
    private var pointsToAdd: Double = 0.0
    private var totalTurns = 0

    private lateinit var currPlayer: Player

    private enum class EnOperation { SUCCESS, FAIL; }
    private enum class EnRandom { CONSEQUENCES, MISSION;

        fun getEnumString(): String = when(this) {
            CONSEQUENCES -> "CONSEQUENCE"
            MISSION -> "MISSION"
        }
    }

    //TODO Button Cancel before done?
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

                    //TODO POST FRAGMENTPOS VAL + FRAGMENT POS VAL!
                    tvPlayerName.apply { text = "GAME ENDED - TAKE ME TO SCORE" }

                    mutableListOf(
                        viewApplyVis(btnSuccess, View.INVISIBLE),
                        viewApplyVis(btnFail, View.INVISIBLE),
                        viewApplyVis(tvCard, View.INVISIBLE))
                        .run {
                            viewApplyVisFromList(this)
                        }

                }
                false -> {
                    when (v) {
                        0 -> {

                            btnSuccess.apply {
                                text  = "NEXT ROUND"
                                setOnClickListener {
                                    nextRound()
                                }
                            }
                            tvPlayerName.apply { text = "CURRENT POINTS" }
                            scoreAdapter!!.notifyDataSetChanged()

                            mutableListOf(
                                viewApplyVis(btnFail, View.INVISIBLE),
                                viewApplyVis(tvCard, View.GONE),
                                viewApplyVis(recView))
                                .run {
                                    viewApplyVisFromList(this)
                                }
                        }
                        else -> {
                            btnSuccess.setOnClickListener(this)
                            mutableListOf(
                                viewApplyVis(btnFail),
                                viewApplyVis(recView, View.GONE))
                                .run {
                                    viewApplyVisFromList(this)
                                }
                            nextPlayerTurn()
                        }
                    }
                }
            }

        })

        gamingViewModel.currentRound.observe(this, {
            currRound = it
            //TODO SPLIT TEXTVIEWS IN XML CHANGE ROUND ON 1 -> ANIMATE NEW ROUND?? OR ANIMATE WHOLE TEXT
            val rnd = "ROUND $it"
            tvCurrRound.apply { text = rnd }
            if (isFinalRound()) { displayFinalRoundStarted() }
        })
    }


    private fun applyViewBinding(){

        binding.apply {

            /* TEXT-VIEWS */
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
    }

    private fun isFinalRound(): Boolean = (currRound == maxRounds) && (currPlayer.playerNum == 1)

    private fun displayFinalRoundStarted(){
        //TODO DISPLAY FINAL ROUND ANIMATION !!
        Log.d("!", "Final Round")
    }

    private fun nextPlayerTurn(){

        val listOfViews = mutableListOf<View>(btnFail, btnSuccess, tvPlayerName)

        val newCard = randomizeCard() /* Generate new random card */

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

        btnSuccess.apply { text = "SUCCESS" }
        currPlayer = sharedViewModel.listOfPlayers[calcPlayerTurn()] /* Return(Current Turn - 1) because of ListIndex starts from 0 = 1, 1 = 2 etc.   */
        tvPlayerName.text = currPlayer.name

    }

    private fun doNext(operation: EnOperation) {
        updatePlayerPoints(operation)
        gamingViewModel.currentTurn.postValue(gamingViewModel.currentTurn.value?.plus(1))
    }

    private fun calcPlayerTurn(): Int =  (currTurn % (pCount.plus(1))) -1

    private fun randomizeCard(): Pair<String, String> = when (val ran = arrayOfEnRandoms.random()) {
        EnRandom.CONSEQUENCES -> {

            currentCardType = ran.getEnumString()
            val randomEvenIndex = (0 until listOfConsequences.count()).filter { it % 2 == 0 }.random()
            val randomEven = listOfConsequences[randomEvenIndex]; pointsToAdd = listOfConsequencesPoints[randomEvenIndex].toDouble()
            val randomOdd = listOfConsequences[randomEvenIndex + 1];pointsToAdd2 = listOfConsequencesPoints[randomEvenIndex+ 1].toDouble()

            // val str = ("${listOfConsequences[r]} Con1: $pointsToAdd ---- $r2 Con2: $pointsToAdd2")
            // Log.d("!", "${listOfConsequences[r]} Con1: $pointsToAdd ---- $r2 Con2: $pointsToAdd2")
            Pair("1. $randomEven ($pointsToAdd)", "2. $randomOdd ($pointsToAdd2)")

        }
        EnRandom.MISSION -> {

            currentCardType = ran.getEnumString()
            val randomIndex = (0 until listOfMissions.count()).random()
            pointsToAdd = listOfMissionsPoints[randomIndex].toDouble()

            Pair("${listOfMissions[randomIndex]} ($pointsToAdd)", "")
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_Success -> { doNext(EnOperation.SUCCESS) }
            R.id.button_Fail -> { doNext(EnOperation.FAIL) }
        }
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