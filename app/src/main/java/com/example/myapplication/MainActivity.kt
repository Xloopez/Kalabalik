package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.example.myapplication.databinding.ActivityMainBinding

const val strEnterName = "Enter the name of Player"
const val hintAmount = "Amount of players"
const val hintPlayer = "Player"


class MainActivity : AppCompatActivity(), View.OnClickListener {

   // TODO CLEAN LIST ON RE-START?

    private lateinit var tvTitle: AppCompatTextView
    private lateinit var tvInputInfo: AppCompatTextView

    private lateinit var etInput: EditText

    private lateinit var btnContinue: AppCompatButton

    private var counter: Int = 0
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Kalabalik alltså"

        applyViewBinding()

        /* SET START-TEXT OF TEXTVIEWS */
        tvTitle.text = getString(R.string.app_name)

        /* SET THIS ACTIVITY AS VIEW.ONCLICKLISTENER FOR THE BUTTON */
        btnContinue.setOnClickListener(this)

        /* USE THE CUSTOM FUNCTION TO HIDE THE VIEW AT START */
        setViewVisibility(btnContinue, visible = true)

        clearEditTextForNewInput(
            newInputType = InputType.TYPE_CLASS_TEXT,
            newInfoStr = "Enter amount of players, 2-5!",
            newHint = "Amount of players"
        )

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

    private fun applyViewBinding() {
        binding.apply {

            /*TEXT-VIEWS*/
            tvTitle = textViewAppTitle
            tvInputInfo = textViewInputInfo

            /*EDIT-TEXTS*/
            etInput = editTextInput

            /*BUTTONS*/
            btnContinue = buttonContinue

        }
    }

    override fun onClick(v: View?) {

        when(v?.id){
            R.id.button_continue -> {

                when (counter) {
                    0 -> {

                        GameSettings.playerCount = Integer.parseInt(etInput.text.toString())
                        increaseCounterByOne()
                        clearEditTextForNewInput(
                            newInputType = InputType.TYPE_CLASS_TEXT,
                            newInfoStr = "$strEnterName $counter",
                            newHint = "$hintPlayer $counter"
                        )
                        btnSetText(btnContinue, getString(R.string.add_player))

                    }
                    in 1 until GameSettings.playerCount -> {

                        addAdditionalPlayer(Player(name = etInput.text.toString()))
                        increaseCounterByOne()
                        clearEditTextForNewInput(
                            newInputType = InputType.TYPE_CLASS_TEXT,
                            newInfoStr = "$strEnterName $counter",
                            newHint = "$hintPlayer $counter"
                        )

                    }
                    GameSettings.playerCount -> {

                        addAdditionalPlayer(Player(name = etInput.text.toString()))
                        increaseCounterByOne()
                        setViewVisibility(etInput, visible = false)
                        setViewVisibility(tvInputInfo, visible = false)
                        btnSetText(btnContinue, getString(R.string.start_game))
                        Animationz.animButton(btnContinue)

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

    private fun clearEditTextForNewInput(newInputType: Int, newInfoStr: String, newHint: String){
        etInput.apply {
            inputType = newInputType
            hint = newHint
            setText("")
        }
        tvInputInfo.text = newInfoStr
    }

    private fun setViewVisibility(view: View, visible: Boolean) = when (visible) {
        true -> {
            view.visibility = View.VISIBLE
            Animationz.fadeInAnim(view)
        }
        false -> {
            view.visibility = View.INVISIBLE
            Animationz.fadeOutAnim(view)
        }
    }

}

//                        GameSettings.listOfPlayers.forEach {
//                            Log.v(TAG, "${it.name}")
//                        }