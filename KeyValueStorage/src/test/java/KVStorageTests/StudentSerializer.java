package KVStorageTests;

import KeyValueStorage.Serializer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;


public class StudentSerializer implements Serializer<Student> {
    @Override
    public void writeToFile(Student object, DataOutput file) throws IOException {
        file.writeInt(object.getGroupId());
        file.writeUTF(object.getName());
        file.writeUTF(object.getHometown());
        file.writeLong(object.getBirthDate().getTime()); // date -> (long)milliseconds
        file.writeBoolean(object.isHasDormitory());
        file.writeDouble(object.getAverageScore());
    }

    @Override
    public Student readFromFile(DataInput file) throws IOException {
        Integer groupId = file.readInt();
        String name = file.readUTF();
        String homeTown = file.readUTF();
        Date date = new Date(file.readLong()); // (long)milliseconds -> date
        Boolean hasDormitory = file.readBoolean();
        Double averageScore = file.readDouble();
        return new Student(groupId, name, homeTown, date, hasDormitory, averageScore);
    }

}
