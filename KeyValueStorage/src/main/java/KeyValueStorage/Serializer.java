package KeyValueStorage;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


public interface Serializer<Type> {
    void writeToFile(Type object, DataOutput file) throws IOException;

    Type readFromFile(DataInput file) throws IOException;
}


