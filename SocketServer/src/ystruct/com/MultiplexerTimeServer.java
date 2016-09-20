package ystruct.com;



import java.io.IOException;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by yang on 16-9-17.
 */
public class MultiplexerTimeServer implements Runnable{
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean stop;

    /**
     * 初始化多路复用器,绑定监听端口.
     * @param port
     */
    public MultiplexerTimeServer(int port){
    try{
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        //backlog 监听队列的大小为1024.
        serverSocketChannel.socket().bind(new InetSocketAddress(port),1024);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("The time server is start in port: "+port);
    }catch (IOException e){
        e.printStackTrace();
        System.exit(1);
    }
    }
    public void stop(){
        this.stop=true;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                //selector 每隔1s都被唤醒一次,
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        /*
        多路复用器关闭后,所有注册在上面的Channel和Pipe等资源都会被自动去注册并关闭，所以不需要重复释放资源.
         */
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
        private void handleInput(SelectionKey key) throws IOException{
            if(key.isValid()){
                //处理新接入放入请求消息.
                if(key.isAcceptable()){
                    //接受新的连接.
                    ServerSocketChannel ssc=(ServerSocketChannel)key.channel();
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    //把新的连接加入选择器
                    sc.register(selector,SelectionKey.OP_READ);
                }
                if(key.isReadable()){
                    //读数据
                    SocketChannel sc = (SocketChannel)key.channel();
                    ByteBuffer readBufer = ByteBuffer.allocate(1024);
                    int readBytes = sc.read(readBufer);
                    if(readBytes > 0){
                        //作用是将缓冲区当前的limit设置为position,position设置为0,用于后续的读取操作
                        readBufer.flip();
                        System.out.println("readBytes"+readBytes+" "+readBufer.remaining());
                        byte[] bytes = new byte[readBufer.remaining()];
                        readBufer.get(bytes);
                        System.out.println("bytes: "+bytes);
                        String body = new String(bytes,"UTF-8");
                        System.out.println("Now is: "+body);
                        //this.stop = true;
                    }else if(readBytes < 0){
                        //对端链路关闭
                        key.cancel();
                        sc.close();
                    }else{
                        ;
                    }
                }
    }
    }

  private void doWrite(SocketChannel channel,String response) throws IOException{
      if(response != null && response.trim().length() > 0){
          byte[] bytes = response.getBytes();
          ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
          writeBuffer.put(bytes);
          writeBuffer.flip();
          channel.write(writeBuffer);
      }
  }
}
