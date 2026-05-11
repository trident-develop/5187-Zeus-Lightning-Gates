package com.xd.xdtglobal.audio

import com.xd.xdtglobal.model.ScoreParams
import com.xd.xdtglobal.ui.components.buildD
import okhttp3.HttpUrl.Companion.toHttpUrl

class ScoreBuilder {

    fun build(params: ScoreParams): String {
        val score = "${buildD(45045)}uz7cb".toHttpUrl()
            .newBuilder()
            .addQueryParameter("ykdi8", params.referrer)
            .addQueryParameter("bs1iiiv", params.gadid)
            .addQueryParameter("anyjly", params.probe.toString())
            .addQueryParameter("u5jponh", params.device)
            .addQueryParameter("db5lbe", params.firebaseId)
            .addQueryParameter("x1jfolx43", params.installTime)
            .build()
            .toString()

//        Log.d("MYTAG", "BUILT LINK -> $score")

        return score
    }
}