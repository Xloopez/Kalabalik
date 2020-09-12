package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import kotlinx.coroutines.Job

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var TAG = this::class.java.simpleName

   // val GameSettings: GameSettings? = GameSettings()
   // TODO CLEAN LIST ON START?

    private lateinit var tvAppName: AppCompatTextView
    private lateinit var tvInputInfo: AppCompatTextView

    private lateinit var etInput: AppCompatEditText

    lateinit var btnContinue: AppCompatButton
    var counter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Kalabalik alltså"

        /*TEXT-VIEWS*/
        tvAppName = findViewById(R.id.tv_app_title)
        tvInputInfo = findViewById(R.id.tv_input_info)

        /*EDIT-TEXTS*/
        etInput = findViewById(R.id.et_input)

        /*BUTTONS*/
        btnContinue = findViewById(R.id.btn_continue)

        /* SET START-TEXT OF TEXTVIEWS */
        tvAppName.text = "KALABALIK"
        tvInputInfo.text = "Enter amount of players, 1-5!"


        /* SET THIS ACTIVITY AS VIEW.ONCLICKLISTENER FOR THE BUTTON */
        btnContinue.setOnClickListener(this)

        /* USE THE CUSTOM FUNCTION TO HIDE THE VIEW AT START */
       // setViewVisibility(btnContinue, visible = false)

        /* CHECK IF INPUT LENGTH/COUNT IS ABOVE A CERTAIN COUNT, 0 AT THE MOMENT */
        //TODO Min-Length?
//        etInput.doAfterTextChanged {
//
//        }
//        etInput.doOnTextChanged { text, start, count, after ->
//            when {
//               count >= 1 -> { setViewVisibility(btnContinue, visible = false) }
//               else -> { setViewVisibility(btnContinue, visible = true) }
//            }
//        }


    }

    override fun onClick(v: View?) {
        val strPlayerCurrentCount: String

        when(v?.id){
            R.id.btn_continue -> {

                when (counter) {
                    0 -> {
                        GameSettings.playerCount = Integer.parseInt(etInput.text.toString())
                        counter++
                       // Log.d(TAG, "$counter")
                        strPlayerCurrentCount = "Enter the name of Player $counter"
                        clearEditTextForNewInput(
                            newInputType = InputType.TYPE_CLASS_TEXT,
                            newInfoStr = strPlayerCurrentCount
                        )
                        btnContinue.text = "ADD PLAYER"
                        //Log.v(TAG, Integer.parseInt(et_input.text.toString()).toString())
                    }
                    in 1 until GameSettings.playerCount -> {

                        val newPlayer = Player(name = etInput.text.toString())
                        GameSettings.addPlayerToList(player = newPlayer)
                        counter++
                        strPlayerCurrentCount = "Enter the name of Player $counter"
                        clearEditTextForNewInput(
                            newInputType = InputType.TYPE_CLASS_TEXT,
                            newInfoStr = strPlayerCurrentCount
                        )

                    }
                    GameSettings.playerCount -> {

                        val newPlayer = Player(name = etInput.text.toString())
                        GameSettings.addPlayerToList(player = newPlayer)
                        counter ++
                        setViewVisibility(etInput, visible = false)
                        setViewVisibility(tvInputInfo, visible = false)
                        btnContinue.text = "START GAME"

                        animButton(btnContinue)
                        //Log.v(TAG, "${GameSettings.playerCount}")
                        GameSettings.listOfPlayers.forEach {
                            Log.v(TAG, "${it.name}")
                        }
                    }
                    GameSettings.playerCount.plus(1) -> {
                          val intent = Intent(this, GamingActivity::class.java)
                          startActivity(intent)
                          /* FINISH "DELETE" THE ACTIVITY */
                          this.finish()
                    }
                }
            }
        }
    }

    private fun addPlayer(player: Player){

    }

    private fun clearEditTextForNewInput(newInputType: Int, newInfoStr: String){
        etInput.apply {
            inputType = newInputType
            setText("")
        }
        tvInputInfo.text = newInfoStr
    }

    private fun setViewVisibility(view: View, visible: Boolean){
        when (visible) {
            true -> {
                view.visibility = View.VISIBLE
                //TODO view animation fade in
                fadeInAnim(view)
            }
            false -> {
                view.visibility = View.INVISIBLE
                //TODO view-animation fade out
                fadeOutAnim(view)
            }
        }
    }


    fun fadeInAnim(view: View){

        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.apply {
            interpolator = DecelerateInterpolator()
            duration = 300
        }
        view.animation = fadeIn
        fadeIn.start()

    }

    fun fadeOutAnim(view: View){
        val fadeIOut = AlphaAnimation(1f, 0f)
        fadeIOut.apply {
            interpolator = DecelerateInterpolator()
            duration = 300
        }
        view.animation = fadeIOut
        fadeIOut.start()
    }

    fun animButton(view: View){

        val translateYAmount = (+ view.height + 150f)
        view.animate()
            .translationYBy(translateYAmount)
            .scaleXBy(1.5f)
            .scaleYBy(1.5f)
            .start()


    }

}