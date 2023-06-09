package org.projekt.logintester;

import android.app.Application;
import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY);
    }
}