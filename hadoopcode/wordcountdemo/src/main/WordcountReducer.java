package main;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class WordcountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    private IntWritable valueout = new IntWritable();
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int sum = 0;
        for (IntWritable value:
             values) {
            int temp = value.get();
            sum += temp;
        }
        valueout.set(sum);
        context.write(key, valueout);
    }
}
