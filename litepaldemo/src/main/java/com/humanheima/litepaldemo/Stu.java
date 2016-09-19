package com.humanheima.litepaldemo;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class Stu {

    /**
     * courseList : [{"name":"math","score":1.5},{"name":"math","score":1.5}]
     * name : hongmin
     */

    private String name;
    /**
     * name : math
     * score : 1.5
     */

    private List<CourseListBean> courseList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CourseListBean> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<CourseListBean> courseList) {
        this.courseList = courseList;
    }

    public static class CourseListBean {
        private String name;
        private double score;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }
    }
}
