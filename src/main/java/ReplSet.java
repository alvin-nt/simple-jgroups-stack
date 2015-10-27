import org.jgroups.*;
import org.jgroups.util.Util;

import java.io.*;
import java.util.*;

public class ReplSet<T> extends ReceiverAdapter implements Closeable {
    public static final int DEFAULT_TIMEOUT = 10000;

    public JChannel channel;

    public final Set<T> stateSet = new HashSet<>();

    protected final List<Address> members = new ArrayList<>();

    public ReplSet(String clusterName) throws Exception {
        channel = new JChannel();

        channel.setReceiver(this);

        channel.connect(clusterName);
        channel.getState(null, DEFAULT_TIMEOUT);
    }

    public boolean add(T obj) throws Exception {
        if (!stateSet.contains(obj)) {
            SetCommand<T> command = new SetCommand<>(SetCommand.ADD, obj);
            byte[] buf = Util.streamableToByteBuffer(command);

            channel.send(new Message(null, null, buf));

            return true;
        }

        return false;
    }

    public boolean contains(T obj) {
        return stateSet.contains(obj);
    }

    public boolean remove(T obj) throws Exception {
        if (stateSet.contains(obj)) {
            SetCommand<T> command = new SetCommand<>(SetCommand.REMOVE, obj);
            byte[] buf = Util.streamableToByteBuffer(command);

            channel.send(new Message(null, null, buf));

            return true;
        }

        return false;
    }

    @Override
    public void viewAccepted(View view) {
        members.clear();
        members.addAll(view.getMembers());

        System.out.println("** view: " + view);

        if (view instanceof MergeView) {
            System.out.println("** MergeView: " + view);

        }
    }

    @Override
    public void receive(Message message) {
        try {
            System.out.printf("[%s] Message received from %s\n", channel.getAddressAsString(), message.getSrc());

            SetCommand command;
            command = (SetCommand) Util.streamableFromByteBuffer(SetCommand.class, message.getRawBuffer());

            synchronized (stateSet) {
                switch (command.mode) {
                    case SetCommand.ADD:
                        stateSet.add(command.getObject());
                        break;
                    case SetCommand.REMOVE:
                        stateSet.remove(command.getObject());
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getState(OutputStream outputStream) throws Exception {
        synchronized (stateSet) {
            Util.objectToStream(stateSet, new DataOutputStream(outputStream));
        }
    }

    @Override
    public void setState(InputStream inputStream) throws Exception {
        Set<T> set;

        try {
            set = (Set<T>) Util.objectFromStream(new DataInputStream(inputStream));

            synchronized (stateSet) {
                stateSet.clear();
                stateSet.addAll(set);
            }
        } catch (ClassCastException e) {
            System.err.printf("[%s] Invalid state received. Class: %s\n", channel.getAddressAsString(), e.getClass());
        }
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }
}
