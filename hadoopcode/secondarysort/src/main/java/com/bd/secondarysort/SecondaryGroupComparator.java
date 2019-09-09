package com.bd.secondarysort;

import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.WritableComparator;

// shuffle分组
public class SecondaryGroupComparator implements RawComparator<CustomWritable> {
    @Override
    public int compare(byte[] bytes, int i, int i1, byte[] bytes1, int i2, int i3) {
        return WritableComparator.compareBytes(bytes, 0, bytes.length-4,
                bytes1, 0, bytes1.length-4);
    }

    @Override
    public int compare(CustomWritable o1, CustomWritable o2) {
        return o1.compareTo(o2);
    }
}
