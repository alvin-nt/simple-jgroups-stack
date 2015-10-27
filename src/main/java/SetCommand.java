import org.jgroups.util.Streamable;
import org.jgroups.util.Util;

import java.io.DataInput;
import java.io.DataOutput;

/**
 * Created by Alvin Natawiguna on 10/27/2015.
 */
public class SetCommand<T> implements Streamable {
    public static final byte ADD = 1;
    public static final byte REMOVE = 2;

    byte mode;
    T object;

    public SetCommand() { // for streamable
        mode = -1;
        object = null;
    }

    public SetCommand(byte mode, T object) {
        assert(object != null);

        this.mode = mode;
        this.object = object;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        switch (mode) {
            case ADD:
                sb.append("ADD ").append(object);
                break;
            case REMOVE:
                sb.append("REMOVE ").append(object);
                break;
            default:
                sb.append("<undefined>");
        }

        return sb.toString();
    }

    public T getObject() {
        return object;
    }

    @Override
    public void writeTo(DataOutput out) throws Exception {
        out.write(mode);
        Util.objectToStream(object, out);
    }

    @Override
    public void readFrom(DataInput in) throws Exception {
        mode = in.readByte();
        object = (T) Util.objectFromStream(in);
    }
}