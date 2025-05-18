package com.gowoon.donmani_android.notification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.gowoon.common.util.FirebaseAnalyticsUtil
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.user.RegisterFCMTokenUseCase
import dagger.hilt.android.AndroidEntryPoint
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DonmaniFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var registerFCMTokenUseCase: RegisterFCMTokenUseCase

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Napier.d("gowoon on new token")
        CoroutineScope(Dispatchers.IO).launch {
            if (registerFCMTokenUseCase.invoke(token) is Result.Error) {
                // TODO error handling
                Napier.d("gowoon on new token")
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        FirebaseAnalyticsUtil.sendEvent(
            trigger = FirebaseAnalyticsUtil.EventTrigger.RECEIVE,
            eventName = "notification_receive"
        )
    }
}