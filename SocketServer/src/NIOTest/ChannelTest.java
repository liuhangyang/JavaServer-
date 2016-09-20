package NIOTest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by yang on 16-9-18.
 */
public class ChannelTest {
    public static void main(String[] args) {
        try {
            RandomAccessFile aFile = new RandomAccessFile("/home/yang/1.cpp", "rw");
            FileChannel inChannel = aFile.getChannel();
            ByteBuffer buf = ByteBuffer.allocate(48);
            int bytesRead = inChannel.read(buf);
            while(bytesRead != -1){
                System.out.println("Read "+bytesRead);
                buf.flip();
                while (buf.hasRemaining()){
                    System.out.println((char)buf.get());
                }
                buf.clear();
                bytesRead = inChannel.read(buf);
            }
            aFile.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException t){
            t.printStackTrace();
        }

    }
}
