package com.wtw.whattheweather

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this,BuildConfig.KAKAO_MAP_KEY)
    }

}