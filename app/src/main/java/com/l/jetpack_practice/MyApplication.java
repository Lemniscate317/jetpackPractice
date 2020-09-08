package com.l.jetpack_practice;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        AppLifecycleOwner owner = new AppLifecycleOwner();
        owner.init(this);

        owner.addObserver(new AppLifecycleObserver() {

            @Override
            public void onAppStateChanged(boolean isFront) {
                Log.e("application", isFront + "");
            }
        });
    }


    abstract class AppLifecycleObserver implements LifecycleEventObserver {
        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            if (event == Lifecycle.Event.ON_START) {
                onAppStateChanged(true);
            } else if (event == Lifecycle.Event.ON_STOP) {
                onAppStateChanged(false);
            }
        }

        public abstract void onAppStateChanged(boolean isFront);
    }

    class AppLifecycleOwner implements LifecycleOwner {
        LifecycleRegistry registry = new LifecycleRegistry(this);


        private int activityCount = 0;
        private boolean isFront = false;


        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return registry;
        }

        public void addObserver(AppLifecycleObserver observer) {
            registry.addObserver(observer);
        }


        public void init(Application application) {

            application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

                }

                @Override
                public void onActivityStarted(@NonNull Activity activity) {
                    activityCount++;

                    if (activityCount > 0 && !isFront) {
                        registry.handleLifecycleEvent(Lifecycle.Event.ON_START);
                        isFront = true;
                    }
                }

                @Override
                public void onActivityResumed(@NonNull Activity activity) {

                }

                @Override
                public void onActivityPaused(@NonNull Activity activity) {

                }

                @Override
                public void onActivityStopped(@NonNull Activity activity) {
                    activityCount--;

                    if (activityCount <= 0 && isFront) {
                        registry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
                        isFront = false;
                    }
                }

                @Override
                public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(@NonNull Activity activity) {

                }
            });
        }
    }
}
