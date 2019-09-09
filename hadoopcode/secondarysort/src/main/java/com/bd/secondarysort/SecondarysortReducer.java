package com.bd.secondarysort;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class SecondarysortReducer extends Reducer<CustomWritable, IntWritable, Text, IntWritable> {
    private Text keyout = new Text();
    @Override
    protected void reduce(CustomWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        for (IntWritable tmp:
             values) {
            keyout.set(key.getOriginKey());
            context.write(keyout, tmp);
        }
    }
}