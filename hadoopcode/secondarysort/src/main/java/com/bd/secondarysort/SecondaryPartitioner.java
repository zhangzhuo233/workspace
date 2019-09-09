package com.bd.secondarysort;


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Partitioner;

// shuffle分区
public class SecondaryPartitioner extends Partitioner<CustomWritable, IntWritable> {
    @Override
    public int getPartition(CustomWritable key, IntWritable value, int numReduceTasks) {
//        HashPartitioner
        return (key.getOriginKey().hashCode() & 2147483647) % numReduceTasks;
    }
}
