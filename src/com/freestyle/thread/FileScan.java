package com.freestyle.thread;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class FileScan implements Runnable {
    private boolean FLAG = true;
    private File inputFile;
    private FileQueue queue;
    private static int times = 0;

    @Override
    public void run() {

        while (FLAG){
            System.out.println("FileScan Warning: 扫描的文件夹size: " + inputFile.list().length);
            if (inputFile.list().length == 0){
                try {
                    long value = getWatingTime(times);
                    System.out.println("FileScan Warning: 文件夹中没文件，等待"+ value/1000 +"秒。第" +times+ "次等待");
                    Thread.sleep(value);
                    recordTimes();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                if(queue.isFull()){
                    try {
                        long value = getWatingTime(times);
                        System.out.println("FileScan Warning: 队列已满，等待等待"+ value/1000 +"秒。秒。第" +times+ "次等待");
                        Thread.sleep(getWatingTime(times));
                        recordTimes();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    if (inputFile.exists() && inputFile.isDirectory()){
                        clearTimes();
                        System.out.println("FileScan Warning: 扫描的文件夹存在。");
                        // 找文件
                        List<String> filePathList = new LinkedList<>();
                        if (!queue.isFull()) {
                            synchronized (queue){
                                filePathList = getFileList(inputFile);
                                int size = filePathList.size();
                                System.out.println("FileScan Warning: 获取文件列表中。size: " + size);
                                int can_in_put_sum = queue.getQueueSize() - queue.getLen();
                                System.out.println("FileScan Warning: 队列有" + can_in_put_sum + "个位置");
                                int i = can_in_put_sum - 1;
                                if (size <= can_in_put_sum) {
                                    i = size - 1;
                                }
                                for (; i >= 0; i--) {
                                    String path = filePathList.get(i);
                                    filePathList.remove(i);
                                    boolean result = queue.addElement(path);
                                    System.out.println("FileScan Warning: 队列增加文件：" + path + "。result = "+result);
                                }
                            }
                        }


                    }
                }
            }
        }
    }

    public FileScan(FileQueue queue){
        this.queue = queue;
        this.inputFile = new File(queue.getInputPath());
    }

    public synchronized List<String> getFileList(File file){
        List<String> list = new LinkedList<>();
        if (file.exists()){
            if(file.isDirectory()){
                for(File f : file.listFiles()){
                    list.addAll(getFileList(f));
                }
            }else {
                list.add(file.getPath());
            }
        }
        return list;
    }

    public static long getWatingTime(int times){
        long value = 2000;
        if(times/10 > 0){
            value = value * (times/10 + 1);
            System.out.println("getWatingTime:"+value+"==" + times);
        }

        return value;
    }

    public synchronized void recordTimes(){
        times++;
    }

    public synchronized void clearTimes(){
        times = 0;
    }
}
