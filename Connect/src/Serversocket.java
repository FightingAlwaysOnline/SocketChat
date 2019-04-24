import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import java.util.HashMap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Serversocket {

    public static HashMap<Integer,PrintWriter> user=new HashMap<>();
    public static void main(String[] args) {

        ExecutorService threadpool= Executors.newFixedThreadPool(10);
        try {
            ServerSocket serverSocket= new ServerSocket(3344);
            Socket socket;
            while(true){
                socket=serverSocket.accept();
                threadpool.execute(new Chat(socket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}

class Chat implements Runnable{
    private Socket socket;

    public Chat(Socket s){
            this.socket=s;
    }

    @Override
    public void run() {
        try {

            BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter writer=new PrintWriter(socket.getOutputStream(),true);

            Integer nicknum=Integer.valueOf(Serversocket.user.size());
            Serversocket.user.put(nicknum,writer);

            String str="";
            msgToAll(-1,"hello every one ,"+nicknum+" is Online now");
            writer.println("hello ,your name is :"+nicknum);

            while(true){
                if (((str=reader.readLine())==null)) break;

                if(str.startsWith("@")){
                    String[] single=str.replace("@","").split(":");
                    msgToSingle(nicknum,Integer.valueOf(single[0]),single[1]);
                }else{
                    msgToAll(nicknum,str);
                }
                System.out.println(str);

            }

        } catch (IOException e) {

        }


    }

    private synchronized void msgToAll(Integer nickname,String str){

        for(PrintWriter writer:Serversocket.user.values()){
            if (str.equals("bye")){
                writer.println("Server: "+nickname+" is exited! ");
            }else if (nickname.equals(-1)){
                writer.println("Server:"+str);
            } else{
                writer.println(nickname+" said: "+str);
            }

        }
    }

    private synchronized void msgToSingle(int snum,int tnum,String msg){
        PrintWriter writer;
        if (Serversocket.user.containsKey(tnum)){
            writer= Serversocket.user.get(tnum);
            writer.println(snum+" said to you : "+msg);
        }else{
            writer= Serversocket.user.get(snum);
            writer.println("there is no user nickname is "+tnum+",send failed!");
        }
       writer.flush();
    }
}