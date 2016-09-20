package ystruct.com;

/**
 * Created by yang on 16-9-17.
 */
public class NIOTimeServer {
    public static void main(String[] args) {
        int port = 8080;
        if(args != null && args.length > 0){
            try{
                port=Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                port = 8080;
            }
        }
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
         new Thread(timeServer,"NIO-MultiplexerTimeServer-001").start();

    }

}
