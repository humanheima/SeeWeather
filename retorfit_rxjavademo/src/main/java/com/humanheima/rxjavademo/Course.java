package com.humanheima.rxjavademo;

import rx.Observable;
import rx.functions.Func0;

public class Course {

    private String lesson;

    public Course(String lesson) {
        this.lesson = lesson;
    }

    public Course() {
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getLesson() {
        return lesson;
    }

    public Observable<String> getLessonObservable() {
        return Observable.just(lesson);
    }

    public Observable<String> valueObservable() {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                //在这之前可以执行保存数据的操作
                return Observable.just(lesson);
            }
        });
    }
}