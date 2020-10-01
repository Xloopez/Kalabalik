package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
    private lateinit var tvPlusSign: AppCompatTextView

    private lateinit var viewList: MutableList<TextView>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //sharedViewModel =  ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        gamingViewModel =  ViewModelProvider(requireActivity()).get(GamingViewModel::class.java) // SCOPE TO ACTIVITY? MAYBE..
        _binding = FragmentCardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyViewBinding()
        viewList = mutableListOf(tvCardType, tvCon1, tvCon1Points, tvPlusSign)

        gamingViewModel.clearCardFragment.observe(this, {
//            Log.d("!", "ClearCardFragment $it")
            clearViews()
        })

        gamingViewModel.updateCardFragment.observe(this, {

            when (gamingViewModel.currentCardType.value) {
                EnumUtil.EnRandom.CONSEQUENCES -> {
                    //TODO USE setTextFromList() here
                    tvCon1.text = gamingViewModel.consequencePair.value?.first
                    tvCon1Points.text = "${gamingViewModel.consequencePair.value?.second}"
                    tvCardType.text = gamingViewModel.currentCardType.value?.getEnumString()
                    tvPlusSign.text = "+"
                }
                EnumUtil.EnRandom.MISSION -> {

                    mutableListOf(
                        Pair(tvCon1, gamingViewModel.missionPair.value?.first),
                        Pair(tvCon1Points, "${gamingViewModel.missionPair.value?.second}"),
                        Pair(tvCardType, gamingViewModel.currentCardType.value?.getEnumString()),
                        Pair(tvPlusSign, "+")
                    ).setTextFromList()

//                    tvCon1.text = gamingViewModel.missionPair.value?.first
//                    tvCon1Points.text = "${gamingViewModel.missionPair.value?.second}"
//                    tvCardType.text = gamingViewModel.currentCardType.value?.getEnumString()
//                    tvPlusSign.text = "+"
                }
            }

        })

    }

    private fun clearViews() { viewList.forEach { it.text = "" } }

    private fun applyViewBinding(){

        binding.apply {
            tvCardType = textViewCardType
            tvCon1 = textViewConOne
            tvCon1Points = textViewConOnePoints
            tvPlusSign = textViewPlusSign
        }
    }

    private fun MutableList<Pair<AppCompatTextView, String?>>.setTextFromList(){
        this.forEach { pair -> pair.first.text = pair.second  }
    }

    private fun TextView.sTxt(sText: CharSequence?) { this.text = sText }

    override fun onResume() {
        super.onResume()
        clearViews()
    }

}