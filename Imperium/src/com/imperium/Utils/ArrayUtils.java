package com.imperium.Utils;

import java.util.List;

public class ArrayUtils {
    public static byte[] toPrimitiveArray(List<Byte> bytes){

        byte[] returnValue = new byte[bytes.size()];
        for(int i = 0; i < bytes.size(); i++){

            returnValue[i] = bytes.get(i).byteValue();
        }

        return returnValue;
    }
}
