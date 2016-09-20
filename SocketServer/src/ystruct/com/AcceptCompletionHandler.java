package ystruct.com;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by yang on 16-9-19.
 */
public class AcceptCompletionHandler implements CompletionHandler{
    @Override
    public void completed(Object result, Object attachment) {
        /*我们从attachment获取成员变量AsynchronousServerSocketChannel,然后继续调用它的accept()方法，
        有的读者在此可能会心存疑惑,既然已经接收客户端成功了,为什么还要再次调用accept方法呢,原因是:当我们调用AsynchronousServerSocketChannel,然后继续调用它的accept方法,原因是这样的,
        当我们调用AsynchronoudServerSocketChannel的accept方法之后,如果有新的客户端连接接入,系统将回调我们传入的CompletionHandler实例的completed方法,表示新的客户端已经接入成功,因为一个AsynchronousServerSocketChannel
        可以接收成千上万个客户端,所以我们继续调用它的accept，接收其他的客户端连接,最终形成一个循环.
         */
        ((AsyncTimeServerHandler)attachment).asynchronousServerSocketChannel.accept(attachment,this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        ((AsynchronousSocketChannel)result).read(buffer,buffer,new ReadCompletionHandler((AsynchronousSocketChannel)result));
    }

    @Override
    public void failed(Throwable exc, Object attachment) {
        exc.printStackTrace();
        ((AsyncTimeServerHandler)attachment).latch.countDown();
    }
}
