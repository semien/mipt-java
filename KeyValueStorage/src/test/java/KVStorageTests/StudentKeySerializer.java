package KVStorageTests;

import KeyValueStorage.Serializer;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class StudentKeySerializer implements Serializer<StudentKey> {

    @Override
    public void writeToFile(StudentKey object, DataOutput file) throws IOException {
        file.writeInt(object.getGroupId()); // write all fields in series
        file.writeUTF(object.getName());
    }

    @Override
    public StudentKey readFromFile(DataInput file) throws IOException {
        Integer groupId = file.readInt();
        String name = file.readUTF();
        return new StudentKey(groupId, name);
    }
}