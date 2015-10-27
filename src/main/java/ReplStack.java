import org.jgroups.*;
import org.jgroups.util.Util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 10/26/2015.
 */
public class ReplStack<T> implements Receiver {
    public JChannel jChannel;
    public final List<T> liststate = new ArrayList<>();

    private void start() throws Exception {
        jChannel = new JChannel();
        jChannel.setReceiver(this);
        jChannel.connect("13512032");
        jChannel.getState(null,10000);
        eventLoop();
        jChannel.close();
    }
    public static void main(String[] args) throws Exception {
        new ReplStack().start();
    }

    private void eventLoop() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            System.out.print("> ");
            System.out.flush();
            String line = in.readLine().toLowerCase();
            String command, commandArg = "";
            if (line.startsWith("/")) {
                // parse command
                int spaceIndex = line.indexOf(' ', 1);
                if (spaceIndex != -1) {
                    command = line.substring(1, spaceIndex);

                    if (line.length() > spaceIndex + 1) {
                        commandArg = line.substring(spaceIndex + 1);
                    }
                } else {
                    command = line.substring(1);
                }

                if (command.equalsIgnoreCase("push")) {
                    push((T) commandArg);
                } else if (command.equalsIgnoreCase("pop")) {
                    System.out.println("Result : " + pop());
                } else if (command.equalsIgnoreCase("top")) {
                    System.out.println("Result : " + top((T) commandArg));
                } else if (command.equalsIgnoreCase("exit")) {
                    break;
                }else{
                    System.out.println("Command not supported yet!");
                }

            }else {
                System.out.println("Please insert command!");
            }

        }
    }
    public void push(T obj) throws Exception {
        Message msg = new Message(null,null,obj);
        jChannel.send(msg);
    }
    public T pop() throws InterruptedException {
        if(!liststate.isEmpty()){
            T objectpop = liststate.get(liststate.size()-1);
            liststate.remove(objectpop);
            Thread.sleep(1000);
            if(!liststate.contains(objectpop)){
                return objectpop;
            }else{
                return null;
            }
        }
        else {
            return null;
        }
    }
    public T top(T obj){
        if(!liststate.isEmpty()){
            return liststate.get(liststate.size()-1);
        }
        else{
            return null;
        }
    }

    @Override
    public void viewAccepted(View view) {
        System.out.println("** view: " + view);
    }

    @Override
    public void suspect(Address address) {
        try {
            throw (new Exception("Not supported yet"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void block() {
        try {
            throw (new Exception("Not supported yet"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unblock() {
        try {
            throw (new Exception("Not supported yet"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receive(Message message) {
        String line=message.getSrc() + ": " + message.getObject();
        Object messageobj = message.getObject();
        synchronized(liststate) {
            liststate.add((T) messageobj);
        }
    }

    @Override
    public void getState(OutputStream outputStream) throws Exception {
        synchronized(liststate) {
            Util.objectToStream(liststate, new DataOutputStream(outputStream));
        }
    }

    @Override
    public void setState(InputStream inputStream) throws Exception {
        List<T> list;
        list=(List<T>)Util.objectFromStream(new DataInputStream(inputStream));
        synchronized(liststate) {
            liststate.clear();
            liststate.addAll(list);
        }
    }
}
