package com.humanheima.rxjavademo;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action4;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private final static String tag = "tag";
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    /**
     * RxJava 的基本实现主要有三点：
     * <p>
     * 11) 创建 Observer 即观察者，它决定事件触发的时候将有怎样的行为
     * 除了 Observer 接口之外，RxJava 还内置了一个实现了 Observer 的抽象类：Subscriber。
     * Subscriber 对 Observer 接口进行了一些扩展，但他们的基本使用方式是完全一样的：
     * <p>
     * 2) 创建 Observable
     * Observable 即被观察者，它决定什么时候触发事件以及触发怎样的事件。
     * <p>
     * 3) Subscribe (订阅)
     * 创建了 Observable 和 Observer 之后，再用 subscribe() 方法将它们联结起来，整条链子就可以工作了
     */
    private void fun1() {

        //1 创建观察者
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
         * // 将会依次调用：
         // onNext("Hello");
         // onNext("Hi");
         // onNext("world");
         // onCompleted();
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
         * // 将会依次调用：
         // onNext("Hello");
         // onNext("Hi");
         // onNext("Aloha");
         // onCompleted();
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
     * <p>
     * 在RxJava 中，Scheduler ——调度器，相当于线程控制器，RxJava 通过它来指定每一段代码应该运行在什么样的线程。
     * RxJava 已经内置了几个 Scheduler ，它们已经适合大多数的使用场景：
     * <p>
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
                }).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        })
        ;

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
                List<Student.Course> courseList = student.getCourseList();
                for (Student.Course course : courseList) {
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

        Subscriber<Student.Course> subscriber2 = new Subscriber<Student.Course>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Student.Course course) {
                Log.e(tag, course.getLesson());
            }
        };

        Observable.from(students)
                .flatMap(new Func1<Student, Observable<Student.Course>>() {
                    @Override
                    public Observable<Student.Course> call(Student student) {
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
        Student.Course course = new Student.Course("语文");
        Student.Course course1 = new Student.Course("数学");
        List<Student.Course> list = new ArrayList<>();
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

        Subscriber<Student.Course> subscriber = new Subscriber<Student.Course>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Student.Course course) {
                Log.e(tag, course.getLesson());
            }
        };

        Observable.from(students)
                .flatMap(new Func1<Student, Observable<Student.Course>>() {
                    @Override
                    public Observable<Student.Course> call(Student student) {
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

    public void useDoOnSubscribe(View view) {
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        progressBar.setVisibility(View.VISIBLE);//需要在主线程执行
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

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(tag, "onNext" + integer);
                    }
                });
    }
}

