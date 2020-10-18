package com.example.myapplication.gaming

import android.content.Context
import android.content.res.Resources
import com.example.myapplication.EnRandom
import com.example.myapplication.R
import com.example.myapplication.getRandomListIndex

class GeneratorMissionOrConsequence(val context: Context) {

	private val res: Resources = context.resources

	private val listOfConsequences = res.getStringArray(R.array.Consequences).toMutableList()
	private val listOfMissions = res.getStringArray(R.array.Mission).toMutableList()
	private val listOfConsequencesPoints =
		res.getIntArray(R.array.ConsequencesPoints).toMutableList()
	private val listOfMissionsPoints = res.getIntArray(R.array.MissionPoints).toMutableList()

	fun generateNewCard(): CardMissionConsequence {

		val rIndex: Int
		val cardText: String
		val cardPoints: Double
		val cardType: EnRandom

		return when (val rCardType = EnRandom.values().random()) {
			EnRandom.CONSEQUENCES -> {

				rIndex = listOfConsequences.getRandomListIndex()

				rIndex.also {
					cardText = listOfConsequences[it]
					cardPoints = listOfConsequencesPoints[it].toDouble()
					listOfConsequences.removeAt(it)
					listOfConsequencesPoints.removeAt(it)
				}
				cardType = rCardType
				CardMissionConsequence(cardText, cardPoints, cardType)
			}
			EnRandom.MISSION -> {

				rIndex = listOfMissions.getRandomListIndex()

				rIndex.also {
					cardText = listOfMissions[it]
					cardPoints = listOfMissionsPoints[it].toDouble()
					listOfMissions.removeAt(it)
					listOfMissionsPoints.removeAt(it)
				}
				cardType = rCardType
				CardMissionConsequence(cardText, cardPoints, cardType)
			}
		}
	}
}