package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentCardBinding

class CardFragment: Fragment() {

    private var _binding: FragmentCardBinding? = null
    private val binding get() = _binding!!

    lateinit var gamingViewModel: GamingViewModel

    private lateinit var tvCardType: AppCompatTextView
    private lateinit var tvCon1: AppCompatTextView
    private lateinit var tvCon1Points: AppCompatTextView

    private lateinit var viewList: MutableList<*>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //sharedViewModel =  ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        gamingViewModel =  ViewModelProvider(requireActivity()).get(GamingViewModel::class.java) // SCOPE TO ACTIVITY? MAYBE..
        _binding = FragmentCardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyViewBinding()

        tvCardType.text = ""
        tvCon1.text = ""
        tvCon1Points.text = ""


        gamingViewModel.clearCardFragment.observe(this, {
            //viewList.run { Util.hideAllViews(this) }
            tvCardType.text = ""
            tvCon1.text = ""
            tvCon1Points.text = ""

        })

        gamingViewModel.updateCardFragment.observe(this, {

            tvCardType.text = gamingViewModel.currentCardType.value?.getEnumString()

            Log.d("!", "${gamingViewModel.currentCardType.value?.getEnumString()}")

            when (gamingViewModel.currentCardType.value) {
                EnumUtil.EnRandom.CONSEQUENCES -> {
                    tvCon1.text = gamingViewModel.consequencePair.value?.first
                    tvCon1Points.text = "${gamingViewModel.consequencePair.value?.second}"
                }
                EnumUtil.EnRandom.MISSION -> {
                    tvCon1.text = gamingViewModel.missionPair.value?.first
                    tvCon1Points.text = "${gamingViewModel.missionPair.value?.second}"
                }
            }

        })

    }

    private fun applyViewBinding(){

        binding.apply {
            tvCardType = textViewCardType
            tvCon1 = textViewConOne
            tvCon1Points = textViewConOnePoints
        }
    }

}