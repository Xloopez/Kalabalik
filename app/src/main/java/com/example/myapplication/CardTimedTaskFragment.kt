package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.viewmodels.GamingViewModel

class CardTimedTaskFragment : Fragment() {

	private lateinit var gamingViewModel: GamingViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		gamingViewModel = ViewModelProvider(requireActivity()).get(GamingViewModel::class.java)
		return inflater.inflate(R.layout.fragment_timed_task_card, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val v = view.findViewById<AppCompatTextView>(R.id.textViewTest)

		StringBuilder().apply {
			val task = gamingViewModel.timedTaskCard.value!!
			append(task.task)
			appendLine()
			append(task.consequence)
		}.let {
			v.text = it.toString()
		}

	}
}