package KeyValueStorage;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class StringSerializer implements Serializer<String> {
    @Override
    public void writeToFile(String object, DataOutput file) throws IOException {
        file.writeUTF(object);
    }

    @Override
    public String readFromFile(DataInput file) throws IOException {
        return file.readUTF();
    }
}
