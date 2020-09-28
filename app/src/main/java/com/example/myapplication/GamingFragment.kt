package com.example.myapplication

import android.animation.AnimatorSet
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.TextSwitcher
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.Animationz.checkCameraDistance
import com.example.myapplication.Animationz.flipToBackY
import com.example.myapplication.Animationz.flipToFrontY
import com.example.myapplication.Animationz.slideOutRightInLeftSetText
import com.example.myapplication.EnumUtil.EnOperation
import com.example.myapplication.EnumUtil.EnOperation.FAIL
import com.example.myapplication.EnumUtil.EnOperation.SUCCESS
import com.example.myapplication.EnumUtil.EnRandom
import com.example.myapplication.Util.FragmentInputSettings
import com.example.myapplication.Util.viewApplyVis
import com.example.myapplication.Util.viewApplyVisFromList
import com.example.myapplication.databinding.FragmentGamingBinding

class GamingFragment : Fragment(), View.OnClickListener {

    lateinit var sharedViewModel: SharedViewModel
    lateinit var gamingViewModel: GamingViewModel

    private var _binding: FragmentGamingBinding? = null
    private val binding get() = _binding!!

    private lateinit var tvPlayerName: AppCompatTextView
    private lateinit var tvTotalRounds: AppCompatTextView

    private lateinit var tSwitcherRounds: TextSwitcher

    private lateinit var btnSuccess: AppCompatButton
    private lateinit var btnFail: AppCompatButton

    private val arrayOfEnRandoms = EnRandom.values()
    private lateinit var listOfViews: MutableList<View>

    private lateinit var listOfConsequences: Array<String>
    private lateinit var listOfMissions: Array<String>
    private lateinit var listOfConsequencesPoints: IntArray
    private lateinit var listOfMissionsPoints: IntArray

    private lateinit var frameLayout: FrameLayout

    private var currRound = 0
    private var currTurn = 0
    private var maxRounds = 0
    private var pCount = 0
    private var totalTurns = 0

    private lateinit var currPlayer: Player

    private lateinit var spUtil: SharedPrefUtil
    private var scale: Float = 0.0f

    //TODO Button Cancel before done?
    //TODO Override backbutton?
    lateinit var fisCard: FragmentInputSettings
    lateinit var fisScore: FragmentInputSettings


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sharedViewModel =  ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        gamingViewModel =  ViewModelProvider(requireActivity()).get(GamingViewModel::class.java) // SCOPE TO ACTIVITY? MAYBE..
        _binding = FragmentGamingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyViewBinding()
        spUtil = SharedPrefUtil(requireActivity())
        scale = spUtil.getFloat(R.string.displayMetrics)
        frameLayout.checkCameraDistance(targetScale = (scale * 8000)) //TODO MOVE TO TOP DEC ANIM

        setFragmentInputs()
        newFragmentInstance(fisCard).commit()
        setUpTextSwitcherRoundNum()

        btnSuccess.setOnClickListener(this)
        btnFail.setOnClickListener(this)

        setInitialValues()
        updateRound()

        setUpCurrentPlayerObserver()
        setUpCurrentTurnObserver()
        setUpCurrentRoundObserver()

    }

    private fun setUpTextSwitcherRoundNum() {
        tSwitcherRounds.setFactory {
            val textView = TextView(requireContext()).apply {
                // setTextAppearance(R.style.TextSwitcherStyle)
                typeface = ResourcesCompat.getFont(requireContext(), R.font.irish_grover)
                textSize = 40f
                setTextColor(Color.WHITE)
                gravity = Gravity.START
                text = "0"
            }
            textView
        }
        tSwitcherRounds.inAnimation =
            AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_in_left)
        tSwitcherRounds.outAnimation =
            AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_out_right)
    }

    private fun setFragmentInputs() {
        fisCard = object : FragmentInputSettings(
            fragmentManager = this.childFragmentManager, fragment = CardFragment(),
            layoutId = frameLayout.id, tag = "CARD", replace = false, animate = false,
        ) {}
        fisScore = object : FragmentInputSettings(
            fragmentManager = this.childFragmentManager, fragment = GameScoreFragment(miniScore = true),
            layoutId = frameLayout.id, tag = "CARD", replace = true, animate = true,
        ) {}
    }

    private fun setInitialValues() {
        sharedViewModel.apply {
            pCount = playerCount.value!!
            maxRounds = amountOfRounds.value!!

            resources.apply {
                listOfConsequences = getStringArray(R.array.Consequences)
                listOfMissions = getStringArray(R.array.Mission)
                listOfConsequencesPoints = getIntArray(R.array.ConsequencesPoints)
                listOfMissionsPoints = getIntArray(R.array.MissionPoints)
            }

        }

        totalTurns = calcTotalTurn()
        tvTotalRounds.apply { text = "out of $maxRounds rounds" }
        gamingViewModel.apply { currentTurn.postValue(1) }

    }

    private fun setUpCurrentRoundObserver() {
        gamingViewModel.currentRound.observe(this, { newRound ->
            currRound = newRound

            tSwitcherRounds.setText(newRound.toString())

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
        newFragmentInstance(fis = fisScore).commit()
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
            tvTotalRounds = textViewAmountOfRounds

            tSwitcherRounds = textSwitcherRoundNum

            btnSuccess = buttonSuccess
            btnFail = buttonFail

            frameLayout = frameLayoutCardPoints
        }

       listOfViews = mutableListOf(btnFail, btnSuccess)
    }


    private fun endGame() {

        //TODO "TAKE ME TO SCORE-BUTTON" AND/OR ANIMATE "NICER" TO GameScoreFragment()
        sharedViewModel.apply {
            sorted()
            currentFragmentPos.postValue(sharedViewModel.currentFragmentPos.value?.plus(1))
        }
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
                newFragmentInstance(fisCard.apply { replace = true }).commit()
                //gamingViewModel.clearCardFragment.postValue(44)
                updateRound()
                updateTurn()
            }
        }

        tvPlayerName.slideOutRightInLeftSetText(sText = getString(R.string.current_points)).start()
        viewApplyVis(btnFail, View.INVISIBLE)

    }

    private fun isFinalRound(): Boolean = (currRound == maxRounds) && (currPlayer.playerNum == 1)

    private fun displayFinalRoundStarted(){
        //TODO DISPLAY FINAL ROUND ANIMATION ?
        Log.d("!", "Final Round")
    }

    private fun nextPlayerTurn(){

        btnSuccess.setOnClickListener(this)

        mutableListOf(viewApplyVis(btnFail)).run {
                viewApplyVisFromList(this) }

        gamingViewModel.clearCardFragment.postValue(3)
        val (pair: Pair<String, Double>, cardType: EnRandom) = generateNewPair()
        gamingViewModel.updateCardTypeAndPair(pair, cardType)
        frameLayout.flip (listOfViews = listOfViews).start()
        updateCurrPlayer()
        buttonChangeText(btnSuccess,"SUCCESS")
    }

    private fun generateNewPair(): Pair<Pair<String, Double>, EnRandom> {

        val rIndex: Int
        val pair: Pair<String, Double>
        val cardType: EnRandom

        when (val rCardType = arrayOfEnRandoms.random()) {
            EnRandom.CONSEQUENCES -> {

                rIndex = getRandomListIndex(listOfConsequences)

                pair =
                    returnListPair(
                        index = rIndex,
                        strArr = listOfConsequences,
                        intArr = listOfConsequencesPoints
                    )
                cardType = rCardType

            }
            EnRandom.MISSION -> {

                rIndex = getRandomListIndex(listOfMissions)

                pair = returnListPair(
                    index = rIndex,
                    strArr = listOfMissions,
                    intArr = listOfMissionsPoints
                )
                cardType = rCardType
            }
        }
        return Pair(pair, cardType)
    }

    private fun FrameLayout.flip(listOfViews: MutableList<View>): AnimatorSet {

        val v = this

        if (calcCurrentTurn() == 1) {v.setBackgroundColor(getColor(requireActivity(), R.color.deep_purple_400))}
        val listOfButtons = listOfViews.listFilterInstance<AppCompatButton>()

        val a1 = v.flipToBackY().apply {
            duration = Animationz.flipCardDurationOneFourth
            interpolator = LinearInterpolator()

            doOnStart {
                listOfButtons.clickable(false)
            }
            v.animate().scaleXBy(-0.5f).scaleYBy(-0.5f)

            doOnEnd {
                v.background = getDrawable(requireContext(), R.drawable.card_background_with_strokes)
            }
        }

        val a2 = v.flipToFrontY()
            .apply {
                interpolator = LinearInterpolator()
                duration = Animationz.flipCardDurationOneFourth
            }
       

        val a3 = v.flipToBackY()
            .apply {
                interpolator = DecelerateInterpolator()
                duration = Animationz.flipCardDurationOneFourth
                doOnEnd {
                    gamingViewModel.updateCardFragment.postValue(1)
                    v.apply {
                        //v.removeView(tv)
                        animate().scaleXBy(0.5f).scaleYBy(0.5f)
                        setBackgroundColor(getColor(requireActivity(), R.color.deep_purple_400)) //getDrawable(requireContext(), R.drawable.card_background_front)
                    }
                }
            }

        val a4 = v.flipToFrontY()
            .apply {
                interpolator = DecelerateInterpolator()
                duration = Animationz.flipCardDurationOneFourth
                doOnEnd { listOfButtons.clickable(true) }
            }

         return AnimatorSet().apply {
            playSequentially(a1, a2, a3, a4)
        }

    }

    private fun List<AppCompatButton>.clickable(clickable: Boolean) {
        this.forEach { it.apply { isClickable = clickable } }
    }

    private inline fun <reified T> MutableList<View>.listFilterInstance() =
        this.filterIsInstance<T>()

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
                updateTurn() //MOVE TO UPDATE PLAYERPOINTS AGAIN?
            }
            R.id.button_Fail -> {
                updatePlayerPoints(FAIL)
                updateTurn() //MOVE TO UPDATE PLAYERPOINTS AGAIN?
            }
        }
    }

    private fun updatePlayerPoints(operation: EnOperation) {

        val pair: Pair<Int, Double> = when(operation){
            SUCCESS -> {
                when (gamingViewModel.currentCardType.value!!) {
                    EnRandom.CONSEQUENCES -> {
                        pairRoundWithPoints(double = gamingViewModel.consequencePair.value!!.second)
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
    }

    private fun pairRoundWithPoints(double: Double): Pair<Int, Double>{
        return Pair(currRound, double)
    }

    private fun newFragmentInstance(fis: FragmentInputSettings): FragmentTransaction {

        return fis.fragmentManager.beginTransaction().apply {

            when (fis.animate) {
                true -> {
                    setCustomAnimations(
                        R.anim.fragment_slide_right_enter,
                        R.anim.fragment_slide_left_exit
                    )
                }
                false -> { }
            }

            when (fis.replace) {
                true -> { replace(fis.layoutId, fis.fragment, tag) }
                false -> { add(fis.layoutId, fis.fragment, tag) }
            }

        }

    }

}
