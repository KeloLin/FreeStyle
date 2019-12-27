package com.freestyle.thread;

import java.io.File;

public class Demo {
    public static void main(String[] args) {
        String FILE_INPUT_PATH = "E:\\file\\data";
        String FILE_OUTPUT_PATH = "E:\\file\\backup";

        FileQueue queue = new FileQueue(FILE_INPUT_PATH, FILE_OUTPUT_PATH);
        FileScan scan = new FileScan(queue);
        FileBackup backup = new FileBackup(queue);

        new Thread(scan).start();
        new Thread(backup).start();



    }
}
