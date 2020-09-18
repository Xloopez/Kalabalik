package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.example.myapplication.databinding.ActivityMainBinding

const val hintAmount = "Amount of players"

class MainActivity : AppCompatActivity(), View.OnClickListener {

   // TODO CLEAN LIST ON RE-START?
    private lateinit var tvTitle: AppCompatTextView
    private lateinit var tvInputInfo: AppCompatTextView

    private lateinit var etInput: EditText

    private lateinit var btnContinue: AppCompatButton

    private var counter: Int = 0
    private lateinit var binding: ActivityMainBinding

    private val inputNumbers = InputObject(
        inputType = InputType.TYPE_CLASS_NUMBER,
        inputHint = "Enter amount of players, 2-5!",
        infoStr = hintAmount,
        arrayOfMinMaxVal = (2..5)
    )
    private val inputPlayers = InputObject(
        inputType = InputType.TYPE_CLASS_TEXT,
        inputHint = "",
        infoStr = "",
        arrayOfMinMaxChar = (3..10)
    )

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
        //setViewVisibility(btnContinue, visible = true)
        Util.setViewVisibilityFadeInOut(btnContinue, visible = true)
        clearEditTextForNewInput(inputNumbers)

        /* CHECK IF INPUT LENGTH/COUNT IS ABOVE A CERTAIN COUNT, 0 AT THE MOMENT */
        //TODO

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
                        inputPlayers.includeCounterValue(count = counter)
                        clearEditTextForNewInput(inputPlayers)
                        btnSetText(btnContinue, getString(R.string.add_player))

                    }
                    in 1 until GameSettings.playerCount -> {

                        addAdditionalPlayer(Player(name = etInput.text.toString(), playerNum = counter))
                        increaseCounterByOne()
                        inputPlayers.includeCounterValue(count = counter)
                        clearEditTextForNewInput(inputPlayers)

                    }
                    GameSettings.playerCount -> {

                        addAdditionalPlayer(Player(name = etInput.text.toString(), playerNum = counter))
                        increaseCounterByOne()
                        Util.setViewVisibilityFadeInOut(etInput, visible = false)
                        Util.setViewVisibilityFadeInOut(tvInputInfo, visible = false)
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

    private fun clearEditTextForNewInput(inputObj: InputObject){

        etInput.apply {
            inputType = inputObj.inputType
            hint = inputObj.inputHint
            setText("")
        }
        tvInputInfo.text = inputObj.infoStr


    }

    class InputObject(
        var inputType: Int,
        var inputHint: String,
        var infoStr: String,
        var arrayOfMinMaxVal: IntRange? = null,
        var arrayOfMinMaxChar: IntRange? = null
        ){

        private val strEnterName = "Enter the name of Player"
        private val hintPlayer = "Player"

        fun includeCounterValue(count: Int){
            inputHint = "$hintPlayer $count"
            infoStr = "$strEnterName $count"
        }
    }
}
