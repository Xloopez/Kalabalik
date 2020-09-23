package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.EnumUtil.*
import com.example.myapplication.EnumUtil.EnOperation.*
import com.example.myapplication.Util.newFragmentInstance
import com.example.myapplication.Util.viewApplyVis
import com.example.myapplication.Util.viewApplyVisFromList
import com.example.myapplication.databinding.FragmentGamingBinding

class GamingFragment : Fragment(), View.OnClickListener {

    lateinit var sharedViewModel: SharedViewModel
    lateinit var gamingViewModel: GamingViewModel

    private var _binding: FragmentGamingBinding? = null
    private val binding get() = _binding!!

    private lateinit var tvPlayerName: AppCompatTextView
    private lateinit var tvCurrRound: AppCompatTextView
    private lateinit var tvTotalRounds: AppCompatTextView

    private lateinit var btnSuccess: AppCompatButton
    private lateinit var btnFail: AppCompatButton

    private val arrayOfEnRandoms = EnRandom.values()
    private val listOfConsequences by lazy { resources.getStringArray(R.array.Consequences) }
    private val listOfMissions by lazy { resources.getStringArray(R.array.Mission) }
    private val listOfConsequencesPoints by lazy { resources.getIntArray(R.array.ConsequencesPoints) }
    private val listOfMissionsPoints by lazy { resources.getIntArray(R.array.MissionPoints) }

    private lateinit var frameLayout: FrameLayout

    private lateinit var listOfViews: MutableList<View>

    private var currRound = 0
    private var currTurn = 0
    private var maxRounds = 0
    private var pCount = 0
    private var totalTurns = 0

    private lateinit var currPlayer: Player

    //TODO Button Cancel before done?
    //TODO Override backbutton?

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sharedViewModel =  ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        gamingViewModel =  ViewModelProvider(requireActivity()).get(GamingViewModel::class.java) // SCOPE TO ACTIVITY? MAYBE..
        _binding = FragmentGamingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyViewBinding()
        viewApplyVis(frameLayout, View.INVISIBLE)
        newFragmentInstance(fragmentManager = childFragmentManager, CardFragment(), R.id.frame_layout_card_points, "CARD", false)

        btnSuccess.setOnClickListener(this)
        btnFail.setOnClickListener(this)

        pCount =  sharedViewModel.playerCount.value!!
        maxRounds = sharedViewModel.amountOfRounds.value!!
        totalTurns = maxRounds.times(pCount).plus(maxRounds)
        tvTotalRounds.apply { text = "out of $maxRounds" }
        gamingViewModel.currentTurn.postValue(1)
        updateRound()

        gamingViewModel.currentPlayer.observe(this, {
            currPlayer = it
            tvPlayerName.text = it.name
        })

        gamingViewModel.currentTurn.observe(this, {
            currTurn = it

            when (it == totalTurns) {
                true -> {
                    endGame()
                }
                false -> {
                    when (currTurn % pCount.plus(1)) {
                        0 -> {
                            nextRound()
                            when (currTurn != 0) {
                                true -> {
                                    frameLayout.background = null
                                    newFragmentInstance(fragmentManager = childFragmentManager, GameScoreFragment(miniScore = true), R.id.frame_layout_card_points, "SCORE", replace = true)
                                }
                            }
                        }
                        else -> {
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
            tvCurrRound = textViewCurrentRound
            tvTotalRounds = textViewAmountOfRounds

            /* BUTTONS */
            btnSuccess = buttonSuccess
            btnFail = buttonFail

            /* OTHER */
            frameLayout = frameLayoutCardPoints
        }

       listOfViews = mutableListOf(btnFail, btnSuccess, tvPlayerName)
    }


    private fun endGame() {

        sharedViewModel.currentFragmentPos.postValue(sharedViewModel.currentFragmentPos.value?.plus(1))
        tvPlayerName.apply { text = "GAME ENDED - TAKE ME TO SCORE" }

        mutableListOf(
            viewApplyVis(btnSuccess, View.INVISIBLE),
            viewApplyVis(btnFail, View.INVISIBLE))
            .run {
                viewApplyVisFromList(this)
            }
    }

    private fun nextRound() {

        btnSuccess.apply {
            text = context.getString(R.string.next_round)
            setOnClickListener {
                newFragmentInstance(fragmentManager = childFragmentManager, CardFragment(), R.id.frame_layout_card_points, "CARD", replace = true)
                updateRound()
                updateTurn()
            }
        }

        tvPlayerName.apply { text = context.getString(R.string.current_points) }

        mutableListOf(
            //viewApplyVis(frameLayout, View.INVISIBLE),
            viewApplyVis(btnFail, View.INVISIBLE))
            .run {
                viewApplyVisFromList(this)
            }

    }

    private fun isFinalRound(): Boolean = (currRound == maxRounds) && (currPlayer.playerNum == 1)

    private fun displayFinalRoundStarted(){
        //TODO DISPLAY FINAL ROUND ANIMATION !!
        Log.d("!", "Final Round")
    }

    private fun nextPlayerTurn(){

        btnSuccess.setOnClickListener(this)

        mutableListOf(
            viewApplyVis(btnFail))
            .run {
                viewApplyVisFromList(this)
            }

        when (val ran = arrayOfEnRandoms.random()) {
            EnRandom.CONSEQUENCES -> {

                val randomEvenIndex = (0 until listOfConsequences.count()).filter { it % 2 == 0 }.random()
                val randomEven = listOfConsequences[randomEvenIndex];
                val pointsEven = listOfConsequencesPoints[randomEvenIndex].toDouble()
//                val randomOdd = listOfConsequences[randomEvenIndex + 1]
//                val pointsOdd = listOfConsequencesPoints[randomEvenIndex+ 1].toDouble()

//                 Log.d("!", "$randomOdd Con1: $pointsEven ---- $randomEven Con2: $pointsOdd")
                gamingViewModel.apply {
                    consequencePair.postValue(Pair(randomEven, pointsEven))
                    currentCardType.postValue(ran)
                }
//               Log.d("!", "${ran.getEnumString()} // Con1: $randomEven $pointsEven ---- $randomOdd Con2: $pointsOdd")
            }
            EnRandom.MISSION -> {

                val randomIndex = (0 until listOfMissions.count()).random()
                val randomMission = listOfMissions[randomIndex]
                val randomPoints = listOfMissionsPoints[randomIndex].toDouble()

                gamingViewModel.apply {
                    consequencePair.postValue(Pair(randomMission, randomPoints))
                    missionPair.postValue(Pair(randomMission, randomPoints))
                    currentCardType.postValue(ran)
                }

                Log.d("!", "${ran.getEnumString()} // $randomMission $randomPoints")

            }
        }

       //val listOfViews = mutableListOf<View>(btnFail, btnSuccess, tvPlayerName)
       Animationz.flipCardFragment(context = requireContext(), view = frameLayout, viewsList = listOfViews, gamingViewModel).start()
       updateCurrPlayer()
       btnSuccess.apply { text = "SUCCESS" }
    }


    private fun updateTurn() = gamingViewModel.currentTurn.postValue(gamingViewModel.currentTurn.value?.plus(1))

    private fun updateCurrPlayer() = gamingViewModel.currentPlayer.postValue(sharedViewModel.listOfPlayers[calcPlayerTurn()])

    private fun calcPlayerTurn(): Int =  (currTurn % (pCount.plus(1))) -1

    private fun updateRound()= gamingViewModel.currentRound.postValue(gamingViewModel.currentRound.value?.plus(1))


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_Success -> {
                updatePlayerPoints(SUCCESS)
            }
            R.id.button_Fail -> {
                updatePlayerPoints(FAIL)
            }
        }
    }

    //TODO SEND SELECTED POINTS BACK TO ACTIVITY IF CONS SUCCESS..
    //TODO MISSION JUST APPLY..
    private fun updatePlayerPoints(operation: EnOperation) {

        currPlayer.listAddRoundAndPoints(when(operation) {
            SUCCESS -> {
                when (gamingViewModel.currentCardType.value!!) {
                    EnRandom.CONSEQUENCES -> {
                        Pair(currRound, gamingViewModel.consequencePair.value!!.second)
                    }
                    EnRandom.MISSION -> {
                        Pair(currRound, gamingViewModel.missionPair.value!!.second)
                    }
                }
            }
            FAIL -> {
                Pair(currRound, -5.0)
            }
        })

        updateTurn()
    }

}
