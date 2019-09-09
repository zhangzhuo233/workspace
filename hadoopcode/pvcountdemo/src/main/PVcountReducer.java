package main;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class PVcountReducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
    private IntWritable valueout = new IntWritable();
    @Override
    protected void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int count = 0;
        for (IntWritable tmp:
             values) {
            count += tmp.get();
        }
        valueout.set(count);
        context.write(key, valueout);
    }
}
