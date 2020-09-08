package com.l.jetpack_practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import android.os.Bundle;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

public class LifecycleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecycle);

        //2. 注册观察者,观察宿主生命周期状态变化
        LocationObserver1 observer = new LocationObserver1();
        getLifecycle().addObserver(observer);



    }

    private void log(String msg) {
        Log.e(this.getClass().getName(), msg);
    }


    //LifecycleEventObserver宿主生命周期事件封装成 Lifecycle.Event
    class LocationObserver2 implements LifecycleEventObserver {

        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            log(event.name());
        }
    }


    //1. 自定义的LifecycleObserver观察者，在对应方法上用注解声明想要观  察的宿主的生命周期事件即可
    class LocationObserver1 implements LifecycleObserver {
        //宿主执行了onstart时，会分发该事件
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        void onStart(@NotNull LifecycleOwner owner){
            log("onStart");
        }

        //宿主执行了onstop时 会分发该事件
        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        void onStop(@NotNull LifecycleOwner owner){
            log("onStop");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocationObserver2 observer2 = new LocationObserver2();
        getLifecycle().addObserver(observer2);
    }
}
