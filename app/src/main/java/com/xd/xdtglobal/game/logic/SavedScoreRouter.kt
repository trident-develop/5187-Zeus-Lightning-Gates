package com.xd.xdtglobal.game.logic

import com.xd.xdtglobal.event.StartDestination
import com.xd.xdtglobal.ui.components.buildD

class SavedScoreRouter {

    fun score(savedScore: String): StartDestination {
        return when {
            !savedScore.startsWith(buildD(45045)) -> {
                StartDestination.OpenSavedScoreTypeA(savedScore)
            }

            savedScore.startsWith(buildD(45045)) -> {
                StartDestination.OpenSavedScoreTypeB(savedScore)
            }

            else -> {
                StartDestination.OpenGame
            }
        }
    }
}