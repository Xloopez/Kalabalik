package com.example.myapplication

import android.animation.AnimatorSet
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.Animationz.slideOutRightInLeftSetText
import com.example.myapplication.EnumUtil.EnOperation
import com.example.myapplication.EnumUtil.EnOperation.FAIL
import com.example.myapplication.EnumUtil.EnOperation.SUCCESS
import com.example.myapplication.EnumUtil.EnRandom
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
        tvTotalRounds.apply { text = "out of $maxRounds rounds" }
        gamingViewModel.currentTurn.postValue(1)
        updateRound()

        gamingViewModel.currentPlayer.observe(this, {
            currPlayer = it
            tvPlayerName.slideOutRightInLeftSetText(it.name).start()
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


            val a1 = Animationz.slideOutRight(tvCurrRound).apply {
                interpolator = AccelerateInterpolator()
            }
            val a2 = Animationz.slideInLeft(tvCurrRound).apply {
                interpolator = OvershootInterpolator()
            }

            a1.doOnEnd { _ ->
                tvCurrRound.apply { text = it.toString() }
            }
            AnimatorSet().apply { playSequentially(a1, a2) }.start()


            if (isFinalRound()) { displayFinalRoundStarted() }
        })
    }

    private fun applyViewBinding(){

        binding.apply {

            /* TEXT-VIEWS */
            tvPlayerName = textViewPlayerName
            tvCurrRound = textViewCurrentRoundNum
            tvTotalRounds = textViewAmountOfRounds

            /* BUTTONS */
            btnSuccess = buttonSuccess
            btnFail = buttonFail

            /* OTHER */
            frameLayout = frameLayoutCardPoints
        }

       listOfViews = mutableListOf(btnFail, btnSuccess)
    }


    private fun endGame() {

        sharedViewModel.currentFragmentPos.postValue(sharedViewModel.currentFragmentPos.value?.plus(1))
        //tvPlayerName.apply { text = "GAME ENDED - TAKE ME TO SCORE" }

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

        tvPlayerName.slideOutRightInLeftSetText(getString(R.string.current_points)).start()

        mutableListOf(
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

                val rIndex =  getRandomListIndex(listOfConsequences)
                val rConsequence = listOfConsequences[rIndex]
                val rPoints = listOfConsequencesPoints[rIndex].toDouble()

                gamingViewModel.apply {
                    consequencePair.postValue(Pair(rConsequence, rPoints))
                    currentCardType.postValue(ran)
                }
//               Log.d("!", "${ran.getEnumString()} // Con1: $randomEven $pointsEven ---- $randomOdd Con2: $pointsOdd")
            }
            EnRandom.MISSION -> {

                val rIndex = getRandomListIndex(listOfMissions)
                val rMission = listOfMissions[rIndex]
                val rPoints = listOfMissionsPoints[rIndex].toDouble()

                gamingViewModel.apply {
                    missionPair.postValue(Pair(rMission, rPoints))
                    currentCardType.postValue(ran)
                }
            }
        }

       //val listOfViews = mutableListOf<View>(btnFail, btnSuccess, tvPlayerName)
       Animationz.flipCardFragment(context = requireContext(), view = frameLayout, viewsList = listOfViews, gamingViewModel).start()
       updateCurrPlayer()
       buttonChangeText(btnSuccess, "SUCCESS")
    }

    private fun getRandomListIndex(list: Array<String>) = (0 until list.count()).random()

    private fun updateTurn() = gamingViewModel.currentTurn.postValue(gamingViewModel.currentTurn.value?.plus(1))

    private fun updateCurrPlayer() = gamingViewModel.currentPlayer.postValue(sharedViewModel.listOfPlayers[calcPlayerTurn()])

    private fun calcPlayerTurn(): Int =  (currTurn % (pCount.plus(1))) -1

    private fun updateRound()= gamingViewModel.currentRound.postValue(gamingViewModel.currentRound.value?.plus(1))

    private fun buttonChangeText(view: AppCompatButton, text: String) = view.apply{ view.text = text}
    //                val randomEvenIndex = (0 until listOfConsequences.count()).filter { it % 2 == 0 }.random()

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
