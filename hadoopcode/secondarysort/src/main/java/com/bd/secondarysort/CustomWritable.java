package com.bd.secondarysort;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CustomWritable implements WritableComparable<CustomWritable> {
    private String originKey;
    private int originValue;

    // Constructor
    public CustomWritable() {
    }

    public CustomWritable(String originKey, int originValue) {
        this.originKey = originKey;
        this.originValue = originValue;
    }

    // get & set func
    public String getOriginKey() {
        return this.originKey;
    }

    public int getOriginValue() {
        return this.originValue;
    }

    public void set(String originKey, int originValue) {
        this.originKey = originKey;
        this.originValue = originValue;
    }

    @Override
    public int compareTo(CustomWritable o) {
        int result = this.getOriginKey().compareTo(o.getOriginKey());
        if (result != 0)
            return result;
        return Integer.valueOf(this.getOriginValue())
                .compareTo(Integer.valueOf(o.getOriginValue()));
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(originKey);
        dataOutput.writeInt(originValue);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        // read的顺序，必须要write的顺序一致
        this.originKey = dataInput.readUTF();
        this.originValue = dataInput.readInt();
    }
}
