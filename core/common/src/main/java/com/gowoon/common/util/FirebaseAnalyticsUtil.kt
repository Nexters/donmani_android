package com.gowoon.common.util

import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import io.github.aakira.napier.Napier

object FirebaseAnalyticsUtil {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun initialize() {
        Napier.d("gowoon firebase initialized")
        firebaseAnalytics = Firebase.analytics
    }

    fun sendEvent(
        trigger: EventTrigger,
        eventName: String,
        params: Bundle? = null
    ) {
        Napier.d("gowoon firebase send event $trigger, $eventName, $params")
        firebaseAnalytics.logEvent("${trigger.prefix}_${eventName}", params)
    }

    fun sendEvent(
        trigger: EventTrigger,
        eventName: String,
        vararg params: Pair<String, String>
    ) {
        val bundle = Bundle().apply {
            params.forEach {
                putString(it.first, it.second)
            }
        }
        sendEvent(trigger, eventName, bundle)
    }

    fun sendEvent(
        trigger: EventTrigger,
        eventName: String,
        params: List<Pair<String, String>>
    ) {
        val bundle = Bundle().apply {
            params.forEach {
                putString(it.first, it.second)
            }
        }
        sendEvent(trigger, eventName, bundle)
    }

    enum class EventTrigger(val prefix: String) {
        OPEN("O"),
        CLICK("C"),
        VIEW("V"),
        IMPRESSION("I"),
        SUBMIT("S"),
        RECEIVE("R")
    }
}