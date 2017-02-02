package com.company;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Main {

    private static Connection conn;
    private static PreparedStatement pstmt;
    private static String excel2003_2007 = GUI.filePath;
    String excel2010 = Common.STUDENT_INFO_XLSX_PATH;
    private static Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
    private static int count = 0;

    private static List<bean> allDataList = new ArrayList<>();
    private static StringBuilder sb;
    private static List<Double> yuwenList = new ArrayList<>();
    private static List<Double> mathList = new ArrayList<>();
    private static List<Double> engList = new ArrayList<>();
    private static double yunwen_last;
    private static double math_last;
    private static double eng_last;
    private static double yunwen_first;
    private static double math_first;
    private static double eng_first;
    private static List<resBean> finalWriteList = new ArrayList<>();//最终将写入excel的数据集
    private static List<Integer> gradeList = new ArrayList<>(); //存储年级

    public Main() {
        createTable();                 //创建数据表
        List<bean> list = loadData(excel2003_2007);
        for (bean temp : list) {
            if (!gradeList.contains(Integer.parseInt(temp.getClassName().trim().charAt(0) + ""))) {
                gradeList.add(Integer.parseInt(temp.getClassName().trim().charAt(0) + ""));
            }
        }
        Collections.sort(gradeList);
        insert(list);                 //从excel读取数据并将读取的数据插入数据库，新增年级字段
        for (int i = 0; i < gradeList.size(); i++) {
            getAll(gradeList.get(i));      //初始化各种数据集
            initData();    //生成各种分数线
            showMap();     //根据分数线计算各种数据
        }
        ToExcel(GUI.exportFilePath);    //导出到excel
        clearData();                //清空数据表中的数据
    }

    public static void initData() {
        int len_first = (int) (count * 0.2);
        int len_last = (int) (count * 0.8);
        yunwen_last = yuwenList.get(len_first);
        math_last = mathList.get(len_first);
        eng_last = engList.get(len_first);
        yunwen_first = yuwenList.get(len_last);
        math_first = mathList.get(len_last);
        eng_first = engList.get(len_last);
        System.err.println(yunwen_first);
        System.err.println(math_first);
        System.err.println(eng_first);
        System.err.println(yunwen_last);
        System.err.println(math_last);
        System.err.println(eng_last);
    }

    public static void createTable() {
        if (conn == null) {
            initMySql();
        }
        Statement stmt = null;
        try {
            stmt = (Statement) conn.createStatement();
            String sql = "CREATE TABLE  IF NOT EXISTS scores ( "
                    + " id  int(11) PRIMARY KEY  NOT NULL AUTO_INCREMENT, "
                    + " school  VARCHAR(255), "
                    + " classname  VARCHAR(255), "
                    + " num  VARCHAR(255), "
                    + " yuwen  VARCHAR(255), "
                    + " math  VARCHAR(255), "
                    + " eng  VARCHAR(255),"
                    + " grade  VARCHAR(255)"
                    + ");";
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void clearData() {
        if (conn == null) {
            initMySql();
        }

        String sql = "TRUNCATE TABLE scores";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void ToExcel(String path) {

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Sheet1");

        String[] cols = {"学校", "班级"
                , "语文平均分", "语文及格率", "语文前20%", "语文后20%"
                , "数学平均分", "数学及格率", "数学前20%", "数学后20%"
                , "英语平均分", "英语及格率", "英语前20%", "英语后20%"
        };
        Object[][] value = new Object[finalWriteList.size() + 1][cols.length];
        for (int m = 0; m < cols.length; m++) {
            value[0][m] = cols[m];
        }
        for (int i = 0; i < finalWriteList.size(); i++) {
            resBean users = (resBean) finalWriteList.get(i);

            value[i + 1][0] = users.getSchool();
            value[i + 1][1] = users.getClassName();
            value[i + 1][2] = users.getYuwen_average();
            value[i + 1][3] = users.getYuwen_ratio();
            value[i + 1][4] = users.getYuwen_first();
            value[i + 1][5] = users.getYuwen_last();
            value[i + 1][6] = users.getMath_average();
            value[i + 1][7] = users.getMath_ratio();
            value[i + 1][8] = users.getMath_first();
            value[i + 1][9] = users.getMath_last();
            value[i + 1][10] = users.getEng_average();
            value[i + 1][11] = users.getEng_ratio();
            value[i + 1][12] = users.getEng_first();
            value[i + 1][13] = users.getEng_last();

        }
        ExcelUtil.writeArrayToExcel(wb, sheet, finalWriteList.size() + 1, cols.length, value);

        ExcelUtil.writeWorkbook(wb, path);

    }

    public static void showMap() {
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            ArrayList<String> list = map.get(key);
            for (String name : list) {          //依次列出每个学校的每个班级
                System.out.println(key + " " + name);
                String res = getBy(key, name);
                String[] split = res.split("#");
                double yuwen = Double.parseDouble(split[0]);
                double math = Double.parseDouble(split[1]);
                double eng = Double.parseDouble(split[2]);
                double yuwen_num = Double.parseDouble(split[3]);
                double math_num = Double.parseDouble(split[4]);
                double eng_num = Double.parseDouble(split[5]);
                double total_num = Double.parseDouble(split[6]);
                double yuwen_fir = 0;
                double math_fir = 0;
                double eng_fir = 0;
                double yuwen_las = 0;
                double math_las = 0;
                double eng_las = 0;
                for (int i = 0; i < allDataList.size(); i++) {
                    if (allDataList.get(i).getSchool().equals(key) && allDataList.get(i).getClassName().equals(name)) {
                        if (allDataList.get(i).getYuwen() >= yunwen_first) {
                            yuwen_fir++;
                        } else if (allDataList.get(i).getYuwen() <= yunwen_last) {
                            yuwen_las++;
                        }
                        if (allDataList.get(i).getMath() >= math_first) {
                            math_fir++;
                        } else if (allDataList.get(i).getMath() <= math_last) {
                            math_las++;
                        }
                        if (allDataList.get(i).getEng() >= eng_first) {
                            eng_fir++;
                        } else if (allDataList.get(i).getEng() <= eng_last) {
                            eng_las++;
                        }
                    }
                }
                if (yuwen_fir > total_num || math_fir > total_num || eng_fir > total_num) {
                    System.out.println();
                }
                resBean temp = new resBean(key, name, yuwen, math, eng,
                        yuwen_num / total_num, math_num / total_num, eng_num / total_num,
                        yuwen_fir / total_num, math_fir / total_num, eng_fir / total_num,
                        yuwen_las / total_num, math_las / total_num, eng_las / total_num
                        , 0);
                finalWriteList.add(temp);
                System.out.println(res);
//                insertResult(temp);
            }
        }
        allDataList.clear();//清除历史数据
        yuwenList.clear();
        mathList.clear();
        engList.clear();
        map.clear(); //清除学校--班级 键值对
    }

    public static List<bean> loadData(String path) {

        // read the 2003-2007 excel
        List<bean> list = null;
        try {
            list = new ReadExcel().readExcel(excel2003_2007);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        for (bean bean : list) {
//            System.out.println(bean.toString());
////            GUI.ta.setText(GUI.ta.getText().toString() + "\n" + bean.toString());
////            GUI.ta.setCaretPosition(GUI.ta.getText().length());
//        }
//        System.out.println("======================================");
        return list;
    }

    private static Integer getAll(int grade) {
        if (conn == null) {
            initMySql();
        }

        String sql = "select school,classname from scores where grade = '" + grade + "'";
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
        sql = "select count(*) from scores where grade = '" + grade + "'";
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sql = "select * from scores where grade = '" + grade + "'";
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean bean = new bean(rs.getString(2), rs.getString(3), rs.getString(4)
                        , rs.getString(5), rs.getString(6), rs.getString(7));
                allDataList.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sql = "select yuwen,math,eng from scores where grade = '" + grade + "'";
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                yuwenList.add(Double.valueOf(rs.getString(1)));
                mathList.add(Double.valueOf(rs.getString(2)));
                engList.add(Double.valueOf(rs.getString(3)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(yuwenList);
        Collections.sort(mathList);
        Collections.sort(engList);
        return null;
    }

    private static String getBy(String school, String className) {
        if (conn == null) {
            initMySql();
        }
        try {
            //////////////求平均分////////////////////////////////////
            PreparedStatement stmt = (PreparedStatement) conn.
                    prepareStatement("select avg(yuwen),avg(math),avg(eng) from scores where school = '" +
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
//                count++;
            }
            System.out.println("==============***********==============");
            //////////////求及格率////////////////////////////////////
            stmt = (PreparedStatement) conn.
                    //count(case when num>=60 then 1 else null end),
                            prepareStatement("select count(case when yuwen>=60 then 1 else null end)," +
                            "count(case when math>=60 then 1 else null end)," +
                            "count(case when eng>=60 then 1 else null end) ,count(*) from scores where school = '" +
                            school + "' and classname='" + className + "'");
            rs = stmt.executeQuery();
            col = rs.getMetaData().getColumnCount();
            System.out.println("==============***********==============");
            while (rs.next()) {
                for (int i = 1; i <= col; i++) {
                    System.out.print(rs.getString(i) + "\t");
//                    GUI.ta.setText(GUI.ta.getText().toString() + rs.getString(i) + "\t");
//                    GUI.ta.setCaretPosition(GUI.ta.getText().length());
                    if ((i == 2) && (rs.getString(i).length() < 8)) {
                        System.out.print("\t");
//                        GUI.ta.setText(GUI.ta.getText().toString() + "\n" + "\t");
//                        GUI.ta.setCaretPosition(GUI.ta.getText().length());
                    }
                    sb.append(rs.getString(i) + "#");
                }
//                count++;
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
            String sql = "insert into result (school,classname,yuwen_average,math_average,eng_average," +
                    "yuwen_ratio,math_ratio,eng_ratio," +
                    "yuwen_first,math_first,eng_first," +
                    "yuwen_last,math_last,eng_last) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            pstmt.setString(9, bean.getYuwen_first() + "");
            pstmt.setString(10, bean.getMath_first() + "");
            pstmt.setString(11, bean.getEng_first() + "");
            pstmt.setString(12, bean.getYuwen_last() + "");
            pstmt.setString(13, bean.getMath_last() + "");
            pstmt.setString(14, bean.getEng_last() + "");
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
                String sql = "insert into scores (school,classname,num,yuwen,math,eng,grade) values(?,?,?,?,?,?,?)";
                pstmt = (PreparedStatement) conn.prepareStatement(sql);
//                temp.getSchool()
                pstmt.setString(1, temp.getSchool());
                pstmt.setString(2, temp.getClassName().trim());
                pstmt.setString(3, temp.getNum()+"");
                pstmt.setString(4, temp.getYuwen()+"");
                pstmt.setString(5, temp.getMath()+"");
                pstmt.setString(6, temp.getEng()+"");
                pstmt.setString(7, temp.getClassName().trim().charAt(0)+"");//添加年级
                i = pstmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("error!!!");
            }
        }
//        try {
//            pstmt.close();
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("error!!!");
//        }
    }

    public static void initMySql() {
        try {
            //定义一个MYSQL链接对象
            conn = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance(); //MYSQL驱动
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/work?useUnicode=true&characterEncoding=UTF-8", "root", "158249"); //链接本地MYSQL
            System.out.print("正在插入...");
            GUI.ta.setText(GUI.ta.getText().toString() + "\n" + "正在插入...");
            GUI.ta.setCaretPosition(GUI.ta.getText().length());
        } catch (Exception e) {
            System.out.print("MYSQL ERROR:" + e.getMessage());
        }
    }

}
