package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.gaming.GamingFragment
import com.example.myapplication.gaminginput.GameInputFragment
import com.example.myapplication.utilities.SharedPrefUtil

class MainActivity : AppCompatActivity() {

    private class TransactionFragment(var fragment: Fragment, @Nullable var fragmentTag: String, var replace: Boolean = true)

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var spUtil: SharedPrefUtil
    

    private val listOfFragment: MutableList<TransactionFragment> =
        mutableListOf(
            TransactionFragment(GameInputFragment(), "mainGameInputFragment", replace = false),
            TransactionFragment(GamingFragment(), "mainGameInputFragment"),
            TransactionFragment(
                GameScoreFragment(miniScore = EnScore.FINAL),
                "mainGameInputFragment"
            )
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(SharedViewModel::class.java)
        spUtil = SharedPrefUtil(this).apply {
            putFloat(
                getString(R.string.displayMetrics),
                resources.displayMetrics.density
            )
        }
        3
        sharedViewModel.currentFragmentPos.observe(this, {
            listOfFragment[it].apply { newFragmentInstance(fragment, fragmentTag, replace) }
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
                true -> {
                    replace(R.id.frame_layout, fragment, tag)
                    addToBackStack(tag)
                }
                false -> {
                    add(R.id.frame_layout, fragment, tag)
                }
            }

        }.commit()

    }

    override fun onBackPressed() {
        Log.d("!", "BACK PRESS IN..")

//        val fragment = supportFragmentManager.findFragmentById(R.id.frame_layout)
//
//
//        when ((fragment as? MainActivityOnBackPress)?.onBackPressed()) {
//            0 -> {
//                supportFragmentManager.popBackStack()
//            }
//            1 -> {
//                super.onBackPressed()
//            }
//        }
    }

//    interface MainActivityOnBackPress {
//        fun onBackPressed(): Int
//    }

}
