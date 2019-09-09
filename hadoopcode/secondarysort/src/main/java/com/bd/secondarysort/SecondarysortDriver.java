package com.bd.secondarysort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class SecondarysortDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 测试地址
        args = new String[] {
                "hdfs://hadoopserver01:8020/secondarysortdemo/input",
                "hdfs://hadoopserver01:8020/secondarysortdemo/output1"
        };

        // conf
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://hadoopserver01:8020");
        // job
        Job job = Job.getInstance(conf);
        job.setJarByClass(SecondarysortDriver.class);
        // params set
        // 路径
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        // 自己的mapper&reducer
        job.setMapperClass(SecondarysortMapper.class);
        job.setReducerClass(SecondarysortReducer.class);
        // map输出类型
        job.setMapOutputKeyClass(CustomWritable.class);
        job.setMapOutputValueClass(IntWritable.class);
        // reduce输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        // 设置分区，分组
        job.setPartitionerClass(SecondaryPartitioner.class);
        job.setGroupingComparatorClass(SecondaryGroupComparator.class);
        // 设置reducetask数量
        job.setNumReduceTasks(2);
        // submit
        boolean result = job.waitForCompletion(true);
        System.exit(result?0:1);
    }
}
