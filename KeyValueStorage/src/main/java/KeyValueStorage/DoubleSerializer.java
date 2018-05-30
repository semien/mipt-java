package KeyValueStorage;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by ${Semien} on ${30.10.16}.
 */
public class DoubleSerializer implements Serializer<Double> {
    @Override
    public void writeToFile(Double object, DataOutput file) throws IOException {
        file.writeDouble(object);
    }

    @Override
    public Double readFromFile(DataInput file) throws IOException {
        return file.readDouble();
    }
}
