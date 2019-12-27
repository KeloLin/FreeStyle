package com.freestyle.thread;

import java.io.File;

public class FileBackup implements Runnable {
    private FileQueue queue;
    @Override
    public void run() {
        while (true){
            if (queue.getLen() > 0){
                synchronized (queue){
                    queue.transformFile();
                }
            }else{
                try {
                    Thread.sleep(3000);
                    System.out.println("FileBackup Warning: 队列为空，等待3秒。");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public FileBackup(FileQueue queue){
        this.queue = queue;
    }
}
