package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import static com.sun.glass.ui.Cursor.setVisible;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * Created by Administrator on 2017/1/31.
 */
public class GUI {
    private Frame f;
    //定义打开和保存对话框
    private FileDialog openDia, saveDia;
    //设置文本区域来保存打开的数据
    public static TextArea ta;
    private File file;
    public static String filePath = "";
    public static String exportFilePath = "";
    private JButton button, button1;

    GUI() {
        init();
    }

    public void init() {
        f = new Frame("my window");
        f.setBounds(300, 100, 500, 300);
        f.setLayout(new FlowLayout());//设置窗体布局为流式布局。
        ta = new TextArea();
        ta.setCaretPosition(ta.getText().length());
        button = new JButton("2.打开excel文件");
        button1 = new JButton("1.选择导出到的excel文件");
        f.add(button1);
        f.add(button);
        f.add(ta);
        //默认模式为 FileDialog.LOAD
        openDia = new FileDialog(f, "我的打开", FileDialog.LOAD);
        saveDia = new FileDialog(f, "我的保存", FileDialog.SAVE);
        myEvent();
        f.setVisible(true);
    }

    private void myEvent() {
        button.addActionListener(new ActionListener() {
            //设置打开文件功能
            public void actionPerformed(ActionEvent e) {
                openDia.setVisible(true);
                String dirPath = openDia.getDirectory();//获取文件路径
                String fileName = openDia.getFile();//获取文件名称
                //System.out.println(dirPath +"++"+ fileName);
                //如果打开路径 或 目录为空 则返回空
                if (dirPath == null || fileName == null) {
                    return;
                }
                if (exportFilePath.length() == 0) {
                    int dialog = JOptionPane.showConfirmDialog(null,
                            "请先选择导出到的EXCEL文件！",
                            "温馨提示", JOptionPane.YES_OPTION);
                    return;
                }
                ta.setText("正在生成数据。。。。");//清空文本
                filePath = dirPath + fileName;
                new Main();
                ta.setText("成功！");//清空文本
            }
        });
        button1.addActionListener(new ActionListener() {


            //设置打开文件功能
            public void actionPerformed(ActionEvent e) {
                openDia.setVisible(true);
                String dirPath = openDia.getDirectory();//获取文件路径
                String fileName = openDia.getFile();//获取文件名称
                //System.out.println(dirPath +"++"+ fileName);
                //如果打开路径 或 目录为空 则返回空
                if (dirPath == null || fileName == null)
                    return;
                exportFilePath = dirPath + fileName;
            }
        });

        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        new GUI();
    }

}
