package com.company;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String excel2003_2007 = "C:\\Users\\Administrator\\Desktop\\test.xls";
        String excel2010 = Common.STUDENT_INFO_XLSX_PATH;
        // read the 2003-2007 excel
        List<Student> list = new ReadExcel().readExcel(excel2003_2007);
        for (Student student : list) {
            System.out.println("No. : " + student.getSchool() + ", name : " + student.getClassName() + ", age : " + student.getNum() + ", score : " + student.getYuwen());
        }
        System.out.println("======================================");
        // read the 2010 excel
//        List<Student> list1 = new ReadExcel().readExcel(excel2010);
//        if (list1 != null) {
//            for (Student student : list1) {
//                System.out.println("No. : " + student.getNo() + ", name : " + student.getName() + ", age : " + student.getAge() + ", score : " + student.getScore());
//            }
//        }
    }


}
