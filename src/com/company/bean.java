package com.company;

/**
 * Created by Administrator on 2017/1/30.
 */
public class bean {
    private String school;
    private String className;
    private double yuwen;
    private double math;
    private double eng;
    private double num;

    public bean(String school, String className, double num, double yuwen, double math, double eng) {
        this.school = school;
        this.className = className;
        this.yuwen = yuwen;
        this.math = math;
        this.eng = eng;
        this.num = num;
    }

    public bean(String school, String className, String num, String yuwen, String math, String eng) {
        this.school = school;
        this.className = className;
        this.yuwen = Double.parseDouble(yuwen);
        this.math = Double.parseDouble(math);
        this.eng = Double.parseDouble(eng);
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

    public double getYuwen() {
        return yuwen;
    }

    public void setYuwen(double yuwen) {
        this.yuwen = yuwen;
    }

    public double getMath() {
        return math;
    }

    public void setMath(double math) {
        this.math = math;
    }

    public double getEng() {
        return eng;
    }

    public void setEng(double eng) {
        this.eng = eng;
    }

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;

    }

    @Override
    public String toString() {
        return "==================================" +
                "\n" + "学校" + school + "班级" + className + "序号" + num + "语文" + yuwen + "数学" + math + "英语" + eng;
    }
}
