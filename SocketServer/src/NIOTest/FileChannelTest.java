package NIOTest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Created by yang on 16-9-18.
 */
public class FileChannelTest {
    public static void main(String[] args) {
        try{
            RandomAccessFile fromFile = new RandomAccessFile("1.cpp","rw");
            FileChannel fromChannel = fromFile.getChannel();
            RandomAccessFile tofile = new RandomAccessFile("fei.cpp","rw");
            FileChannel toChannel = tofile.getChannel();

            long position = 0;
            long count = fromChannel.size();
            toChannel.transferFrom(fromChannel,position,count);

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException t){
            t.printStackTrace();
        }
    }
}
