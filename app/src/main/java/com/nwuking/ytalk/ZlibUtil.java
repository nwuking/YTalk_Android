package com.nwuking.ytalk;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public final class ZlibUtil {

    public static String decompressString(byte[] bytes) {
        //Decompress the bytes // 开始解压,
        Inflater decompresser = new Inflater();
        decompresser.setInput(bytes, 0, bytes.length);
        //对byte[]进行解压，同时可以要解压的数据包中的某一段数据，就好像从zip中解压出某一个文件一样。
        byte[] result = new byte[bytes.length];
        int resultLength = 0;
        //返回的是解压后的的数据包大小
        try {
            resultLength  = decompresser.inflate(result);
        }catch (DataFormatException e){
            return "";
        }

        decompresser.end();
        if (resultLength <= 0)
            return "";

        return new String(result, 0, resultLength);
    }

    public static byte[] decompressBytes(byte[] data){
        byte[] output = new byte[0];
        Inflater decompresser = new Inflater();
        decompresser.reset();
        //设置当前输入解压
        decompresser.setInput(data, 0, data.length);
        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!decompresser.finished()) {
                int i = decompresser.inflate(buf);
                o.write(buf, 0, i);
            }
            output = o.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        } finally {
            try {
                o.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        decompresser.end();
        return output;
    }
}