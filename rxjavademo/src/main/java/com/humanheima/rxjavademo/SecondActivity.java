package com.humanheima.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.humanheima.rxjavademo.network.NetWork;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class SecondActivity extends AppCompatActivity {
    public final static String tag = "tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public void useAndroidObservable() {

        Observable.zip(NetWork.getApi().getWeather("", ""), NetWork.getApi().getWeather("", ""), new Func2<String, String, Object>() {
            @Override
            public Object call(String s, String s2) {
                return null;
            }
        });
    }

    /**
     * Observable.flatMap()接收一个Observable的输出作为输入，同时输出另外一个Observable。
     * 实现把一个List集合转化成字符串
     */
    public void useFlatMap1() {
        NetWork.getApi().query("")
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> list) {
                        //把List转化成一个一个的字符串
                        return Observable.from(list);
                    }
                }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(tag, s);
            }
        });
    }

    public void useFlatMap2() {
        NetWork.getApi().query("")
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> list) {
                        return Observable.from(list);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String url) {
                        return NetWork.getApi().getTitle(url);
                    }
                })
                //过滤为null的值
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String title) {
                        return title != null;
                    }
                })
                .take(5)//最多输出5条
                //允许我们在每次输出一个元素之前做一些额外的事情，比如这里的保存标题。
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        //把标题保存起来
                        //saveTitle(s);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(tag, s);
                    }
                });
    }

    public void fun1() {
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io())//指定时间的产生在io线程
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return "string" + integer;
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())//遇到observeOn()从io线程切换到UI线程，之前的map操作也是在io线程,之后的subscribe()操作在UI线程
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e("tag", "UI线程打印" + s);
                    }
                });
    }


}
