package ystruct.com;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * Created by yang on 16-9-19.
 */
public class AsyncTimeServerHandler  implements Runnable{
    private int port;
    CountDownLatch latch;
    AsynchronousServerSocketChannel asynchronousServerSocketChannel;
    public AsyncTimeServerHandler(int port){
        this.port = port;
        try{
            asynchronousServerSocketChannel =AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("The time server is start in port: "+port);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //在完成一组正在执行的操作之前,允许当前的线程一直阻塞.在本例中,我们让线程在此阻塞,防止服务端执行完成退出。
        latch = new CountDownLatch(1);
        doAccept();
        try {
            latch.await();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void doAccept(){
        asynchronousServerSocketChannel.accept(this,new AcceptCompletionHandler());

    }
}
