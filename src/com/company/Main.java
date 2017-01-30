package com.company;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.*;

public class Main {

    private static Connection conn;
    private static PreparedStatement pstmt;
    private static String excel2003_2007 = "C:\\Users\\Administrator\\Desktop\\项目\\test1.xls";
    String excel2010 = Common.STUDENT_INFO_XLSX_PATH;
    private static Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
    private static int count = 0;

    private static List<bean> selectedList = new ArrayList<>();
    private static StringBuilder sb;

    public static void main(String[] args) {
//        insert(loadData(excel2003_2007));                 //从excel读取数据并将读取的数据插入数据库
        getAll();
//        getBy("回民小学", "3.2");
        showMap();
    }

    public static void showMap() {
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            ArrayList<String> list = map.get(key);
            for (String name : list) {          //依次列出每个学校的每个班级
                System.out.println(key + " " + name);
                selectedList.clear();
                String res = getBy(key, name);
                String[] split = res.split("#");
                double yuwen = Double.parseDouble(split[0]);
                double math = Double.parseDouble(split[1]);
                double eng = Double.parseDouble(split[2]);
                double yuwen_num = Double.parseDouble(split[3]);
                double math_num = Double.parseDouble(split[4]);
                double eng_num = Double.parseDouble(split[5]);
                double total_num = Double.parseDouble(split[6]);
                resBean temp = new resBean(key, name, yuwen, math, eng,
                        yuwen_num / total_num, math_num / total_num, eng_num / total_num, 0);
                System.out.println(res);
                insertResult(temp);
            }
        }
        System.out.println(count);
    }

    public static List<bean> loadData(String path) {

        // read the 2003-2007 excel
        List<bean> list = null;
        try {
            list = new ReadExcel().readExcel(excel2003_2007);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (bean bean : list) {
            System.out.println(bean.toString());
        }
        System.out.println("======================================");
        return list;
    }

    private static Integer getAll() {
        if (conn == null) {
            initMySql();
        }

        String sql = "select school,classname from score";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
//            System.out.println("============================");
            while (rs.next()) {
//                for (int i = 1; i <= col; i++) {
//                    System.out.print(rs.getString(i) + "\t");
//                    if ((i == 2) && (rs.getString(i).length() < 8)) {
//                        System.out.print("\t");
//                    }
//                }
                String school = rs.getString(1);
                String classname = rs.getString(2);
                if (!map.containsKey(school)) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(classname);
                    map.put(school, list);
                } else {
                    ArrayList<String> arrayList = map.get(school);
                    if (!arrayList.contains(classname)) {
                        arrayList.add(classname);
                    }
                }
            }
//            System.out.println("============================");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getBy(String school, String className) {
        if (conn == null) {
            initMySql();
        }
        try {
            //////////////求平均分////////////////////////////////////
            PreparedStatement stmt = (PreparedStatement) conn.
                    prepareStatement("select avg(yuwen),avg(math),avg(eng) from score where school = '" +
                            school + "' and classname='" + className + "'");
            ResultSet rs = stmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            System.out.println("==============***********==============");
            sb = new StringBuilder();
            while (rs.next()) {
                for (int i = 1; i <= col; i++) {
//                    System.out.print(rs.getString(i) + "\t");
//                    if ((i == 2) && (rs.getString(i).length() < 8)) {
//                        System.out.print("\t");
//                    }
                    sb.append(rs.getString(i) + "#");
                }
                count++;
                System.out.println("");
            }
            System.out.println("==============***********==============");
            //////////////求及格率////////////////////////////////////
            stmt = (PreparedStatement) conn.
                    //count(case when num>=60 then 1 else null end),
                            prepareStatement("select count(case when yuwen>=60 then 1 else null end)," +
                            "count(case when math>=60 then 1 else null end)," +
                            "count(case when eng>=60 then 1 else null end) ,count(*) from score where school = '" +
                            school + "' and classname='" + className + "'");
            rs = stmt.executeQuery();
            col = rs.getMetaData().getColumnCount();
            System.out.println("==============***********==============");
            while (rs.next()) {
                for (int i = 1; i <= col; i++) {
                    System.out.print(rs.getString(i) + "\t");
                    if ((i == 2) && (rs.getString(i).length() < 8)) {
                        System.out.print("\t");
                    }
                    sb.append(rs.getString(i) + "#");
                }
                count++;
                System.out.println("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static void insertResult(resBean bean) {
        if (conn == null) {
            initMySql();
        }
        int i = 0;
        try {
            String sql = "insert into result (school,classname,yuwen_average,math_average,eng_average" +
                    ",yuwen_ratio,math_ratio,eng_ratio) values(?,?,?,?,?,?,?,?)";
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
//                temp.getSchool()
            pstmt.setString(1, bean.getSchool());
            pstmt.setString(2, bean.getClassName());
            pstmt.setString(3, bean.getYuwen_average() + "");
            pstmt.setString(4, bean.getMath_average() + "");
            pstmt.setString(5, bean.getEng_average() + "");
            pstmt.setString(6, bean.getYuwen_ratio() + "");
            pstmt.setString(7, bean.getMath_ratio() + "");
            pstmt.setString(8, bean.getEng_ratio() + "");
            i = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("error!!!");
        }
//        try {
//            pstmt.close();
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("error!!!");
//        }
    }

    private static void insert(List<bean> bean) {
        if (conn == null) {
            initMySql();
        }
        int i = 0;
        for (bean temp : bean) {
            try {
                String sql = "insert into score (school,classname,num,yuwen,math,eng) values(?,?,?,?,?,?)";
                pstmt = (PreparedStatement) conn.prepareStatement(sql);
//                temp.getSchool()
                pstmt.setString(1, temp.getSchool());
                pstmt.setString(2, temp.getClassName());
                pstmt.setDouble(3, temp.getNum());
                pstmt.setDouble(4, temp.getYuwen());
                pstmt.setDouble(5, temp.getMath());
                pstmt.setDouble(6, temp.getEng());
                i = pstmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("error!!!");
            }
        }
        try {
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("error!!!");
        }
    }

    public static void initMySql() {
        try {
            //定义一个MYSQL链接对象
            conn = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance(); //MYSQL驱动
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ymh?useUnicode=true&characterEncoding=UTF-8", "root", "158249"); //链接本地MYSQL
            System.out.print("正在插入...");
        } catch (Exception e) {
            System.out.print("MYSQL ERROR:" + e.getMessage());
        }
    }

}
