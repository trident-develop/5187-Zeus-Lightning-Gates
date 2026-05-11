package com.xd.xdtglobal.game.logic

import com.xd.xdtglobal.audio.ScoreBuilder
import com.xd.xdtglobal.data.GameRepo
import com.xd.xdtglobal.event.StartDestination

class ResolveStartFlowUseCase(
    private val gameRepo: GameRepo,
    private val paramsCollector: ScoreParamsCollector,
    private val linkBuilder: ScoreBuilder,
    private val savedScoreRouter: SavedScoreRouter
) {

    suspend operator fun invoke(): StartDestination {
        val savedScore = gameRepo.getSavedScore()

        return if (savedScore.isNullOrBlank()) {
//            Log.d("MYTAG", "SAVED LINK IS EMPTY -> BUILD NEW LINK")

            val params = paramsCollector.collect()
            val builtLink = linkBuilder.build(params)

            StartDestination.BuiltScore(builtLink)

        } else {
//            Log.d("MYTAG", "SAVED LINK EXISTS -> $savedScore")

            savedScoreRouter.score(savedScore)
        }
    }
}