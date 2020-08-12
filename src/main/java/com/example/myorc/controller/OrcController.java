package com.example.myorc.controller;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
public class OrcController {
    @RequestMapping(value = "test{day}")
    public String test(@PathVariable("day") String day){

        ITesseract instance =getTesseract();
        System.out.println("sss");
        String path = "D://Tess4J/"+day;
        String resultPath = "D://Tess4J//result.txt";
        String[] filelist = readfiles(path);

        for (int i = 0; i < filelist.length; i++) {
            File file = new File(path + "\\" + filelist[i]);
            if (!file.isDirectory()) {
               /* System.out.println("path=" + file.getPath());
                System.out.println("absolutepath=" + file.getAbsolutePath());*/
                System.out.println("name=" + file.getName());
                String result =  doOrc(instance,file);
                try {
                    write(resultPath,result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("result: ");
                System.out.println(result);
            }
        }
        return "success";
    }

    public static boolean readfile(String filepath) throws FileNotFoundException {
        try {

            File file = new File(filepath);
            if (!file.isDirectory()) {
                System.out.println("文件");
                System.out.println("path=" + file.getPath());
                System.out.println("absolutepath=" + file.getAbsolutePath());
                System.out.println("name=" + file.getName());

            } else if (file.isDirectory()) {
                System.out.println("文件夹");
                String[] filelist = file.list();
                if(filelist.length==0){
                    System.out.println("改目录没有文件");
                }
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "\\" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        System.out.println("path=" + readfile.getPath());
                        System.out.println("absolutepath=" + readfile.getAbsolutePath());
                        System.out.println("name=" + readfile.getName());

                    } else if (readfile.isDirectory()) {
                        readfile(filepath + "\\" + filelist[i]);
                    }
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return true;
    }
    public String[] readfiles(String filepath)  {
            File file = new File(filepath);
            if (!file.isDirectory()) {
                System.out.println("文件");
                System.out.println("path=" + file.getPath());
                System.out.println("absolutepath=" + file.getAbsolutePath());
                System.out.println("name=" + file.getName());
            } else if (file.isDirectory()) {
                System.out.println("文件夹");
                String[] filelist = file.list();
                return filelist;
            }
        return null;
    }

    public String doOrc(ITesseract instance, File file){

        String result = null;
        try {
            long startTime = System.currentTimeMillis();
            result =  instance.doOCR(file);
            long endTime = System.currentTimeMillis();
            System.out.println("Time is：" + (endTime - startTime) + " 毫秒");
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ITesseract getTesseract(){
        ITesseract instance = new Tesseract();
        //设置训练库的位置
        instance.setDatapath( ".//tessdata");
        instance.setLanguage("chi_sim");//chi_sim ：简体中文， eng	根据需求选择语言库
        return instance;
    }

    public static void write(String path,String content)throws IOException {
        if(content == null || content==""){
            return;
        }
          //将写入转化为流的形式
             BufferedWriter bw = new BufferedWriter(new FileWriter(path,true));
           //一次写一行
            bw.write(content);
            bw.newLine();  //换行用
            bw.close();
             System.out.println("写入成功");
           }

}
