package com.example.myapplication

import android.animation.AnimatorSet
import android.graphics.Color
import android.os.Bundle
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

	private lateinit var sharedViewModel: SharedViewModel
	private lateinit var gamingViewModel: GamingViewModel

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

	lateinit var fisCard: FragmentInputSettings
	lateinit var fisScore: FragmentInputSettings

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
		gamingViewModel = ViewModelProvider(requireActivity()).get(GamingViewModel::class.java)
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
		fisCard.newFragmentInstance().commit()
		setUpTextSwitcherRoundNum()

		btnSuccess.setOnClickListener(this)
		btnFail.setOnClickListener(this)

		setInitialValues()
		gamingViewModel.updateRound()

		setUpCurrentPlayerObserver()
		setUpCurrentTurnObserver()
		setUpCurrentRoundObserver()

	}

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

	private fun applyViewBinding() {

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

	private fun setUpTextSwitcherRoundNum() {
		tSwitcherRounds.setFactory {
			val textView = TextView(requireContext()).apply {
				typeface = ResourcesCompat.getFont(requireContext(), R.font.irish_grover)
				textSize = 40f
				setTextColor(Color.WHITE)
				gravity = Gravity.START
				text = "0"
			}
			textView
		}
		tSwitcherRounds.inAnimation = AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_in_left)
		tSwitcherRounds.outAnimation = AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_out_right)
	}

	private fun setFragmentInputs() {
		fisCard = object : FragmentInputSettings(
			fragmentManager = this.childFragmentManager, fragment = CardFragment(),
			layoutId = frameLayout.id, tag = "CARD", replace = false, animate = false,
		){}
		fisScore = object : FragmentInputSettings(
			fragmentManager = this.childFragmentManager, fragment = GameScoreFragment(miniScore = true),
			layoutId = frameLayout.id, tag = "CURRENT_SCORE", replace = true, animate = true,){}
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
		})
	}

	private fun setUpCurrentTurnObserver() {
		gamingViewModel.currentTurn.observe(this, {
			currTurn = it
			
			val showScore = calcCurrentTurn().isZero()
			val showScoreNot = currTurn.isZero().not()
			val showNextFragment = currTurn.isEqualTo(totalTurns)
			
			runUnitIfTrue(::endGame, b1 = showNextFragment)
			runUnitIfTrueElse(::nextRound, ::nextPlayerTurn, b1 = showScore)
			runUnitIfTrue(::displayScoreFragment, b1 = showScore, b2 = showScoreNot)
		})
	}

	private fun setUpCurrentPlayerObserver() {
		gamingViewModel.currentPlayer.observe(this, {
			currPlayer = it
			tvPlayerName.slideOutRightInLeftSetText(it.name).start()
		})
	}

	private fun displayScoreFragment() {
		frameLayout.background = null
		fisScore.newFragmentInstance().commit()
	}

	private fun endGame() {

		//TODO "TAKE ME TO SCORE-BUTTON" AND/OR ANIMATE "NICER" TO GameScoreFragment()
		sharedViewModel.apply {
			sorted()
			updateFragmentPos()
		}
		mutableListOf(
			viewApplyVis(btnSuccess, View.INVISIBLE),
			viewApplyVis(btnFail, View.INVISIBLE)
		)
			.run {
				viewApplyVisFromList(this)
			}
	}

	private fun nextRound() {

		btnSuccess.apply {
			text = getString(R.string.next_round)
			setOnClickListener {
				fisCard.apply { replace = true }.newFragmentInstance().commit()
				gamingViewModel.apply {
					updateRound()
					updateTurn()
				}
			}
		}
		tvPlayerName.slideOutRightInLeftSetText(sText = getString(R.string.current_points)).start()
		viewApplyVis(btnFail, View.INVISIBLE)

	}

	private fun nextPlayerTurn() {

		btnSuccess.setOnClickListener(this)

		mutableListOf(viewApplyVis(btnFail)).run {
			viewApplyVisFromList(this)
		}

		val (pair: Pair<String, Double>, cardType: EnRandom) = generateNewPair()

		gamingViewModel.apply {
			clearCard()
			updateCardTypeAndPair(pair, cardType)
		}

		frameLayout.flip(listOfViews = listOfViews).start()
		gamingViewModel.updatePlayer(getCurrPlayerObj())
		btnSuccess.buttonChangeText("SUCCESS")
	}

	private fun generateNewPair(): Pair<Pair<String, Double>, EnRandom> {

		val rIndex: Int
		val pair: Pair<String, Double>
		val cardType: EnRandom

		when (val rCardType = arrayOfEnRandoms.random()) {
			EnRandom.CONSEQUENCES -> {

				rIndex = listOfConsequences.getRandomListIndex()
				pair = returnListPair(
					index = rIndex,
					strArr = listOfConsequences,
					intArr = listOfConsequencesPoints
				)
				cardType = rCardType

			}
			EnRandom.MISSION -> {

				rIndex = listOfMissions.getRandomListIndex()
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

		if (calcCurrentTurn().isEqualTo(1)) {
			v.setBackgroundColor(getColor(requireActivity(), R.color.deep_purple_400))
		}
		val listOfButtons = listOfViews.listFilterInstance<AppCompatButton>()

		val a1 = v.flipToBackY().apply {
			duration = Animationz.flipCardDurationOneFourth
			interpolator = LinearInterpolator()

			doOnStart {
				listOfButtons.clickable(false)
			}
			v.animate().scaleXBy(-0.5f).scaleYBy(-0.5f)

			doOnEnd {
				v.background =
					getDrawable(requireContext(), R.drawable.card_background_with_strokes)
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
						animate().scaleXBy(0.5f).scaleYBy(0.5f)
						setBackgroundColor(
							getColor(requireActivity(), R.color.deep_purple_400)
						) //getDrawable(requireContext(), R.drawable.card_background_front)
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

	private fun runUnitIfTrue(unit: () -> Unit, b1: Boolean, b2: Boolean = true) { if (b1 and b2) { unit() } }
	private fun runUnitIfTrueElse(unit: () -> Unit, unitElse: () -> Unit, b1: Boolean)= if (b1) { unit() } else { unitElse() }
	private fun getCurrPlayerObj() = sharedViewModel.listOfPlayers[calcPlayerTurn()]
	private fun calcTotalTurn() = maxRounds.times(pCount).plus(maxRounds)
	private fun calcPlayerTurn(): Int = calcCurrentTurn().minus(1)
	private fun calcCurrentTurn(): Int = currTurn % pCount.plus(1)
	private fun Array<String>.getRandomListIndex() = (0 until this.count()).random()
	private fun Int.isZero() = (this == 0)
	private fun Int.isEqualTo(value: Int) = (this == value)
	private fun pairRoundWithPoints(double: Double): Pair<Int, Double> = Pair(currRound, double)
	private fun AppCompatButton.buttonChangeText(text: String) = apply { this@buttonChangeText.text = text }
	private inline fun <reified T> MutableList<View>.listFilterInstance() = this.filterIsInstance<T>() //TODO move to Util?
	private fun returnListPair(index: Int, strArr: Array<String>, intArr: IntArray): Pair<String, Double> = Pair(strArr[index], intArr[index].toDouble())
	private fun List<AppCompatButton>.clickable(clickable: Boolean) { this.forEach { it.apply { isClickable = clickable } } }

	private fun updatePlayerPoints(operation: EnOperation) {

		val pair: Pair<Int, Double> = when (operation) {
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
		gamingViewModel.updateTurn()
	}

	private fun FragmentInputSettings.newFragmentInstance(): FragmentTransaction {

		val f = this

		return f.fragmentManager.beginTransaction().apply {

			f.let {
				when (animate) {
					true -> {
						setCustomAnimations(
							R.anim.fragment_slide_right_enter,
							R.anim.fragment_slide_left_exit
						)
					}
				}

				when (f.replace) {
					true -> { replace(layoutId, fragment, tag)
					}
					false -> { add(layoutId, fragment, tag)
					}
				}
			}
		}
	}
}
