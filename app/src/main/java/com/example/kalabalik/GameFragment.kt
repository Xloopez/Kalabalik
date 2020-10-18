package com.example.kalabalik

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    //
    lateinit var frontAnimation: AnimatorSet
    lateinit var backAnimation: AnimatorSet
    var isFront = false
    //var isFront = true

    //
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

    //val randomConsequenceIndex = (0 until arrayConsequence.count()).random()
    //val consequencePoints = arrayConsequencePoints[randomConsequenceIndex]
    var randomConsequenceIndex by Delegates.notNull<Int>()
    var consequencePoints by Delegates.notNull<Int>()

    //val randomMissionIndex = (0 until arrayMission.count()).random()
    //val missionPoints = arrayMissionPoints[randomMissionIndex]
    var randomMissionIndex by Delegates.notNull<Int>()
    var missionPoints by Delegates.notNull<Int>()


    var consequenceOptionPoints = 0
    var turn = 0
    var currentRound = 1

    val listOfChoices = mutableListOf("Consequence", "Mission")
    val finalRoundReached = GameSettings.amountOfRounds.plus(1)
    val instructionFragment = InstructionFragment()

    //val addPoints = consequenceOrMission()
    //val consequenceFrontImage = ImageView


    lateinit var instructionScreen: ConstraintLayout

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


        randomConsequenceIndex = (0 until arrayConsequence.count()).random()
        consequencePoints = arrayConsequencePoints[randomConsequenceIndex]

        randomMissionIndex = (0 until arrayMission.count()).random()
        missionPoints = arrayMissionPoints[randomMissionIndex]

        instructionScreen = view!!.findViewById(R.id.constraintLayout)

        instructionFragment()
        val snackBar = Snackbar.make(activity!!.findViewById(android.R.id.content), R.string.instruction_snackbar, Snackbar.LENGTH_INDEFINITE)

        instructionScreen.setOnTouchListener{v, event ->
            //Här sätter vi så att vår bild kan känna igen touch
            when(event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("!!!", "klickkkkk")
                    removeInstructionFragment()

                    snackBar.dismiss()

                    frontCardText.visibility = View.VISIBLE
                    backCardText.visibility = View.VISIBLE

                    if (isFront == false) {
                        flipCard()
                        playerTurn()
                        when (consequenceOrMission()){
                            "consequence" -> {
                                rightButton.setOnClickListener {
                                    rightButtonConcequensePoints(consequencePoints)
                                    flipCard()
                                    playerTurn()
                                }

                                leftButton.setOnClickListener {
                                    leftButtonConsequencePoints(consequenceOptionPoints)
                                    flipCard()
                                    playerTurn()
                                }
                            }
                            "mission" -> {
                                rightButton.setOnClickListener {
                                    rightMissionButtonPoints(missionPoints)
                                    flipCard()
                                    playerTurn()
                                }

                                leftButton.setOnClickListener {
                                    leftButtonMissionPoints(2)
                                    flipCard()
                                    playerTurn()
                                }

                            }
                        }
                        //isFront = true
                    }

                    //flipCard()
                    //playerTurn()
                }
            }
            true
        }
        snackBar.show()

        //Hit kommer vi efter att användaren klickat bort instruktionerna
        backCardText = view.findViewById(R.id.card_back)
        frontCardText = view.findViewById(R.id.card_Front)


        //Animator object
        //modified camera scale
        val appContext = getActivity()?.applicationContext
        val scale: Float = appContext?.resources?.displayMetrics!!.density//applicationContext.resources.displayMetrics.density

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

        /*backCardText = view.findViewById(R.id.card_back)
        frontCardText = view.findViewById(R.id.card_Front)*/

        //arrayConsequence = resources.getStringArray(R.array.Consequence)
        //arrayConsequencePoints = resources.getIntArray(R.array.ConsequencePoints)
        //
        //arrayMission = resources.getStringArray(R.array.Mission)
        //arrayMissionPoints = resources.getIntArray(R.array.MissionPoints)

        rightButton = view.findViewById(R.id.right_btn)
        leftButton = view.findViewById(R.id.left_btn)

        displayPlayerName = view.findViewById(R.id.playerNameTurn)

        displayPlayerName.text = ""//"${GameSettings.listOfPlayers[0].name}:s tur!"

        frontCardText.visibility = View.INVISIBLE
        backCardText.visibility = View.INVISIBLE

        //instructionFragment()
        //Snackbar.make(activity!!.findViewById(android.R.id.content), "${R.string.instruction_toast}", Snackbar.LENGTH_INDEFINITE).show()


        /*rightButton.setOnClickListener {
            Log.d("!!!", "ADDED POINTSSS")
            consequenceOrMission() 
            flipCard()
            playerTurn()

        }    */
    }


    fun flipCard() {
        when (isFront) {
            false -> {
                frontAnimation.setTarget(frontCardText)
                backAnimation.setTarget(backCardText)
                frontAnimation.start()
                backAnimation.start()
                isFront = false
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
        //val finalRoundReached = GameSettings.amountOfRounds.plus(1)

        when (currentRound) {
            0 -> {
                increaseRounds()
                Log.d("!!!", "Round $currentRound")
            }
            in roundInterval -> {
                turn++
                if (turn == GameSettings.listOfPlayers.count().plus(1)) {
                    increaseRounds()
                    restartcounter()

                    playerName = GameSettings.listOfPlayers[turn-1].name
                    displayPlayerName.text = "${playerName}:s tur!"

                    //consequenceOrMission()

                    Log.d("!!!", "Turn non-existing: increasingRounds() & restartcounter(): ${GameSettings.listOfPlayers.count().plus(1)}")
                } else {
                    playerName = GameSettings.listOfPlayers[turn-1].name
                    displayPlayerName.text = "${playerName}:s tur!"

                    //consequenceOrMission()
                }

                Log.d("!!!", "Player: ${playerName}! Round: $currentRound Turn: $turn")

                if (currentRound == finalRoundReached){
                    Log.d("!!!", "Next Activity")
                    scoreBoardActivity()
                }
            }
        }
    }

    fun consequenceOrMission() : String {

        val randomC = listOfChoices.random()
        //Log.d("!!!", "randomC: $randomC")

        when (randomC) {
            "Consequence" -> {
                //randomConsequenceIndex = (0 until arrayConsequence.count()).random()
                //Log.d("!!!", "ranCIdex: $randomConsequenceIndex")

                val consequenceStr = arrayConsequence[randomConsequenceIndex]
                //consequencePoints = arrayConsequencePoints[randomConsequenceIndex]
                val consequenceOption = consequenceChoice(randomConsequenceIndex)
                //Log.d("!!!", "$consequenceStr")
                //Log.d("!!!", "$consequencePoints")

                //Front card image
                frontCardText.setBackgroundResource(R.drawable.konsekvensfram)
                //Back card image
                backCardText.setBackgroundResource(R.drawable.konskortbakcopy)

                //Back card text
                backCardText.setText("$consequenceStr \n+$consequencePoints poäng" +
                        "\n Eller \n $consequenceOption \n+$consequenceOptionPoints poäng")

                //rightButtonConcequensePoints(consequencePoints)

                rightButton.setText("+$consequencePoints")

                //leftButton.visibility = View.VISIBLE
                leftButton.setText("+${consequenceOptionPoints}")


                /*leftButton.setOnClickListener {
                    leftButtonConsequencePoints((consequenceOptionPoints/2)-1)
                    flipCard()
                    playerTurn()
                }
                rightButton.setOnClickListener {
                    rightButtonConcequensePoints(consequencePoints)
                    flipCard()
                    playerTurn()
                }   */
                return "consequence"
            }
            "Mission" -> {
                //Front card image
                frontCardText.setBackgroundResource(R.drawable.uppdragskortframcopy)
                //Back card image
                backCardText.setBackgroundResource(R.drawable.kalabalauppdragbakcopy)

                //val randomMissionIndex = (0 until arrayMission.count()).random()
                //Log.d("!!!", "ranCIdex: $randomMissionIndex")

                val missionStr = arrayMission[randomMissionIndex]
                //val missionPoints = arrayMissionPoints[randomMissionIndex]
                //Log.d("!!!", "$missionStr")
                //Log.d("!!!", "$missionPoints")

                //leftButton.setText("-2")
                backCardText.setText("$missionStr \n+$missionPoints poäng (-2 poäng)")

                rightButton.setText("+$missionPoints")
                leftButton.setText("-2")

                //rightMissionButton(missionPoints)

                /*leftButton.setOnClickListener {
                    leftButtonMissionPoints(-2)
                    flipCard()
                    playerTurn()
                }
                rightButton.setOnClickListener {
                    rightMissionButton(missionPoints)
                    flipCard()
                    playerTurn()
                }  */
                return "mission"
            }
        }
        return ""
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
    fun increaseRounds() {
        currentRound++
    }

    fun restartcounter() {
        turn = 1
    }

    fun rightButtonConcequensePoints(points: Int){
        //leftButton.visibility = View.VISIBLE
        //rightButton.setText("+$points")
        GameSettings.addPointsToPlayer(turn-1, points)
    }

    fun leftButtonConsequencePoints(points: Int){
        GameSettings.addPointsToPlayer(turn-1, points)
    }

    fun rightMissionButtonPoints(points: Int){
        //leftButton.visibility = View.INVISIBLE
        //rightButton.setText("+$points")
        GameSettings.addPointsToPlayer(turn-1, points)
    }

    fun leftButtonMissionPoints(points: Int){
        //GameSettings.addPointsToPlayer(turn-1, points)
        GameSettings.removePointsFromPlayer(turn-1, points)
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
}