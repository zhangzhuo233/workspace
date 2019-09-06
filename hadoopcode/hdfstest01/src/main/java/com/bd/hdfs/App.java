package com.bd.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    private FileSystem fs;

    @Before
    public void init() {
        // 获取core-site.xml,hdfs-site.xml,mapred-site.xml,yarn-site.xml
        // 等hadoop配置文件的默认配置信息
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://hadoopserver01:8020");
        try {
            // 获取文件系统的操作对象
            fs = FileSystem.get(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpload() {
        try {
            fs.copyFromLocalFile(new Path("E:\\Abigdata\\code\\hdfstest01\\resources\\hdfs-test.log")
                    , new Path("/hdfs-test.log.bak"));
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
