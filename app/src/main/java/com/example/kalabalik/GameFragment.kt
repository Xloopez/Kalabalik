package com.example.kalabalik

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import kotlin.properties.Delegates


class GameFragment : Fragment() {


    lateinit var frontAnimation: AnimatorSet
    lateinit var backAnimation: AnimatorSet
    var isFront = false

    lateinit var displayPlayerName: TextView
    lateinit var backCardText: TextView
    lateinit var frontCardText: TextView

    lateinit var arrayConsequence: Array<String>
    lateinit var arrayConsequencePoints: IntArray
    lateinit var arrayMission: Array<String>
    lateinit var arrayMissionPoints: IntArray

    lateinit var rightButton: Button
    lateinit var leftButton: Button
    lateinit var playerName: String

    lateinit var instructionScreen: ConstraintLayout

    val randomConsequenceIndex get() =  (0 until arrayConsequence.count()).random()
    var consequencePoints by Delegates.notNull<Int>()

    val randomMissionIndexGet get() = (0 until arrayMission.count()).random()
    var missionPoints by Delegates.notNull<Int>()


    var consequenceOptionPoints = 0
    var turn = 0
    var currentRound = 1

    val listOfChoices = mutableListOf("Consequence", "Mission")
    val finalRoundReached = GameSettings.amountOfRounds.plus(1)
    val instructionFragment = InstructionFragment()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_game, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrayConsequence = resources.getStringArray(R.array.Consequence)               
        arrayConsequencePoints = resources.getIntArray(R.array.ConsequencePoints)
        arrayMission = resources.getStringArray(R.array.Mission)                       
        arrayMissionPoints = resources.getIntArray(R.array.MissionPoints)
        instructionScreen = view!!.findViewById(R.id.constraintLayout)
        displayPlayerName = view.findViewById(R.id.playerNameTurn)
        backCardText = view.findViewById(R.id.card_back)
        frontCardText = view.findViewById(R.id.card_Front)
        rightButton = view.findViewById(R.id.right_btn)
        leftButton = view.findViewById(R.id.left_btn)


        consequencePoints = arrayConsequencePoints[randomConsequenceIndex]
        missionPoints = arrayMissionPoints[randomMissionIndexGet]

        makeViewsInvinsible()

        cardAnimator()

        instructionFragment()

        val snackBar = Snackbar.make(activity!!.findViewById(android.R.id.content), R.string.instruction_snackbar, Snackbar.LENGTH_INDEFINITE)

        instructionScreen.setOnTouchListener{v, event ->
            //Här sätter vi så att vår bild kan känna igen touch
            when(event?.action) {
                MotionEvent.ACTION_DOWN -> {

                    removeInstructionFragment()
                    snackBar.dismiss()
                    makeViewsVisible()
                    flipCard()
                    playerTurn()
                    consequenceOrMission()

                }
            }
            true
        }
    }

    fun flipCard() {
        when (isFront) {
            false -> {
                frontAnimation.setTarget(frontCardText)
                backAnimation.setTarget(backCardText)
                frontAnimation.start()
                backAnimation.start()
            }
            true -> {
                frontAnimation.setTarget(backCardText)
                backAnimation.setTarget(frontCardText)
                backAnimation.start()
                frontAnimation.start()
                isFront = false
            }
        }
    }

    fun playerTurn() {
        val roundInterval = (1..GameSettings.amountOfRounds)


        when (currentRound) {
            0 -> {
                increaseRounds()
            }
            in roundInterval -> {
                turn++
                if (turn == GameSettings.listOfPlayers.count().plus(1)) {
                    increaseRounds()
                    restartcounter()
                    setPlayerTurn()
                    consequenceOrMission()
                } else {
                    setPlayerTurn()
                    consequenceOrMission()
                }

                if (currentRound == finalRoundReached){
                    scoreBoardActivity()
                }
            }
        }
    }

    fun consequenceOrMission() : String {
        var stringD = StringBuilder()

        when (val str = listOfChoices.random()) {

            getString(R.string.consequence) -> {
                setCardImage(R.drawable.nyk, R.drawable.nykbak)

                val consequenceStr = arrayConsequence[randomConsequenceIndex]
                val consequenceOption = consequenceChoice(randomConsequenceIndex)

                //Back card text
                stringD.apply {
                    append(consequenceStr)
                    appendLine()
                    append("+$consequencePoints poäng")
                    appendLine()
                    appendLine()
                    append("Eller")
                    appendLine()
                    appendLine()
                    append(consequenceOption)
                    appendLine()
                    append("+$consequenceOptionPoints poäng")
                }.toString()

                backCardText.text = stringD

                rightButton.text = "+$consequencePoints"
                rightButtonPoints(consequencePoints)
                leftButton.text = "+${consequenceOptionPoints}"
                leftButtonPoints(consequenceOptionPoints)

                return str
            }
            getString(R.string.mission) -> {
                setCardImage(R.drawable.uppdragnyast, R.drawable.nyubak)

                val missionStr = arrayMission[randomMissionIndexGet]

                stringD.apply {
                    append(missionStr)
                    appendLine()
                    append("+$missionPoints poäng (-2 poäng)")
                }.toString()

                backCardText.setText(stringD)

                rightButton.setText("+$missionPoints")
                rightButtonPoints(missionPoints)
                leftButton.setText("-2")
                leftButtonMissionPoints(-2)

                return str
            }
        }
        return ""
    }


    fun cardAnimator(){
        //Animator object
        //modified camera scale
        val appContext = getActivity()?.applicationContext
        val scale: Float = appContext?.resources?.displayMetrics!!.density

        frontCardText.cameraDistance = 8000 * scale
        backCardText.cameraDistance = 8000 * scale

        //front animation
        frontAnimation = AnimatorInflater.loadAnimator(
            appContext,
            R.animator.front_animator
        ) as AnimatorSet
        backAnimation = AnimatorInflater.loadAnimator(
            appContext,
            R.animator.back_animator
        ) as AnimatorSet
    }

    fun consequenceChoice(index: Int): String {

        var consequenceCoiceStr = ""

        if (index.plus(1) % 2 != 0) {
            consequenceCoiceStr = arrayConsequence[index.plus(1)]
            consequenceOptionPoints = arrayConsequencePoints[index.plus(1)]
            return consequenceCoiceStr
        } else if (index.plus(1) % 2 == 0){
            consequenceCoiceStr = arrayConsequence[index - 1]
            consequenceOptionPoints = arrayConsequencePoints[index - 1]
        }

        return consequenceCoiceStr
    }
    fun setCardImage(imageFront: Int, imageBack: Int) {
        //Front card image
        frontCardText.setBackgroundResource(imageFront)
        //Back card image
        backCardText.setBackgroundResource(imageBack)
    }

    fun setPlayerTurn(){
        playerName = GameSettings.listOfPlayers[turn-1].name
        displayPlayerName.text = "${playerName}:s tur!"
    }
    fun makeViewsInvinsible(){
        frontCardText.visibility = View.INVISIBLE
        backCardText.visibility = View.INVISIBLE
        displayPlayerName.visibility = View.INVISIBLE
    }

    fun makeViewsVisible(){
        frontCardText.visibility = View.VISIBLE
        backCardText.visibility = View.VISIBLE
        displayPlayerName.visibility = View.VISIBLE
    }

    fun rightButtonPoints(points: Int){
        rightButton.setOnClickListener {
            GameSettings.addPointsToPlayer(turn-1, points)
            flipCard()
            playerTurn()
        }
    }

    fun leftButtonPoints(points: Int){
        leftButton.setOnClickListener {
            GameSettings.addPointsToPlayer(turn-1, points)
            flipCard()
            playerTurn()
        }
    }

    fun leftButtonMissionPoints(points: Int){
        leftButton.setOnClickListener {
            GameSettings.removePointsFromPlayer(turn-1, points)
            flipCard()
            playerTurn()
        }
    }

    fun scoreBoardActivity (){
        activity.let {
            val intent = Intent(it, HighScoreActivity::class.java)
            it!!.startActivity(intent)
        }
    }
    fun instructionFragment(){
        //Här kommer fragment med instruktionerna
        val transaction = activity?.supportFragmentManager!!.beginTransaction()

        transaction.add(R.id.playerFragmentContainer, instructionFragment, "instructionFragment")
        transaction.commit()
    }
    fun removeInstructionFragment(){
        val transaction = activity?.supportFragmentManager!!.beginTransaction()
        transaction.remove(instructionFragment)
        transaction.commit()
    }
    fun increaseRounds() {
        currentRound++
    }

    fun restartcounter() {
        turn = 1
    }
}