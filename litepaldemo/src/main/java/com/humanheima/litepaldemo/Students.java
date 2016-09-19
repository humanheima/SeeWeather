package com.humanheima.litepaldemo;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class Students {
    private String name;
    private List<Course> courseList;

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
