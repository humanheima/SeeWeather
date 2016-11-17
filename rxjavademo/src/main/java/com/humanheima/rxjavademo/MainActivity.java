package com.humanheima.rxjavademo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action4;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public final static String tag = "tag";
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //使用rxjava方式实现从欢迎页进入主界面.
        // delayStartAct();
    }

    /**
     * RxJava 的基本实现主要有三点：
     * 11) 创建 Observer 即观察者，它决定事件触发的时候将有怎样的行为
     * 除了 Observer 接口之外，RxJava 还内置了一个实现了 Observer 的抽象类：Subscriber。
     * Subscriber 对 Observer 接口进行了一些扩展，但他们的基本使用方式是完全一样的：
     * 2) 创建 Observable
     * Observable 即被观察者，它决定什么时候触发事件以及触发怎样的事件。
     * 3) Subscribe (订阅)
     * 创建了 Observable 和 Observer 之后，再用 subscribe() 方法将它们联结起来，整条链子就可以工作了
     */
    private void fun1() {

        //1 创建观察者的两种方式
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        };

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e("tag", s);
            }
        };

        //2 创建被观察者的几种方式
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();
            }
        });
        //just(T...): 将传入的参数依次发送出来。
        Observable observable1 = Observable.just("hello", "hi", "world");
        /**
         将会依次调用：
         onNext("Hello");
         onNext("Hi");
         onNext("world");
         onCompleted();
         */
        //from(T[]) / from(Iterable<? extends T>) : 将传入的数组或 Iterable 拆分成具体对象后，依次发送出来。
        String[] words = {"Hello", "Hi", "Aloha"};
        Observable observable2 = Observable.from(words);
        ArrayList<String> list = new ArrayList<>();
        list.add("hello");
        list.add("Hi");
        list.add("Aloha");
        Observable observable3 = Observable.from(list);
        /**
         * 将会依次调用：
         onNext("Hello");
         onNext("Hi");
         onNext("Aloha");
         onCompleted();
         */

        //3 Subscribe (订阅)
        observable.subscribe(subscriber);
        observable1.subscribe(subscriber);
        observable2.subscribe(subscriber);
        observable3.subscribe(subscriber);

        //subscribe() 还支持不完整定义的回调
        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(tag, s);
            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(tag, throwable.getMessage());
            }
        };
        Action0 onCompletedAction = new Action0() {
            @Override
            public void call() {
                Log.e(tag, "onCompletedAction");
            }
        };
        Action4<String, String, Integer, List<String>> action4 = new Action4<String, String, Integer, List<String>>() {
            @Override
            public void call(String s, String s2, Integer integer, List<String> list) {

            }
        };
        // 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);

    }

    //打印字符串
    public void example1() {
        ArrayList<String> list = new ArrayList<>();
        list.add("hello");
        list.add("Hi");
        list.add("Aloha");
        Observable.from(list)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(tag, s);
                    }
                });
    }

    //由id取得图片并显示
    public void example2() {

        final int resId = R.mipmap.ic_launcher;
        final ImageView imageView = new ImageView(this);
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {

                Drawable drawable = getResources().getDrawable(resId);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribe(new Observer<Drawable>() {
            @Override
            public void onCompleted() {
                Log.e(tag, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(tag, e.getMessage());
            }

            @Override
            public void onNext(Drawable drawable) {
                imageView.setImageDrawable(drawable);
            }
        });
    }

    /*****以上的例子都是在同一个线程（主线程中操作的，并不实用）******************************************************************/
    /**
     * 线程控制 —— Scheduler
     * 在不指定线程的情况下， RxJava 遵循的是线程不变的原则，
     * 即：在哪个线程调用 subscribe()，就在哪个线程生产事件；在哪个线程生产事件，就在哪个线程消费事件。
     * 如果需要切换线程，就需要用到 Scheduler （调度器）。
     * 在RxJava 中，Scheduler ——调度器，相当于线程控制器，RxJava 通过它来指定每一段代码应该运行在什么样的线程。
     * RxJava 已经内置了几个 Scheduler ，它们已经适合大多数的使用场景：
     * Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
     * Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
     * Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，
     * 区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，
     * 因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。
     * Schedulers.computation(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。
     * 这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
     * 另外， Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行。
     * 有了这几个 Scheduler ，就可以使用 subscribeOn() 和 observeOn() 两个方法来对线程进行控制了。
     * subscribeOn(): 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。
     * * observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
     */

    public void example3() {
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e(tag, integer.toString());
                    }
                });
        /**
         * 上面这段代码中，由于 subscribeOn(Schedulers.io()) 的指定，被创建的事件的内容 1、2、3、4 将会在 IO 线程发出；
         * 而由于 observeOn(AndroidScheculers.mainThread()) 的指定，因此 subscriber 数字的打印将发生在主线程 。
         * 事实上，这种在 subscribe() 之前写上两句 subscribeOn(Scheduler.io()) 和 observeOn(AndroidSchedulers.mainThread()) 的使用方式非常常见，
         * 它适用于多数的 『后台线程取数据，主线程显示』的程序策略。
         */

        /**
         * 而前面提到的由图片 id 取得图片并显示的例子，如果也加上这两句：
         * 那么，加载图片将会发生在 IO 线程，而设置图片则被设定在了主线程。
         * 这就意味着，即使加载图片耗费了几十甚至几百毫秒的时间，也不会造成丝毫界面的卡顿。
         */
        final int resId = R.mipmap.ic_launcher;
        final ImageView imageView = new ImageView(this);
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {

                Drawable drawable = getResources().getDrawable(resId);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribe(new Observer<Drawable>() {
            @Override
            public void onCompleted() {
                Log.e(tag, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(tag, e.getMessage());
            }

            @Override
            public void onNext(Drawable drawable) {
                imageView.setImageDrawable(drawable);
            }
        });
    }

    /**
     * 所谓变换，就是将事件序列中的对象或整个序列进行加工处理，转换成不同的事件或事件序列
     */
    public void example4() {
        //一个map的例子
        // map() 方法将参数中的 String 对象转换成一个 Bitmap 对象后返回，
        // 而在经过 map() 方法后，事件的参数类型也由 String 转为了 Bitmap。
        final ImageView imageView = new ImageView(this);
        Observable.just("images/logo.png")// 输入类型 String
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String filePath) {
                        return getBitMapFromPath(filePath);
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }
                });

    }

    /**
     * 获取图片
     *
     * @param filePath
     * @return
     */
    private Bitmap getBitMapFromPath(String filePath) {
        return null;
    }


    public void example5() {
        /**
         *  flatMap(): 这是一个很有用但非常难理解的变换，
         *  因此我决定花多些篇幅来介绍它。 首先假设这么一种需求：
         *  假设有一个数据结构『学生』，现在需要打印出一组学生的名字。实现方式很简单：
         Student[] students = ...;
         */
        List<Student> students = new ArrayList<>();
        students.add(new Student());
        students.add(new Student());
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String name) {
                Log.e(tag, name);
            }
        };
        Observable.from(students)
                .map(new Func1<Student, String>() {
                    @Override
                    public String call(Student student) {
                        return student.getName();
                    }
                }).subscribe(subscriber);

        /**
         * 那么再假设：如果要打印出每个学生所需要修的所有课程的名称呢？（
         * 需求的区别在于，每个学生只有一个名字，但却有多个课程。）首先可以这样实现：
         */
        Subscriber<Student> subscriber1 = new Subscriber<Student>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Student student) {
                List<Course> courseList = student.getCourseList();
                for (Course course : courseList) {
                    Log.e(tag, course.getLesson());
                }
            }
        };

        Observable.from(students).subscribe(subscriber1);

        /**
         * 那么如果我不想在 Subscriber 中使用 for 循环，
         * 而是希望 Subscriber 中直接传入单个的 Course 对象呢（这对于代码复用很重要）？
         * 用 map() 显然是不行的，因为 map() 是一对一的转化，而我现在的要求是一对多的转化。
         * 那怎么才能把一个 Student 转化成多个 Course 呢？
         *这个时候，就需要用 flatMap() 了：
         */

        Subscriber<Course> subscriber2 = new Subscriber<Course>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Course course) {
                Log.e(tag, course.getLesson());
            }
        };

        Observable.from(students)
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        return Observable.from(student.getCourseList());
                    }
                }).subscribe(subscriber2);

        /**
         *  flatMap() 的原理是这样的：
         *  1. 使用传入的事件对象创建一个 Observable 对象；
         *  2. 并不发送这个 Observable, 而是将它激活，于是它开始发送事件；
         *  3. 每一个创建出来的 Observable 发送的事件，都被汇入同一个 Observable ，
         *  而这个 Observable 负责将这些事件统一交给 Subscriber 的回调方法。
         *  这三个步骤，把事件拆成了两级，
         *  通过一组新创建的 Observable 将初始的对象『铺平』之后通过统一路径分发了下去
         *
         *  下面实践一把
         */
    }

    public void useFlatMap(View view) {
        List<Student> students = new ArrayList<>();
        Course course = new Course("语文");
        Course course1 = new Course("数学");
        List<Course> list = new ArrayList<>();
        list.add(course);
        list.add(course1);
        Student stu1 = new Student();
        stu1.setName("homgin");
        stu1.setCourseList(list);
        Student stu2 = new Student();
        stu2.setName("dumingwei");
        stu2.setCourseList(list);
        students.add(stu1);
        students.add(stu2);

        Subscriber<Course> subscriber = new Subscriber<Course>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Course course) {
                Log.e(tag, course.getLesson());
            }
        };

        Observable.from(students)
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        return Observable.from(student.getCourseList());
                    }
                }).subscribe(subscriber);

    }


    public void useCompose(View view) {


        /**
         * compose: 对 Observable 整体的变换
         * Observable 还有一个变换方法叫做 compose(Transformer)。
         * compose() 是针对 Observable 自身进行变换。
         * 举个例子，假设在程序中有多个 Observable ，并且他们都需要应用一组相同的 lift() 变换。你可以这么写：
         * observable1
         .lift1()
         .lift2()
         .lift3()
         .lift4()
         .subscribe(subscriber1);
         observable2
         .lift1()
         .lift2()
         .lift3()
         .lift4()
         .subscribe(subscriber2);

         */

        /**
         * 你觉得这样太不软件工程了，这个时候，就应该用 compose() 来解决了
         */
        LiftAllTransformer transformer = new LiftAllTransformer();
        Observable<Integer> observable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
                subscriber.onCompleted();
            }
        });

        observable.compose(transformer).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e(tag, s);
            }
        });

    }

    /**
     * 5. 线程控制：Scheduler (二)
     * 除了灵活的变换，RxJava 另一个牛逼的地方，就是线程的自由控制。
     * observeOn() 指定的是它之后的操作所在的线程。
     * 因此如果有多次切换线程的需求，只要在每个想要切换线程的位置调用一次 observeOn()
     */

    public void changeScheduler(View view) {
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e(tag, "主线程" + s);
            }
        };
        Observable.just("hello", "world", "hi")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return "新线程1" + s;
                    }
                }).observeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return "新线程2" + s;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * doOnSubscribe执行的线程不是subscriber执行所在的线程
     * 默认情况下， doOnSubscribe() 执行在 subscribe() 发生的线程；而如果在 doOnSubscribe() 之后有 subscribeOn() 的话，
     * 它将执行在离它最近的 subscribeOn() 所指定的线程。
     * 下面的的代码如果运行注释的代码会发现progressBar不能显示，因为subscribe()所发生的线程是一个new Thread 不是主线程
     *
     * @param view
     */
    public void useDoOnSubscribe(View view) {
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        progressBar.setVisibility(View.VISIBLE);//需要在主线程执行
                        Log.e(tag, "doOnSubscribe ,thread id" + Thread.currentThread().getId() + ",thread name" + Thread.currentThread().getName());
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(tag, e.getMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(tag, "onNext" + integer + ",thread id" + Thread.currentThread().getId() + ",thread name" + Thread.currentThread().getName());
                    }
                });
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                Observable.just(1, 2, 3, 4)
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                progressBar.setVisibility(View.VISIBLE);//需要在主线程执行
                                Log.e(tag, "doOnSubscribe ,thread id" + Thread.currentThread().getId() + ",thread name" + Thread.currentThread().getName());
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Integer>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(tag, e.getMessage());
                            }

                            @Override
                            public void onNext(Integer integer) {
                                Log.e(tag, "onNext" + integer + ",thread id" + Thread.currentThread().getId() + ",thread name" + Thread.currentThread().getName());
                            }
                        });
            }
        }).start();*/
    }

    /**
     * defer 操作符，just操作符是在创建Observable就进行了赋值操作，而defer是在订阅者订阅时才创建Observable，此时才进行真正的赋值操作
     */

    //初始的时候的时候i=10，有瑕疵
    int i = 10;

    public void compareJustAndDefer(View view) {
        Observable<Integer> justObservable = Observable.just(i);
        i = 12;
        Observable<Integer> deferObservable = Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                return Observable.just(i);
            }
        });
        i = 15;
        justObservable.subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.e(tag, "justObservable i=" + i);
            }
        });
        deferObservable.subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.e(tag, "deferObservable i=" + i);
            }
        });
    }

    /**
     * timer 操作符
     * app 从欢迎页，2-3秒后自动跳转到主页面
     */
    public void delayStartAct() {
        Observable.timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .map(new Func1<Long, Object>() {
                    @Override
                    public Object call(Long aLong) {
                        startActivity(new Intent(MainActivity.this, SecondActivity.class));
                        finish();
                        return null;
                    }
                }).subscribe();
    }


    private Observable<ImageView> loadImgFromLocal() {
        return null;
    }

    private Observable<ImageView> loadImgFromNet() {
        return null;
    }

    /**
     * 使用timer操作符,两种用法，一种是延迟产生一个数字就结束，
     *
     * @param view
     */
    public void useTimer(View view) {
        //延迟产生一个数字就结束
       /* Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.e(tag, "timer" + aLong);
                    }
                });
        /**
         * interval操作符是每隔一段时间就产生一个数字，这些数字从0开始，一次递增1直至无穷大
         */
        Observable.interval(2, 2, TimeUnit.SECONDS, Schedulers.io())
                .take(5)//最多输出5个
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.e(tag, "timer" + aLong);
                    }
                });
    }

    /**
     * range操作符
     * range操作符是创建一组在从n开始，个数为m的连续数字，比如range(3,10)，就是创建3、4、5…12的一组数字，
     *
     * @param view
     */
    public void useRange(View view) {
        Observable.range(3, 10, Schedulers.io())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e(tag, "timer" + integer);
                    }
                });
    }

    /**
     * 使用repeat 和repeatWhen操作符
     *
     * @param view
     */
    public void useRepeat(View view) {
       /* Observable.range(3, 3).repeat(2).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(tag, "timer" + integer);
            }
        });*/

        Observable.range(3, 3).repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Void> observable) {
                return observable.zipWith(Observable.range(1, 3), new Func2<Void, Integer, Integer>() {
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

            }

            @Override
            public void onNext(Integer integer) {
                Log.e(tag, "onNext" + integer);
            }
        });
    }

    /**
     * buffer操作符周期性地收集源Observable产生的结果到列表中，并把这个列表提交给订阅者，
     * 订阅者处理后，清空buffer列表，同时接收下一次收集的结果并提交给订阅者，周而复始。
     */
    int num = 1;

    public void useBuffer(View view) {

        //定义邮件内容
        final String[] mails = new String[]{"Here is an email!", "Another email!", "Yet another email!"};
        //每隔1秒就随机发布一封邮件
        Observable<String> endlessMail = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                Random ran = new Random();
                while (true) {
                    String mail = mails[ran.nextInt(mails.length)];
                    subscriber.onNext(mail);
                    if (num == 8) {
                        subscriber.onError(new Throwable("故意出错"));
                    }
                    num++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }
                }

            }
        }).subscribeOn(Schedulers.io());
        //把上面产生的邮件内容缓存到列表中，并每隔3秒通知订阅者
        endlessMail.buffer(3, TimeUnit.SECONDS)
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(tag, e.getMessage());
                    }

                    @Override
                    public void onNext(List<String> list) {
                        Log.e(tag, String.format("You've got %d new messages!  Here they are!", list.size()));
                        for (int i = 0; i < list.size(); i++)
                            Log.e(tag, "**" + list.get(i).toString());
                    }
                });
    }

    /**
     * concatMap操作符
     * cancatMap操作符与flatMap操作符类似，都是把Observable产生的结果转换成多个Observable，
     * 然后把这多个Observable“扁平化”成一个Observable，并依次提交产生的结果给订阅者。
     * 与flatMap操作符不同的是，concatMap操作符在处理产生的Observable时，
     * 采用的是“连接(concat)”的方式，而不是“合并(merge)”的方式，这就能保证产生结果的顺序性，
     * 也就是说提交给订阅者的结果是按照顺序提交的，不会存在交叉的情况。
     *
     * @param f
     * @return
     */
    private Observable<File> listFiles(File f) {
       /* if (f.isDirectory()) {
            return Observable.from(f.listFiles())
                    .flatMap(new Func1<File, Observable<File>>() {
                        @Override
                        public Observable<File> call(File file) {
                            return listFiles(file);
                        }
                    });
        } else {
            return Observable.just(f);
        }*/
        if (f.isDirectory()) {
            return Observable.from(f.listFiles()).concatMap(new Func1<File, Observable<? extends File>>() {
                @Override
                public Observable<? extends File> call(File file) {
                    return listFiles(file);
                }
            });
        } else {
            return Observable.just(f);
        }
    }

    public void groupBy(View view) {
        Observable.interval(1, TimeUnit.SECONDS).take(10).groupBy(new Func1<Long, Long>() {
            @Override
            public Long call(Long aLong) {
                return aLong % 3;
            }
        }).subscribe(new Action1<GroupedObservable<Long, Long>>() {
            @Override
            public void call(final GroupedObservable<Long, Long> result) {
                result.subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long value) {
                        Log.e(tag, "key:" + result.getKey() + ", value:" + value);
                    }
                });
            }
        });
    }

    /**
     * cast操作符
     * cast操作符类似于map操作符，不同的地方在于map操作符可以通过自定义规则，
     * 把一个值A1变成另一个值A2，A1和A2的类型可以一样也可以不一样；
     * 而cast操作符主要是做类型转换的，传入参数为类型class，
     * 如果源Observable产生的结果不能转成指定的class，则会抛出ClassCastException运行时异常。
     *
     * @param view
     */
    public void useCast(View view) {
        Observable.just(1, 2).cast(Integer.class).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {

            }
        });
    }

    /**
     * scan操作符
     * scan操作符通过遍历源Observable产生的结果，
     * 依次对每一个结果项按照指定规则进行运算，
     * 计算后的结果作为下一个迭代项参数，每一次迭代项都会把计算结果输出给订阅者。
     *
     * @param view
     */
    public void useScan(View view) {
        Observable.just(1, 2, 3, 4, 5)
                .scan(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer sum, Integer item) {
                        //参数sum就是上一次的计算结果
                        return sum + item;
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(tag, "Next: " + integer);
            }
        });
    }

    public void useWindow(View view) {
        Observable.interval(1, TimeUnit.SECONDS)
                .take(12)
                .window(3, TimeUnit.SECONDS)
                .subscribe(new Action1<Observable<Long>>() {
                    @Override
                    public void call(Observable<Long> observable) {
                        Log.e(tag, "subdivide begin......");
                        observable.subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                Log.e(tag, "Next:" + aLong);
                            }
                        });
                    }
                });
    }

    /**
     * debounce操作符对源Observable每产生一个结果后，如果在规定的间隔时间内没有别的结果产生，
     * 则把这个结果提交给订阅者处理，否则忽略该结果。
     * <p>
     * 值得注意的是，如果源Observable产生的最后一个结果后在规定的时间间隔内调用了onCompleted
     * ，那么通过debounce操作符也会把这个结果提交给订阅者。
     *
     * @param view
     */
    public void debounce(View view) {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if (subscriber.isUnsubscribed()) return;

                try {
                    for (int i = 1; i < 10; i++) {
                        subscriber.onNext(i);
                        Thread.sleep(i * 100);
                    }
                    subscriber.onCompleted();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.e(tag, "completed!");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(tag, "Next:" + integer);
                    }
                });
    }

    /**
     * distinct操作符对源Observable产生的结果进行过滤，把重复的结果过滤掉，只输出不重复的结果给订阅者
     *
     * @param view
     */
    public void distinct(View view) {
        Observable.just(1, 1, 22, 3, 3, 4)
                .distinct()
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.e(tag, "completed!");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        //输出1,22,3,4
                        Log.e(tag, "Next:" + integer);
                    }
                });
    }

    /**
     * elementAt操作符在源Observable产生的结果中，仅仅把指定索引的结果提交给订阅者
     *
     * @param view
     */
    public void elementAt(View view) {
        Observable.just(1, 2, 3, 4, 5)
                .elementAt(2)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.e(tag, "completed!");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        //输出3
                        Log.e(tag, "elementAt Next:" + integer);
                    }
                });
    }

    /**
     * filter操作符是对源Observable产生的结果按照指定条件进行过滤，
     * 只有满足条件的结果才会提交给订阅者
     *
     * @param view
     */
    public void filter(View view) {
        Observable.just(1, 2, 3, 4, 5)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer < 4;
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.e(tag, "completed!");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        //只输出1,2,3,
                        Log.e(tag, "filter Next:" + integer);
                    }
                });
    }

    /**
     * ofType操作符类似于filter操作符，区别在于ofType操作符是按照类型对结果进行过滤
     *
     * @param view
     */
    public void ofType(View view) {
        Observable.just("hello", 2F, 3L, true, 'c')
                .ofType(Float.class)
                .subscribe(new Subscriber<Float>() {
                    @Override
                    public void onCompleted() {
                        Log.e(tag, "ofType onCompleted:");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Float aFloat) {
                        Log.e(tag, "ofType Next:" + aFloat);
                    }
                });
    }

    /**
     * single操作符是对源Observable的结果进行判断，如果产生的结果满足指定条件的数量不为1(有且只有一个)
     * ，则抛出异常，否则把满足条件的结果提交给订阅者，
     *
     * @param view
     */
    public void single(View view) {
        Observable.just(1, 2, 3, 4, 5)
                .single(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 3;
                    }
                }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.e(tag, "single onCompleted:");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(tag, "single onError:" + e.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                Log.e(tag, "single Next:" + integer);
            }
        });
    }

    /**
     * ignoreElements操作符
     * ignoreElements操作符忽略所有源Observable产生的结果，只把Observable的onCompleted和onError事件通知给订阅者。
     * ignoreElements操作符适用于不太关心Observable产生的结果，
     * 只是在Observable结束时(onCompleted)或者出现错误时能够收到通知。
     *
     * @param view
     */
    public void ignoreElements(View view) {
        Observable.just(1, 2, 3, 4, 5)
                .ignoreElements()
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.e(tag, "Sequence complete.");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(tag, "ignoreElements Next:" + integer);
                    }
                });
    }

    /**
     * skip操作符
     * skip操作符针对源Observable产生的结果，跳过前面n个不进行处理，而把后面的结果提交给订阅者处理
     * 运行结果
     * Next: 4
     * Next: 5
     * Next: 6
     * Next: 7
     * Sequence complete.
     * skipLast操作符
     * skipLast操作符针对源Observable产生的结果，忽略Observable最后产生的n个结果，而把前面产生的结果提交给订阅者处理，
     * 值得注意的是，skipLast操作符提交满足条件的结果给订阅者是存在延迟效果的
     */

    public void skip(View view) {
        Observable.just(1, 2, 3, 4, 5, 6, 7).skip(3)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onNext(Integer item) {
                        System.out.println("Next: " + item);
                    }

                    @Override
                    public void onError(Throwable error) {
                        System.err.println("Error: " + error.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("Sequence complete.");
                    }
                });

    }

    public void combineLatest(View view) {
        //产生0,5,10,15,20数列
        Observable<Long> observable1 = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 5;
                    }
                }).take(5);

        //产生0,10,20,30,40数列
        Observable<Long> observable2 = Observable.interval(500, 1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 10;
                    }
                }).take(5);
        Observable.combineLatest(observable1, observable2, new Func2<Long, Long, Long>() {
            @Override
            public Long call(Long aLong, Long aLong2) {
                return aLong + aLong2;
            }
        }).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                Log.e(tag, "Sequence complete.");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(tag, "Error: " + e.getMessage());
            }

            @Override
            public void onNext(Long aLong) {
                Log.e(tag, "Next: " + aLong);
            }
        });

    }

    public void zip(View view) {
        Observable<Integer> observable1 = Observable.just(10, 20, 30);
        Observable<Integer> observable2 = Observable.just(4, 8, 12, 16);
        Observable.zip(observable1, observable2, new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.e(tag, "Sequence complete.");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(tag, "Error: " + e.getMessage());
            }

            @Override
            public void onNext(Integer value) {
                Log.e(tag, "zip Next:" + value);
            }
        });
    }

    public void test(View view) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 0; i < 10; i++) {
                    try {
                        subscriber.onNext("String" + i);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onCompleted();

            }
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.e("test", s);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

