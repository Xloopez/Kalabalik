package com.example.kalabalik

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class InstructionFragment : Fragment() {

    lateinit var instructionScreen: ConstraintLayout
    lateinit var toastText: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_instruction, container, false)

        //requireActivity().findViewById(R.id.context_id)
        Snackbar.make(requireActivity().findViewById(R.id.playerFragmentContainer), "Nicolinaaaaa", Snackbar.LENGTH_INDEFINITE).show()

        //Toast.makeText(requireActivity(),"Klicka skärmmmmmmmm",Toast.LENGTH_LONG).show()

        /*activity.let {

            Toast.makeText(it,"Klicka skärmmmmmmmm",Toast.LENGTH_LONG).show()
        }*/

    }
}