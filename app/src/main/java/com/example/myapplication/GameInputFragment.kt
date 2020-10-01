package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.Animationz.slideOutRightSlideInLeft
import com.example.myapplication.Util.hideSoftKeyBoard
import com.example.myapplication.databinding.FragmentGamingInputBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class GameInputFragment : Fragment(), View.OnClickListener {

    private lateinit var sharedViewModel: SharedViewModel
    private var _binding: FragmentGamingInputBinding? = null
    private val binding get() = _binding!!

    private lateinit var tvTitle: AppCompatTextView

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
        inputMaxLength = 1,
    )

    private val inputPlayers = InputObject(
        inputType = InputType.TYPE_CLASS_TEXT,
        inputHint = "",
        infoStr = "",
        inputMaxLength = 10,
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        _binding = FragmentGamingInputBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyViewBinding()
        tvTitle.text = getString(R.string.app_name)

        btnContinue.setOnClickListener(this)

        Util.viewApplyVis(btnContinue, View.INVISIBLE)
        inputNumbers.clearEditTextForNewInput()

        sharedViewModel.playerCount.observe(this, {
            playerCount = it
        })

        addInputTextWatcher()
    }

    private fun addInputTextWatcher() {
        etInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
               // Util.setViewVisibilityFadeInOut(view = btnContinue, visible = bVisible)
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                bVisible = false
                charSequence.checkInputValidity()
            }

            override fun afterTextChanged(p0: Editable?) {
                Util.setViewVisibilityFadeInOut(view = btnContinue, visible = bVisible)
            }
        })
    }

    private fun CharSequence?.checkInputValidity() {

        val str = this
        val b = this.toString().isNotEmpty()

        if (b) {
            when (etInput.inputType) {
                InputType.TYPE_CLASS_TEXT -> {
                    bVisible = str.checkValidTextLength()
                }
                InputType.TYPE_CLASS_NUMBER -> {
                    bVisible = str.checkValidNumber()
                }
            }

        }
    }

    private fun CharSequence?.checkValidNumber(): Boolean =
        try {
            when (val parseNum = Integer.parseInt(this.toString())) {
                in playerAmountNumRange -> {
//                    Log.d("!", "CORRECT NUMBER $parseNum")
                    true
                }
                else -> {
//                    Log.d("!", "INCORRECT - NUMBER NOT IN RANGE $parseNum")
                    false
                }
            }
        } catch (e: Exception) {
//            Log.d("!", "checkValidNumber: $e")
            false
        }

    private fun CharSequence?.checkValidTextLength(): Boolean =
        try {
            when (val length = this.toString().length) {
                in playerNameMinMaxLength -> {
//                    Log.d("!", "VALID $length TEXT LENGTH")
                    true
                }
                else -> {
//                    Log.d("!", "INVALID $length TEXT LENGTH")
                    false
                }
            }
        } catch (e: Exception) {
//            Log.d("!", "checkValidTextLength: $e")
            false
        }

    private fun applyViewBinding() {
        binding.apply {

            /*TEXT-VIEWS*/
            tvTitle = textViewAppTitle

            /*EDIT-TEXTS*/
            tilInput = textInputLayoutInput
            etInput = editTextInput

            /*BUTTONS*/
            btnContinue = buttonContinue

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_continue -> {
                doNext()
            }
        }
    }

    private fun doNext() {

        when (counter) {
            0 -> dnPrepareForPlayerInput()
            in 1 until playerCount -> dnPrepareForNextPlayer()
            playerCount -> dnPrepareGameStart()
            playerCount.plus(1) -> moveToNextFragment()
        }
    }

    private fun dnPrepareForPlayerInput() {
        setPlayerCount()
        increaseCount()
        inputObjectUpdate()
        inputPlayers.clearEditTextForNewInput()
        tilInput.slideOutRightSlideInLeft().start()
        btnContinue.btnSetText(getString(R.string.add_player))
        etInput.setText("Player $counter") //TODO QUICK TESTING - REMOVE LINE LATER
    }

    private fun dnPrepareGameStart() {
        Player(name = etInput.text.toString(), playerNum = counter).addAdditionalPlayer()
        increaseCount()
        Util.viewApplyVis(tilInput, View.INVISIBLE)
        btnContinue.btnSetText(getString(R.string.start_game))
        requireActivity().hideSoftKeyBoard(btnContinue)
    }

    private fun dnPrepareForNextPlayer() {
        Player(name = etInput.text.toString(), playerNum = counter).addAdditionalPlayer()
        increaseCount()
        inputObjectUpdate()
        tilInput.slideOutRightSlideInLeft().start()
        inputPlayers.clearEditTextForNewInput()
        etInput.setText("Player $counter") //TODO QUICK TESTING - REMOVE LINE LATER
    }

    private fun inputObjectUpdate() = inputPlayers.includeCounterValue(count = counter)
    private fun moveToNextFragment() = sharedViewModel.updateFragmentPos()
    private fun setPlayerCount() = sharedViewModel.setPlayerCount(Integer.parseInt(etInput.text.toString()))
    private fun Player.addAdditionalPlayer() = sharedViewModel.addPlayerToList(player = this)
    private fun AppCompatButton.btnSetText(text: String) { this.text = text }
    private fun increaseCount() = counter++

    private fun InputObject.clearEditTextForNewInput() {

        val ipo = this

        ipo.apply {
            tilInput.apply {
                hint = ipo.inputHint
                helperText = ipo.infoStr
                counterMaxLength = inputMaxLength
            }

            etInput.apply {
                inputType = ipo.inputType
                filters = arrayOf(InputFilter.LengthFilter(inputMaxLength))
                setText("")
            }
            etInput.requestFocus()
        }
    }

    class InputObject(
        var inputType: Int,
        var inputHint: String,
        var infoStr: String,
        val inputMaxLength: Int) {

        private val strEnterName = "Enter the name of Player"
        private val hintPlayer = "Player"

        fun includeCounterValue(count: Int) {
            inputHint = "$hintPlayer $count"
            infoStr = "$strEnterName $count"
        }
    }

}