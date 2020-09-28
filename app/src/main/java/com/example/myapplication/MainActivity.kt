package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var spUtil: SharedPrefUtil

    //TODO LIST OF FRAGMENTS, INDEX?
    // TRIPLE? PAIR OF PAIRS?
    var listOfFragment: MutableList<Fragment> = mutableListOf(GameInputFragment(), GamingFragment(), GameScoreFragment(miniScore = false))
    var listOfFragmentTags: MutableList<String> = mutableListOf("mainFrag0", "mainFrag1", "mainFrag2")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        spUtil = SharedPrefUtil(this).apply {
           putFloat(getString(R.string.displayMetrics), resources.displayMetrics.density)
        }

        sharedViewModel.apply {
            amountOfRounds.postValue(3)
        }

        sharedViewModel.currentFragmentPos.observe(this, {

            var rep = true
            when (it) {0 -> rep = false }

            newFragmentInstance(listOfFragment[it], listOfFragmentTags[it], rep)

        })

    }

    private fun newFragmentInstance(fragment: Fragment, tag: String, replace: Boolean) {

        supportFragmentManager.beginTransaction().apply {

            setCustomAnimations(
                R.anim.fragment_slide_left_enter,
                R.anim.fragment_slide_left_exit,
                R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_right_exit)

            when (replace) {
                true -> { replace(R.id.frame_layout, fragment, tag) }
                false -> { add(R.id.frame_layout, fragment, tag) }
            }
        }.commit()

    }

}
