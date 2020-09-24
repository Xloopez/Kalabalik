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
import com.example.myapplication.Animationz.flipNewRound
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

        sharedViewModel.apply {
            pCount = playerCount.value!!
            maxRounds = amountOfRounds.value!!
        }

        totalTurns = calcTotalTurn()
        tvTotalRounds.apply { text = "out of $maxRounds rounds" }
        gamingViewModel.apply {
            currentTurn.postValue(1)
        }
        updateRound()

        setUpCurrentPlayerObserver()
        setUpCurrentTurnObserver()
        setUpCurrentRoundObserver()
    }

    private fun setUpCurrentRoundObserver() {
        gamingViewModel.currentRound.observe(this, { newRound ->
            currRound = newRound
            //TODO ADD VALUEANIMATOR?? TRANSFORM TEXT (newRound) FROM TO
            tvCurrRound.flipNewRound(requireContext(), newRound.toString()).start()
            if (isFinalRound()) {
                displayFinalRoundStarted() //TODO IMPLEMENT?
            }

        })
    }

    private fun setUpCurrentTurnObserver() {
        gamingViewModel.currentTurn.observe(this, {
            currTurn = it

            when ((it == totalTurns)) {
                true -> endGame()
                false -> {

                    when (calcCurrentTurn()) {
                        0 -> {
                            nextRound()
                            when {
                                it != 0 -> displayScoreFragment()
                            }
                        }
                        else -> nextPlayerTurn()
                    }
                }
            }

        })
    }

    private fun displayScoreFragment() {
        frameLayout.background = null
        newFragmentInstance(
            fragmentManager = childFragmentManager,
            fragment = GameScoreFragment(miniScore = true),
            layoutId = R.id.frame_layout_card_points,
            tag = "SCORE",
            replace = true
        )
    }

    private fun setUpCurrentPlayerObserver() {
        gamingViewModel.currentPlayer.observe(this, {
            currPlayer = it
            tvPlayerName.slideOutRightInLeftSetText(it.name).start()
        })
    }

    private fun calcTotalTurn() = maxRounds.times(pCount).plus(maxRounds)

    private fun applyViewBinding(){

        binding.apply {

            tvPlayerName = textViewPlayerName
            tvCurrRound = textViewCurrentRoundNum
            tvTotalRounds = textViewAmountOfRounds

            btnSuccess = buttonSuccess
            btnFail = buttonFail

            frameLayout = frameLayoutCardPoints
        }

       listOfViews = mutableListOf(btnFail, btnSuccess)
    }


    private fun endGame() {

        sharedViewModel.sorted()
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
            text = getString(R.string.next_round)
            setOnClickListener {
                newFragmentInstance(fragmentManager = childFragmentManager, CardFragment(), R.id.frame_layout_card_points, "CARD", replace = true)
                updateRound()
                updateTurn()
            }
        }

        tvPlayerName.slideOutRightInLeftSetText(getString(R.string.current_points)).start()
        viewApplyVis(btnFail, View.INVISIBLE)

    }

    private fun isFinalRound(): Boolean = (currRound == maxRounds) && (currPlayer.playerNum == 1)

    private fun displayFinalRoundStarted(){
        //TODO DISPLAY FINAL ROUND ANIMATION !!
        Log.d("!", "Final Round")
    }

    private fun nextPlayerTurn(){

        btnSuccess.setOnClickListener(this)

        val pair: Pair<String, Double>
        val cardType: EnRandom

        mutableListOf(viewApplyVis(btnFail)).run {
                viewApplyVisFromList(this) }

        val rIndex: Int

        when (val rCardType = arrayOfEnRandoms.random()) {
            EnRandom.CONSEQUENCES -> {

                rIndex =  getRandomListIndex(listOfConsequences)

                    pair =
                        returnListPair(index = rIndex,
                        strArr =  listOfConsequences,
                        intArr =  listOfConsequencesPoints)
                    cardType = rCardType

            }
            EnRandom.MISSION -> {

                rIndex = getRandomListIndex(listOfMissions)

                    pair = returnListPair(index = rIndex,
                        strArr = listOfMissions,
                        intArr = listOfMissionsPoints)
                    cardType = rCardType
            }
        }


//     Log.d("!", "${ran.getEnumString()} // Con1: $randomEven $pointsEven ---- $randomOdd Con2: $pointsOdd")
       //val listOfViews = mutableListOf<View>(btnFail, btnSuccess, tvPlayerName)
       gamingViewModel.updateCardTypeAndPair(pair, cardType)
       Animationz.flipCardFragment(context = requireContext(), view = frameLayout, viewsList = listOfViews, gamingViewModel).start()
       updateCurrPlayer()
       buttonChangeText(btnSuccess,"SUCCESS")
    }

    private fun returnListPair(index: Int, strArr: Array<String>, intArr: IntArray): Pair<String, Double> = Pair(strArr[index], intArr[index].toDouble())

    private fun getRandomListIndex(list: Array<String>) = (0 until list.count()).random()

    private fun updateTurn() = gamingViewModel.currentTurn.postValue(gamingViewModel.currentTurn.value?.plus(1))

    private fun updateCurrPlayer() = gamingViewModel.currentPlayer.postValue(sharedViewModel.listOfPlayers[calcPlayerTurn()])

    private fun calcPlayerTurn(): Int =  calcCurrentTurn().minus(1)

    private fun calcCurrentTurn() = currTurn % pCount.plus(1)

    private fun updateRound()= gamingViewModel.currentRound.postValue(gamingViewModel.currentRound.value?.plus(1))

    private fun buttonChangeText(view: AppCompatButton, text: String) = view.apply{ view.text = text}

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

        val pair: Pair<Int, Double> = when(operation){
            SUCCESS -> {
                when (gamingViewModel.currentCardType.value!!) {
                    EnRandom.CONSEQUENCES -> {
                        pairRoundWithPoints(double =  gamingViewModel.consequencePair.value!!.second)
                    }
                    EnRandom.MISSION -> {
                        pairRoundWithPoints(double = gamingViewModel.missionPair.value!!.second)
                    }
                }
            }
            FAIL -> {
                pairRoundWithPoints(double = -5.0)
            }
        }
        currPlayer.listAddRoundAndPoints(pair = pair)
        updateTurn()
    }

    private fun pairRoundWithPoints(double: Double): Pair<Int, Double>{
        return Pair(currRound, double)
    }
}
