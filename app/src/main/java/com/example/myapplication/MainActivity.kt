package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        sharedViewModel.apply {
            amountOfRounds.postValue(3)
        }

        sharedViewModel.currentFragmentPos.observe(this, {
            when (it) {
                0 ->  newFragmentInstance(GameInputFragment(), "GameInput", false)
                1 ->  newFragmentInstance(GamingFragment(), "Gaming", true)
                2 ->  newFragmentInstance(GameScoreFragment(miniScore = false), "Score", true)
            }
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
