package com.example.myapplication

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
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
import androidx.core.view.marginRight
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentGamingBinding
import com.example.myapplication.dataclasses.Player
import com.example.myapplication.utilities.*
import com.example.myapplication.utilities.Animationz.checkCameraDistance
import com.example.myapplication.utilities.Animationz.flipToBackY
import com.example.myapplication.utilities.Animationz.flipToFrontY
import com.example.myapplication.utilities.Animationz.slideOutRightInLeftSetText
import com.example.myapplication.utilities.EnumUtil.EnOperation
import com.example.myapplication.utilities.EnumUtil.EnOperation.FAIL
import com.example.myapplication.utilities.EnumUtil.EnOperation.SUCCESS
import com.example.myapplication.utilities.EnumUtil.EnRandom
import com.example.myapplication.viewmodels.GamingViewModel
import com.example.myapplication.viewmodels.SharedViewModel

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

	private val tt = TimedTasks()

	private var currRound = 0
	private var currTurn = 0
	private var maxRounds = 0
	private var plyCount = 0
	private var totalTurns = 0

	private lateinit var currPlayer: Player

	private lateinit var spUtil: SharedPrefUtil
	private var scale: Float = 0.0f

	private lateinit var fisCard: FragmentInputSettings
	private lateinit var fisScore: FragmentInputSettings
	private lateinit var fisBlank: FragmentInputSettings

	private inline val isPrepareNextRound get() = calcCurrentTurn.isZero()
	private inline val isStart get() = 	(currTurn == 0) and (calcCurrentTurn == 0)
	private inline val isNextFragment get() = currTurn.isEqualTo(totalTurns)
	private inline val getCurrPlayerObj get() = sharedViewModel.listOfPlayers[calcPlayerTurn]
	private inline val calcPlayerTurn: Int get() = calcCurrentTurn.minus(1)
	private inline val calcCurrentTurn: Int get() = currTurn % plyCount.plus(1)
	private inline val listOfTimedTaskTurns get() = sharedViewModel.listOfRandomTimedTaskTurns
	private inline val isTimedTask get() = listOfTimedTaskTurns.contains(currTurn)

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
		(fisCard).newFragmentInstance().commit()
		setUpTextSwitcherRoundNum()

		btnSuccess.setOnClickListener(this)
		btnFail.setOnClickListener(this)

		setInitialValues()

		setUpCurrentPlayerObserver()
		setUpCurrentTurnObserver()
		setUpCurrentRoundObserver()
		gamingViewModel.currentTurn.postValue(0)

		listOfTimedTaskTurns.forEach { Log.d("!", "Random task-turns: $it") }
	}

	override fun onClick(v: View?) {

		if (isTimedTask){
			val generateRandomTask = tt.randomTask()
			gamingViewModel.updateRandomTaskCard(generateRandomTask)
			displayTimedTask()
			startEndTimedTaskHandler(generateRandomTask.seconds)

		}else {
			when (v?.id) {
				R.id.button_Success -> {
					updatePlayerPoints(SUCCESS)
				}
				R.id.button_Fail -> {
					updatePlayerPoints(FAIL)
				}
			}
		}
	}

	private fun displayTimedTask() {
		btnSuccess.visibility = View.INVISIBLE
		btnFail.visibility = View.INVISIBLE
		(fisBlank).newFragmentInstance().commit()
	}

	private fun startEndTimedTaskHandler(seconds: Long) {

	//	tvPlayerName.text = "Remaining: 10"
		(tvPlayerName).animateWithSetText(text = "10")
		object: SecondsTimer(
			totalRunningSeconds = seconds,
			updateInterval = 1,
			textView = tvPlayerName,
			tCallBack =  object: TimerCallBack {
				override fun onFinish() {
					gamingViewModel.apply { updateTurn() }
				}
			}
		){}.start()

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
			layoutId = frameLayout.id, tag = "CURRENT_SCORE", replace = true, animate = true,
		){}

		fisBlank = object : FragmentInputSettings(
			fragmentManager = this.childFragmentManager, fragment = CardTimedTaskFragment(),
			layoutId = frameLayout.id, tag = "TIMED_TASK", replace = true, animate = true,
		){}
	}

	private fun setInitialValues() {
		sharedViewModel.apply {
			plyCount = pCount
			maxRounds = amRounds
			totalTurns = totalTurnsPlus

			resources.apply {
				listOfConsequences = getStringArray(R.array.Consequences)
				listOfMissions = getStringArray(R.array.Mission)
				listOfConsequencesPoints = getIntArray(R.array.ConsequencesPoints)
				listOfMissionsPoints = getIntArray(R.array.MissionPoints)
			}
		}

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

			Log.d("!", "Timed Task: $isTimedTask Turn: $currTurn")

			when {
				isStart -> { gamingViewModel.updateTurn(); gamingViewModel.updateRound() }
				isNextFragment -> { endGame() }
				isPrepareNextRound -> { nextRound() }
				!isPrepareNextRound -> { nextPlayerTurn() }
			}

		})
	}

	private fun setUpCurrentPlayerObserver() {
		gamingViewModel.currentPlayer.observe(this, {
			currPlayer = it
			tvPlayerName.animateWithSetText(text = it.name)
		})
	}

	private fun displayScoreFragment() {
		frameLayout.background = null
		(fisScore).newFragmentInstance().commitAllowingStateLoss()
	}

	private fun endGame() {
		sharedViewModel.apply {
			listSumPairSorted()
			updateFragmentPos()
		}
	}

	private fun nextRound() {

		btnSuccess.apply {
			isClickable = false
			visibility = View.VISIBLE
		}
		val vFrom = (btnSuccess.width).toFloat()
		val vTo = (resources.displayMetrics.widthPixels).toFloat() - (btnSuccess.marginRight * 4)
		val valueAnimator: ValueAnimator? = nextRoundValueAnimator(vFrom, vTo)

		tvPlayerName.animateWithSetText(text = getString(R.string.current_score))
		mutableListOf({ btnFail.viewApplyVis(View.INVISIBLE) }).runIterateUnit()
		displayScoreFragment()

		btnSuccess.apply {
			text = getString(R.string.next_round)

			setOnClickListener {
				valueAnimator?.reverse()
				(fisCard).apply { replace = true }.newFragmentInstance().commit()
				gamingViewModel.apply {
					updateRound()
					updateTurn()
				}
			}
		}

		if(btnSuccess.isLaidOut) {
			valueAnimator?.start()
		}
	}

	private fun nextRoundValueAnimator(vFrom: Float, vTo: Float): ValueAnimator? {
		return ValueAnimator.ofFloat(vFrom, vTo)
			.apply {
				duration = 1000
				interpolator = AccelerateDecelerateInterpolator()
				addUpdateListener {
					val inter = it.animatedValue.toString().toFloat()
					btnSuccess.apply {
						layoutParams.width = inter.toInt()
						requestLayout()
					}
				}
				doOnEnd {
					btnSuccess.isClickable = true
				}
			}
	}

	private fun AppCompatTextView.animateWithSetText(text: String){
		this.slideOutRightInLeftSetText(sText = text).start()
	}

	private fun nextPlayerTurn() {

		btnSuccess.setOnClickListener(this)

		if ((CardFragment() as? Fragment)!!.isAdded.not()){(fisCard).apply { replace = true }.newFragmentInstance().commit() }

		gamingViewModel.apply {
			clearCard()
			updateCurrentCard(generateNewCard())
		}

		frameLayout.flip(listOfViews = listOfViews).start()
		gamingViewModel.updatePlayer(getCurrPlayerObj)
		(btnSuccess).btnChangeText("SUCCESS")
	}

	private fun generateNewCard(): Triple<String, Double, EnRandom> {

		val rIndex: Int
		val cardText: String
		val cardPoints: Double
		val cardType: EnRandom

		when (val rCardType = arrayOfEnRandoms.random()) {
			EnRandom.CONSEQUENCES -> {

				rIndex = listOfConsequences.getRandomListIndex()
				cardText = listOfConsequences[rIndex]
				cardPoints = listOfConsequencesPoints[rIndex].toDouble()
				cardType = rCardType
			}
			EnRandom.MISSION -> {

				rIndex = listOfMissions.getRandomListIndex()
				cardText = listOfMissions[rIndex]
				cardPoints = listOfMissionsPoints[rIndex].toDouble()
				cardType = rCardType
			}
		}
	    return Triple(cardText, cardPoints, cardType)
	}

	private fun FrameLayout.flip(listOfViews: MutableList<View>): AnimatorSet {

		val v = this

		if (calcCurrentTurn.isEqualTo(1)) {
			v.setBackgroundColor(getColor(requireActivity(), R.color.deep_purple_400))
		}
		val listOfButtons = listOfViews.filterIsInstance<AppCompatButton>().toMutableList()

		val a1 = v.flipToBackY().apply {
			duration = Animationz.flipCardDurationOneFourth
			interpolator = LinearInterpolator()

			doOnStart { listOfButtons.clickable(false) }
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
				doOnEnd {
					listOfButtons.clickable(true)
					listOfButtons.forEach {
						it.visibility = View.VISIBLE
					}
				}
			}

		return AnimatorSet().apply {
			playSequentially(a1, a2, a3, a4)
		}
	}

	private fun pairRoundWithPoints(double: Double): Pair<Int, Double> = Pair(currRound, double)

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

			f.apply {
				when (animate) {
					true -> {
						setCustomAnimations(
							R.anim.fragment_slide_right_enter,
							R.anim.fragment_slide_left_exit)
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
