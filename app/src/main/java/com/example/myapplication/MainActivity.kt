package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.TextView
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

        when(v?.id){
            R.id.btn_continue -> {

                when (counter) {
                    0 -> {

                        GameSettings.playerCount = Integer.parseInt(etInput.text.toString())
                        increaseCounterByOne()
                        clearEditTextForNewInput(newInputType = InputType.TYPE_CLASS_TEXT, newInfoStr = "Enter the name of Player $counter")
                        btnSetText(btnContinue, getString(R.string.add_player))

                    }
                    in 1 until GameSettings.playerCount -> {

                        addAdditionalPlayer(Player(name = etInput.text.toString()))
                        increaseCounterByOne()
                        clearEditTextForNewInput(newInputType = InputType.TYPE_CLASS_TEXT, newInfoStr = "Enter the name of Player $counter")

                    }
                    GameSettings.playerCount -> {

                        addAdditionalPlayer(Player(name = etInput.text.toString()))
                        increaseCounterByOne()
                        setViewVisibility(etInput, visible = false)
                        setViewVisibility(tvInputInfo, visible = false)
                        btnSetText(btnContinue, getString(R.string.start_game))
                        animButton(btnContinue)

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

    private fun addAdditionalPlayer(newPlayer: Player){
        GameSettings.addPlayerToList(player = newPlayer)
    }
    private fun btnSetText(btn: AppCompatButton, text: String){
        btn.text = text
    }
    private fun increaseCounterByOne(){
        counter ++
    }

    private fun clearEditTextForNewInput(newInputType: Int, newInfoStr: String){
        etInput.apply {
            inputType = newInputType
            setText("")
        }
        tvInputInfo.text = newInfoStr
    }

    private fun setViewVisibility(view: View, visible: Boolean) = when (visible) {
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

//                        GameSettings.listOfPlayers.forEach {
//                            Log.v(TAG, "${it.name}")
//                        }