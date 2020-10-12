package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentTimedTaskCardBinding
import com.example.myapplication.viewmodels.GamingViewModel

class CardTimedTaskFragment : Fragment() {

	private lateinit var gamingViewModel: GamingViewModel
	private var _binding: FragmentTimedTaskCardBinding? = null
	private val binding get() = _binding!!

	private lateinit var tvTest: AppCompatTextView

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		gamingViewModel = ViewModelProvider(requireActivity()).get(GamingViewModel::class.java)
		_binding = FragmentTimedTaskCardBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		applyViewBinding()
		tvTest.text = buildCardText()

	}

	private fun buildCardText(): String {
		return StringBuilder().apply {
			val task = gamingViewModel.timedTaskCard.value!!
			append(task.task)
			appendLine()
			append(task.consequence)
		}.toString()
	}

	private fun applyViewBinding() {
		binding.apply {
			tvTest = textViewTest
		}
	}
}