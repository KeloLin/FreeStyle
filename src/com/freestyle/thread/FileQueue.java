package com.freestyle.thread;

import java.io.*;

public class FileQueue {
    private final static int size = 4;
    private static Object[] queueList;
    private static int len = 0;
    private String inputPath;
    private String outputPath;

    public FileQueue(String inputPath, String outputPath){
        if(queueList == null){
            queueList = new Object[this.size];
            len = 0;
        }
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    public FileQueue(String inputPath, String outputPath, int size){
        if(queueList == null){
            if(size <= 0){
                queueList = new Object[this.size];
            }
            if(size > 0){
                queueList = new Object[size];
            }
            len = 0;
        }
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    public static boolean isFull(){
        if(queueList != null){
            return queueList.length == len;
        }else {
            return true;
        }

    }

    public synchronized Object[] getQueueList(){
        return this.queueList;
    }

    public synchronized int getLen(){
        return this.len;
    }

    public synchronized int getQueueSize(){
        return this.queueList.length;
    }

    public synchronized boolean addElement(Object object){
        if (this.len < queueList.length){
            System.out.println("FileQueue Warning：队列还没满，增加数据");
            queueList[len] = object;
            this.len++;
            return true;
        }
        if (this.len == queueList.length){
            System.out.println("FileQueue Warning：队列已满，不增加数据");
            return false;
        }
        return false;
    }

    private synchronized Object getElement(){
        Object obj = null;
        if(this.len > 0){
            obj =  queueList[0];
            for (int i = 0; i < this.len; i++) {
                if (i == this.len - 1) {
                    queueList[i] = null;
                }else {
                    queueList[i] = queueList[i+1];
                }
            }
            this.len--;
            System.out.println("FileQueue Warning：队列有数据，队列取出数据"+obj.toString() + "，其他数据前移。");
        }
        return obj;
    }

    public synchronized void transformFile(){
        while (this.len > 0){
            Object obj = getElement();
            File f = new File(obj.toString());
            if (f.exists()){
                System.out.println("FileQueue Warning：队列中文件存在，开始进行转移文件。" + f.getPath());
                FileInputStream fileInputStream = null;
                FileOutputStream fileOutputStream = null;
                byte[] bytes = new byte[2048];
                    try {
                        File in = new File(inputPath);
                        String path = f.getPath().substring(inputPath.length(), f.getPath().length());

                        File out = new File(outputPath + path);
                        if (!out.getParentFile().exists()){
                            out.getParentFile().mkdirs();
                            out.createNewFile();
                        }
                        fileInputStream = new FileInputStream(f);
                        fileOutputStream = new FileOutputStream(out);
                        while (fileInputStream.read(bytes) > -1){
                            fileOutputStream.write(bytes);
                        }
                        System.out.println("FileQueue Warning：转移文件：" + f.getName());

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }
                            if (fileInputStream != null) {
                                fileInputStream.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            if (f.exists() && f.isFile()){
                                System.out.println("FileQueue Warning：删除文件：" + f.getName());
                                f.delete();
                                f.getParentFile().delete();
                            }
                        }

                }

            }
        }
    }

    public synchronized void copyFile(){

    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String getInputPath() {
        return inputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }
}
