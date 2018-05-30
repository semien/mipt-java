package KeyValueStorage;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IntegerSerializer implements Serializer<Integer> {
    @Override
    public void writeToFile(Integer object, DataOutput file) throws IOException {
        file.writeInt(object);
    }

    @Override
    public Integer readFromFile(DataInput file) throws IOException {
        return file.readInt();
    }
}
