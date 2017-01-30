package com.company;

/**
 * Created by Administrator on 2017/1/30.
 */
public class resBean {
    private String school;
    private String className;
    private double yuwen_average;
    private double math_average;
    private double eng_average;
    private double yuwen_ratio;
    private double math_ratio;
    private double eng_ratio;
    private double yuwen_first;
    private double math_first;
    private double eng_first;
    private double yuwen_last;
    private double math_last;
    private double eng_last;
    private double num;

    public double getYuwen_first() {
        return yuwen_first;
    }

    public void setYuwen_first(double yuwen_first) {
        this.yuwen_first = yuwen_first;
    }

    public double getMath_first() {
        return math_first;
    }

    public void setMath_first(double math_first) {
        this.math_first = math_first;
    }

    public double getEng_first() {
        return eng_first;
    }

    public void setEng_first(double eng_first) {
        this.eng_first = eng_first;
    }

    public double getYuwen_last() {
        return yuwen_last;
    }

    public void setYuwen_last(double yuwen_last) {
        this.yuwen_last = yuwen_last;
    }

    public double getMath_last() {
        return math_last;
    }

    public void setMath_last(double math_last) {
        this.math_last = math_last;
    }

    public double getEng_last() {
        return eng_last;
    }

    public void setEng_last(double eng_last) {
        this.eng_last = eng_last;
    }

    public resBean(String school, String className, double yuwen_average, double math_average, double eng_average, double yuwen_ratio, double math_ratio, double eng_ratio, double yuwen_first, double math_first, double eng_first, double yuwen_last, double math_last, double eng_last, double num) {
        this.school = school;
        this.className = className;
        this.yuwen_average = yuwen_average;
        this.math_average = math_average;
        this.eng_average = eng_average;
        this.yuwen_ratio = yuwen_ratio;
        this.math_ratio = math_ratio;
        this.eng_ratio = eng_ratio;
        this.yuwen_first = yuwen_first;
        this.math_first = math_first;
        this.eng_first = eng_first;
        this.yuwen_last = yuwen_last;
        this.math_last = math_last;
        this.eng_last = eng_last;
        this.num = num;
    }
    public resBean(String school, String className, String yuwen_average,
                   String math_average, String eng_average, String yuwen_ratio,
                   String math_ratio, String eng_ratio, String yuwen_first,
                   String math_first, String eng_first, String yuwen_last,
                   String math_last, String eng_last, String num) {
        this.school = school;
        this.className = className;
        this.yuwen_average = Double.parseDouble(yuwen_average);
        this.math_average = Double.parseDouble(math_average);
        this.eng_average = Double.parseDouble(eng_average);
        this.yuwen_ratio = Double.parseDouble(yuwen_ratio);
        this.math_ratio = Double.parseDouble(math_ratio);
        this.eng_ratio = Double.parseDouble(eng_ratio);
        this.yuwen_first = Double.parseDouble(yuwen_first);
        this.math_first = Double.parseDouble(math_first);
        this.eng_first = Double.parseDouble(eng_first);
        this.yuwen_last = Double.parseDouble(yuwen_last);
        this.math_last = Double.parseDouble(math_last);
        this.eng_last = Double.parseDouble(eng_last);
        this.num = Double.parseDouble(num);
    }
    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public double getYuwen_average() {
        return yuwen_average;
    }

    public void setYuwen_average(double yuwen_average) {
        this.yuwen_average = yuwen_average;
    }

    public double getMath_average() {
        return math_average;
    }

    public void setMath_average(double math_average) {
        this.math_average = math_average;
    }

    public double getEng_average() {
        return eng_average;
    }

    public void setEng_average(double eng_average) {
        this.eng_average = eng_average;
    }

    public double getYuwen_ratio() {
        return yuwen_ratio;
    }

    public void setYuwen_ratio(double yuwen_ratio) {
        this.yuwen_ratio = yuwen_ratio;
    }

    public double getMath_ratio() {
        return math_ratio;
    }

    public void setMath_ratio(double math_ratio) {
        this.math_ratio = math_ratio;
    }

    public double getEng_ratio() {
        return eng_ratio;
    }

    public void setEng_ratio(double eng_ratio) {
        this.eng_ratio = eng_ratio;
    }

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }
}
