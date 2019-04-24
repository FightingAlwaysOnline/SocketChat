
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;


public class sockets {

    public static void main(String[] args) {
        Socket socket=new Socket();
        try {
            socket.connect(new InetSocketAddress("127.0.0.1",3344),60);

            PrintWriter writer=new PrintWriter(socket.getOutputStream(),true);

            BufferedReader fromServer=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(new GetServerHandler(fromServer)).start();

            BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));

            String str;

            while((str=reader.readLine())!=null){

                writer.println(str);
                writer.flush();

                if (str.equals("bye")) System.exit(1);
            }

            writer.close();

        } catch (IOException e) {
            System.out.println("Please check whether the server is opended!");
        }
    }
}
class GetServerHandler implements Runnable{
    BufferedReader fromServer;
    public GetServerHandler(BufferedReader f){
        this.fromServer=f;
    }

    @Override
    public void run() {
        String str;
        while(true){
            try {
                if((str=fromServer.readLine())!=null){
                    System.out.println(str);
                }
            } catch (IOException e) {
                System.out.println("something wrong! exit...");
                System.exit(1);
            }
        }
    }
}