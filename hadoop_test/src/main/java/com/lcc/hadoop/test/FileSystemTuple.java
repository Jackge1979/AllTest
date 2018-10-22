package com.lcc.hadoop.test;

import org.apache.hadoop.fs.FileSystem;

/**
 * Created by lcc on 2018/10/19.
 */
public class FileSystemTuple<T,L> {
    public  FileSystem fileSystem;
    public  Long time;

    public FileSystemTuple(FileSystem fileSystem, Long time) {
        this.fileSystem = fileSystem;
        this.time = time;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public void setFileSystem(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
