package com.humanheima.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.humanheima.rxjavademo.network.NetWork;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class SecondActivity extends AppCompatActivity {

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

    public static String tag = "student";

    public void empty(View view) {
      /*  Course course = new Course();
       // Observable<String> observable = course.getLessonObservable();
        Observable<String> observable = course.valueObservable();
        course.setLesson("yuwen");
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                //输出为空
                Log.e("defer", s);
            }
        });*/

        Observable.empty().subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
                Log.e(tag, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(tag, "onError");
            }

            @Override
            public void onNext(Object o) {
                Log.e(tag, "onNext");
            }
        });
    }

    public void never(View view) {
        Observable.never().subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
                Log.e(tag, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(tag, "onError");
            }

            @Override
            public void onNext(Object o) {
                Log.e(tag, "onNext");
            }
        });
    }

    public void useError(View view) {
        Observable.error(new Throwable("使用error")).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
                Log.e(tag, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(tag, "onError");
            }

            @Override
            public void onNext(Object o) {
                Log.e(tag, "onNext");
            }
        });
    }

    public void useInterver(View view) {
        /**
         * interval操作符是每隔一段时间就产生一个数字，这些数字从0开始，一次递增1直至无穷大
         */
        Observable.interval(2, TimeUnit.SECONDS, Schedulers.io())
                .take(5)//最多输出5个
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.e(tag, aLong + "");
                    }
                });
    }

    public void useRange(View view) {
        Observable.just(1, 2, 3).repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Void> observable) {
                return observable.zipWith(observable.range(1, 3), new Func2<Void, Integer, Integer>() {
                    @Override
                    public Integer call(Void aVoid, Integer integer) {
                        return integer;
                    }
                }).flatMap(new Func1<Integer, Observable<?>>() {
                    @Override
                    public Observable<?> call(Integer integer) {
                        Log.e(tag, "delay repeat the " + integer + " count");
                        //1秒钟重复一次
                        return Observable.timer(1, TimeUnit.SECONDS);
                    }
                });
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.e(tag, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(tag, "onError");
            }

            @Override
            public void onNext(Integer integer) {
                Log.e(tag, integer + "");
            }
        });
    }

    public void useStart(View view) {

        Observable.just(1, 2, 4).startWith(Observable.just(9, 8, 7))
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.e(tag, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(tag, "onError");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(tag, integer + "");
                    }
                });
    }

    public void useTimer(View view) {

        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        Log.e(tag, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(tag, "onError");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.e(tag, aLong + "");
                    }
                });
    }

    public void useMap(View view) {

        Observable.just(1, 2, 3).map(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return integer + "string";
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String string) {
                Log.e(tag, string);
            }
        });
    }


}
