package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentGamingInputBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class GameInputFragment: Fragment(), View.OnClickListener {

    lateinit var sharedViewModel: SharedViewModel
    private var _binding: FragmentGamingInputBinding? = null
    private val binding get() = _binding!!

    private lateinit var tvTitle: AppCompatTextView
    private lateinit var tvInputInfo: AppCompatTextView

    private lateinit var tilInput: TextInputLayout
    private lateinit var etInput: TextInputEditText

    private lateinit var btnContinue: AppCompatButton

    private var counter: Int = 0

    private val playerAmountNumRange = (2..5)
    private val playerNameMinMaxLength = (3..10)

    private var bVisible = false

    private var playerCount = 0

    private val inputNumbers = InputObject(
        inputType = InputType.TYPE_CLASS_NUMBER,
        inputHint = "Amount of players",
        infoStr = "Enter amount of players, 2-5!",
    )

    private val inputPlayers = InputObject(
        inputType = InputType.TYPE_CLASS_TEXT,
        inputHint = "",
        infoStr = "",
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        _binding = FragmentGamingInputBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyViewBinding()

        /* SET START-TEXT OF TEXTVIEWS */
        tvTitle.text = getString(R.string.app_name)

        /* SET THIS ACTIVITY AS VIEW.ONCLICKLISTENER FOR THE BUTTON */
        btnContinue.setOnClickListener(this)

        /* USE THE CUSTOM FUNCTION TO HIDE THE VIEW AT START */
        //setViewVisibility(btnContinue, visible = true)
        Util.viewApplyVis(btnContinue, View.INVISIBLE)
        clearEditTextForNewInput(inputObj = inputNumbers)

        sharedViewModel.playerCount.observe(this, {
            playerCount = it
        })

        /* TEXT-WATCHER TO CHECK FOR CHANGES, DISPLAY BUTTON IF CERTAIN CRITERIA IS MET   */
        etInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("!", "$count")
                Util.setViewVisibilityFadeInOut(view = btnContinue, visible = bVisible) //ENABLE DISABLE BUTTON ANIM
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {

                bVisible = false

                if (charSequence.toString().isNotEmpty()) {
                    when (etInput.inputType) {
                        InputType.TYPE_CLASS_TEXT -> {
                            when(checkValidTextLength(charSequence)) {true -> { bVisible = true} }
                        }
                        InputType.TYPE_CLASS_NUMBER -> {
                            when(checkValidNumber(charSequence)) {true -> { bVisible = true} }
                        }
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                Util.setViewVisibilityFadeInOut(view = btnContinue, visible = bVisible)
            }
        })
    }

    private fun checkValidNumber(charSequence: CharSequence?): Boolean =
        try {
            when(val parseNum = Integer.parseInt(charSequence.toString())){
                in playerAmountNumRange -> {
                    Log.d("!", "CORRECT NUMBER $parseNum")
                    true
                }
                else -> {
                    Log.d("!", "INCORRECT - NUMBER NOT IN RANGE $parseNum")
                    false
                }
            }
        }catch (e: Exception){
            Log.d("!", "checkValidNumber: $e")
            false
        }

    private fun checkValidTextLength(charSequence: CharSequence?): Boolean =
        try {
            when(val length = charSequence.toString().length){
                in playerNameMinMaxLength -> {
                    Log.d("!", "VALID $length TEXT LENGTH")
                    true
                }
                else -> {
                    Log.d("!", "INVALID $length TEXT LENGTH")
                    false
                }
            }
        }catch (e: Exception){
            Log.d("!", "checkValidTextLength: $e")
            false
        }

    private fun applyViewBinding() {
        binding.apply {

            /*TEXT-VIEWS*/
            tvTitle = textViewAppTitle
            tvInputInfo = textViewInputInfo

            /*EDIT-TEXTS*/
            tilInput = textInputLayoutInput
            etInput = editTextInput

            /*BUTTONS*/
            btnContinue = buttonContinue

        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_continue -> { doNext() }
        }
    }

    private fun doNext(){

        when (counter) {
            0 -> {

                sharedViewModel.playerCount.postValue(Integer.parseInt(etInput.text.toString()))
                increaseCounterByOne()
                inputPlayers.includeCounterValue(count = counter)
                clearEditTextForNewInput(inputPlayers)
                Animationz.slideOutRightSlideInLeft(tilInput)
                btnSetText(btnContinue, getString(R.string.add_player))
                etInput.setText("Player $counter") //TODO REMOVE LATER
            }
            in 1 until playerCount -> {

                addAdditionalPlayer(Player(name = etInput.text.toString(), playerNum = counter))
                increaseCounterByOne()
                inputPlayers.includeCounterValue(count = counter)
                Animationz.slideOutRightSlideInLeft(tilInput)
                clearEditTextForNewInput(inputPlayers)
                etInput.setText("Player $counter") //TODO REMOVE LATER

            }
            playerCount -> {

                addAdditionalPlayer(Player(name = etInput.text.toString(), playerNum = counter))
                increaseCounterByOne()
                Util.viewApplyVis(tilInput, View.INVISIBLE)
                Util.viewApplyVis(tvInputInfo, View.INVISIBLE)
                btnSetText(btnContinue, getString(R.string.start_game))
                Animationz.animButton(btnContinue)
                Animationz.hideSoftKeyBoard(requireActivity(), btnContinue)

            }
            playerCount.plus(1) -> {

                sharedViewModel.currentFragmentPos.postValue(1)

            }
        }
    }

    private fun addAdditionalPlayer(newPlayer: Player){
        sharedViewModel.addPlayerToList(player = newPlayer)
    }

    private fun btnSetText(btn: AppCompatButton, text: String){
        btn.text = text
    }

    private fun increaseCounterByOne(){
        counter ++
    }

    private fun clearEditTextForNewInput(inputObj: InputObject){

        tilInput.apply {
            hint = inputObj.inputHint
            helperText = inputObj.infoStr
        }

        etInput.apply {
            inputType = inputObj.inputType
            setText("")
        }
        etInput.requestFocus()
       // tvInputInfo.text = inputObj.infoStr

    }

    class InputObject(var inputType: Int, var inputHint: String, var infoStr: String){

        private val strEnterName = "Enter the name of Player"
        private val hintPlayer = "Player"

        fun includeCounterValue(count: Int){
            inputHint = "$hintPlayer $count"
            infoStr = "$strEnterName $count"
        }
    }


}