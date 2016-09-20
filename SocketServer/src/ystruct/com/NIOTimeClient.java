package ystruct.com;

/**
 * Created by yang on 16-9-19.
 */
public class NIOTimeClient {
    public static void main(String[] args) {
        int port = 8080;
        if(args != null && args.length > 0){
            try{
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        new Thread(new TimeClientHandle("127.0.0.1",port),"TimeClient-001").start();

    }
}
