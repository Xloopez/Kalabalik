package com.example.myapplication.gaming

import android.animation.ValueAnimator
import android.graphics.BlendMode.SRC_ATOP
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff.Mode
import android.graphics.PorterDuffColorFilter
import android.media.MediaPlayer
import android.os.Build
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
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginRight
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.*
import com.example.myapplication.animators.View360Flip
import com.example.myapplication.databinding.FragmentGamingBinding
import com.example.myapplication.dataclasses.Player
import com.example.myapplication.utilities.*
import com.example.myapplication.utilities.Animationz.checkCameraDistance
import com.example.myapplication.utilities.Animationz.slideOutRightInLeftSetText

class GamingFragment : Fragment(), View.OnClickListener {

	private lateinit var sharedViewModel: SharedViewModel
	private lateinit var gamingViewModel: GamingViewModel

	private var _binding: FragmentGamingBinding? = null
	private val binding get() = _binding!!

	private lateinit var tvPlayerName: AppCompatTextView
	private lateinit var tvTotalRounds: AppCompatTextView
	private lateinit var tvCounter: AppCompatTextView

	private lateinit var tSwitcherRounds: TextSwitcher

	private lateinit var btnSuccess: AppCompatButton
	private lateinit var btnFail: AppCompatButton

	private lateinit var listOfViews: MutableList<View>
	private lateinit var frameLayout: FrameLayout

	private val generatorTimedTask by lazy { GeneratorTimedTask(requireContext()) }

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
	private lateinit var fisTimedScore: FragmentInputSettings

	private inline val isPrepareNextRound get() = calcCurrentTurn.isZero()
	private inline val isStart get() = (currTurn == 0) and (calcCurrentTurn == 0)
	private inline val isNextFragment get() = currTurn.isEqualTo(totalTurns)
	private inline val getCurrPlayerObj get() = sharedViewModel.listOfPlayers[calcPlayerTurn]
	private inline val calcPlayerTurn: Int get() = calcCurrentTurn.minus(1)
	private inline val calcCurrentTurn: Int get() = currTurn % plyCount.plus(1)
	private inline val listOfTimedTaskTurns get() = sharedViewModel.listOfRandomTimedTaskTurns
	private inline val isTimedTask get() = listOfTimedTaskTurns.contains(currTurn)

	private var soundMissionOrConsequence: MediaPlayer? = null
	private val soundTimedTask: MediaPlayer? by lazy {
		createMediaPlayer(requireActivity(),
			R.raw.balalaika_russian_14_930)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View? {
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
		(frameLayout).checkCameraDistance(targetScale = (scale * 16000))

		soundMissionOrConsequence = createMediaPlayer(requireActivity(), R.raw.trail_swoosh_1_195)
		//soundTimedTask = createMediaPlayer(requireActivity(), R.raw.balalaika_russian_14_930)

		setFragmentInputs()
		(fisCard).newFragmentInstance().commit()
		setUpTextSwitcherRoundNum()

		btnSuccess.setOnClickListener(this)
		btnFail.setOnClickListener(this)

		setInitialValues()

		setUpCurrentPlayerObserver()
		setUpCurrentTurnObserver()
		setUpCurrentRoundObserver()
		gamingViewModel.currentTurn.postEmpty() //NEEDED??

//		sharedViewModel.listOfMissionOrConsequenceTurns.forEach { Log.d("!", "$it") }
//		listOfTimedTaskTurns.forEach { Log.d("!", "Random task-turns: $it") }

	}

	override fun onClick(v: View?) {

		when (v?.id) {
			R.id.button_Success -> {
				updatePlayerPoints(EnOperation.SUCCESS)
			}
			R.id.button_Fail -> {
				updatePlayerPoints(EnOperation.FAIL)
			}
		}
	}

	private fun displayTimedTask() {
		gamingViewModel.clearCardFragment.postEmpty()
		(frameLayout).flip(false).getAnimatorSet().start()

		(fisTimedScore).newFragmentInstance().commit()

		try {
			// TODO: 2020-10-18 uncomment below
			soundTimedTask?.start()
		} catch (e: Exception) {
			makeLogD("Sound timed task ERROR: $e")
		}
	}

	private fun startTimer(seconds: Long) {

		(tvPlayerName).animateWithSetText(text = getString(R.string.timed_task))
		object : SecondsTimer(
			totalRunningSeconds = seconds,
			updateInterval = 1,
			textView = tvCounter,
			tCallBack = object : TimerCallBack {
				override fun onFinish() {
					gamingViewModel.apply { currentTurn.postUpdateIntBy(1) }
					tvCounter.visibility = View.INVISIBLE
				}

			}
		){}.start()
	}

	private fun applyViewBinding() {

		binding.apply {

			tvPlayerName = textViewPlayerName
			tvTotalRounds = textViewAmountOfRounds
			tvCounter = textViewCounter

			tSwitcherRounds = textSwitcherRoundNum

			btnSuccess = buttonSuccess
			btnFail = buttonFail

			frameLayout = frameLayoutCardPoints
		}
		listOfViews = mutableListOf(btnFail, btnSuccess)
	}

	private fun setUpTextSwitcherRoundNum() {
		tSwitcherRounds.apply {

			inAnimation = AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_in_left)
			outAnimation = AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_out_right)

			setFactory {
				val textView = TextView(requireContext()).apply {
					typeface = ResourcesCompat.getFont(requireContext(), R.font.irish_grover)
					textSize = 40f
					setTextColor(Color.WHITE)
					gravity = Gravity.START
					text = "0"
				}
				textView
			}
		}
	}

	private fun setFragmentInputs() {
		fisCard = object : FragmentInputSettings(
			fragmentManager = this.childFragmentManager,
			fragment = CardMissionConsequenceFragment(),
			layoutId = frameLayout.id,
			tag = "CARD",
		){}
		fisScore = object : FragmentInputSettings(
			fragmentManager = this.childFragmentManager,
			fragment = GameScoreFragment(miniScore = EnScore.MINI),
			layoutId = frameLayout.id,
			tag = "CURRENT_SCORE",
			replace = true,
			animate = true,
		){}

		fisTimedScore = object : FragmentInputSettings(
			fragmentManager = this.childFragmentManager, fragment = CardTimedTaskFragment(),
			layoutId = frameLayout.id, tag = "TIMED_TASK", replace = true, animate = true,
		){}
	}

	private fun setInitialValues() {
		sharedViewModel.apply {
			plyCount = pCount
			maxRounds = amRounds
			totalTurns = totalTurnsPlus
		}
		tvTotalRounds.apply {
			text = getString(R.string.out_of_x_rounds, maxRounds)
		}

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
				isStart -> {
					gamingViewModel.apply {
						currentTurn.postUpdateIntBy(1)
						currentRound.postUpdateIntBy(1)
					}
				}
				isNextFragment -> {
					endGame()
				}
				isPrepareNextRound -> {
					nextRound()
				}
				!isPrepareNextRound -> {
					nextPlayerTurn()
				}
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
			listSumPairSort()
			updateFragmentPos()
		}
	}

	private fun nextRound() {

		btnSuccess.apply {
			isClickable = false
			visibility = View.VISIBLE
		}

		val valueAnimator: ValueAnimator? = nextRoundValueAnimator()

		(tvPlayerName).animateWithSetText(text = getString(R.string.current_score))
		mutableListOf({ btnFail.viewApplyVis(View.INVISIBLE) }).runIterateUnit()
		displayScoreFragment()

		btnSuccess.apply {
			text = getString(R.string.next_round)

			setOnClickListener {
				valueAnimator?.reverse()
				(fisCard).apply { replace = true }.newFragmentInstance().commit()

				gamingViewModel.apply {
					currentRound.postUpdateIntBy(1)
					currentTurn.postUpdateIntBy(1)
				}
			}
		}

		if (btnSuccess.isLaidOut) {
			valueAnimator?.start()
		}
	}

	private fun nextRoundValueAnimator(): ValueAnimator? {
		val vFrom = (btnSuccess.width).toFloat()
		val vTo = (resources.displayMetrics.widthPixels).toFloat() - (btnSuccess.marginRight * 4)

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

		if ((CardMissionConsequenceFragment() as? Fragment)!!.isAdded.not()) {
			(fisCard).apply { replace = true }.newFragmentInstance().commit()
		}

		val nextCard = sharedViewModel.getListCard(currTurn)

		gamingViewModel.apply {
			clearCardFragment.postEmpty()
			updateCurrentCard(nextCard)
		}

		(frameLayout).flip(card = nextCard).getAnimatorSet().start()
		gamingViewModel.updatePlayer(getCurrPlayerObj)
		(btnSuccess).btnChangeText(getString(R.string.Flipp))
	}

	private fun FrameLayout.flip(
		isNotTimedTask: Boolean = true,
		card: CardMissionConsequence? = null
	): View360Flip {
		val v = this

		var colorFilterDefault: ColorFilter = getColorFilter()

		if (calcCurrentTurn.isEqualTo(1)) {
			v.background = getDrawable(requireContext(), R.drawable.card_background_back)
			v.background.clearColorFilter()
		}

		card?.type?.getBackGroundColor()?.let {
			colorFilterDefault = getColorFilter(resColor = card.getBackColor())
		}

		try {
			// TODO: 2020-10-18 uncomment below
			soundMissionOrConsequence?.start()
		} catch (e: Exception) {
			makeLogD("$e")
		}

		val listOfButtons = listOfViews.filterIsInstance<AppCompatButton>().toMutableList()
		listOfButtons.forEach { it.visibility = View.INVISIBLE }

		return View360Flip(
			view = frameLayout,
			totalDuration = Animationz.flipCardDuration,
			arrayListOf(
				LinearInterpolator(),
				DecelerateInterpolator(),
				DecelerateInterpolator(),
				LinearInterpolator()
			),
			flipCallBack = object : View360Flip.FlipAnimationInterface {

				override fun firstFourthStart() {
					listOfButtons.clickable(false)
					v.animate().scaleXBy(-0.5f).scaleYBy(-0.5f)

				}

				override fun firstFourthEnd() {
					v.apply {
						background = getDrawable(requireContext(), R.drawable.card_background_back)
						background.clearColorFilter()
					}
				}

				override fun thirdFourthOnEnd() {
					gamingViewModel.updateCardFragment.postValue(1)
					v.apply {
						animate().scaleXBy(0.5f).scaleYBy(0.5f)
						background.colorFilter = colorFilterDefault
					}
				}

				override fun fourthFourthOnEnd() {
					if (isNotTimedTask) {
						listOfButtons.forEach {
							it.isClickable = true
							it.visibility = View.VISIBLE
						}
					}
				}
			})
	}

	private fun getColorFilter(resColor: Int = R.color.purple_800): ColorFilter {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			BlendModeColorFilter(getColor(requireContext(), resColor), SRC_ATOP)
		} else {
			PorterDuffColorFilter(
				getColor(requireContext(), resColor),
				Mode.SRC_ATOP
			)
		}
	}

	private fun updatePlayerPoints(operation: EnOperation) {

		val currCard = gamingViewModel.currentCard.value!!

		currPlayer.listAddCard(
			when (operation) {
				EnOperation.SUCCESS -> {
					currCard.apply {
						setRound(currRound)
					}
				}
				EnOperation.FAIL -> {
					currCard.apply {
						setRound(currRound)
						points = -5.0
					}
				}
			})

		if (isTimedTask) {
			val generatedTask = generatorTimedTask.listOfTasks.random()
			gamingViewModel.updateRandomTaskCard(generatedTask)

			displayTimedTask()
			startTimer(generatedTask.seconds)
		} else {
			gamingViewModel.currentTurn.postUpdateIntBy(1)
		}


	}

}