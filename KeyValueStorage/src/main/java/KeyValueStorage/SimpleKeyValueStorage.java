package KeyValueStorage;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SimpleKeyValueStorage<Key, Value>  implements KeyValueStorage<Key, Value> {

    private final HashMap<Key, Value> actualCopy = new HashMap<>();
    private String keyType;
    private String valueType;
    private Serializer keySerializer;
    private Serializer valueSerializer;
    private String pathDirectory;
    private String fullPath;
    private boolean isOpen;

    public SimpleKeyValueStorage(String path, String keyT, String valueT, Serializer externalKeySerializer,
                             Serializer externalValueSerializer) throws Exception {
        keyType = keyT;
        valueType = valueT;
        pathDirectory = path;
        File file = new File(pathDirectory);
        if (!file.isDirectory()) {
            throw new RuntimeException("invalid directory path");
        }
        if (!file.exists()) {
            throw new RuntimeException("such directory does not exist");
        }

        fullPath = pathDirectory + File.separator + "MyStorage";
        isOpen = true;

        keySerializer = externalKeySerializer;
        valueSerializer = externalValueSerializer;

        File file2 = new File(fullPath);
        if (!file2.exists()) {
            file2.createNewFile();
        } else {
            FileInputStream in = new FileInputStream(fullPath);
            DataInputStream fileIn = new DataInputStream(in);
            int num = fileIn.readInt(); // read number of pairs(key/value) in storage
            String fileKeyType = fileIn.readUTF(); // read the type of keys
            String fileValueType = fileIn.readUTF(); // read the type of values
            if (!fileKeyType.equals(keyType) || !fileValueType.equals(valueType)) {
                throw new RuntimeException("This file contains other types of objects");
            }
            Key key;
            Value value;
            for (int i = 0; i < num; ++i) { // read all pairs from file
                key = (Key) keySerializer.readFromFile(fileIn);
                value = (Value) valueSerializer.readFromFile(fileIn);
                actualCopy.put(key, value); // add pair to hashMap
            }
        }
    }

    public SimpleKeyValueStorage(String path, String keyT, String valueT) throws Exception {
        this(path, keyT, valueT,  SerializerFactory.takeSerializer(keyT), SerializerFactory.takeSerializer(valueT));
    }

    public Value read(Key key) {
        if (!isOpen) {
            throw new RuntimeException("Storage has been already closed");
        }
        return actualCopy.get(key);
    }

    public boolean exists(Key key) {
        if (!isOpen) {
            throw new RuntimeException("Storage has been already closed");
        }
        return actualCopy.containsKey(key);
    }

    public void write(Key key, Value value) {
        if (!isOpen) {
            throw new RuntimeException("Storage has been already closed");
        }
        actualCopy.put(key, value);
    }

    public void delete(Key key) {
        if (!isOpen) {
            throw new RuntimeException("Storage has been already closed");
        }
        actualCopy.remove(key);
    }

    public Iterator<Key> readKeys() {
        if (!isOpen) {
            throw new RuntimeException("Storage has been already closed");
        }
        return actualCopy.keySet().iterator(); // return iterator to the set of pairs
    }

    public int size() {
        return actualCopy.size();
    }

    public void close() throws IOException {
        if (!isOpen) {
            throw new RuntimeException("Storage has been already closed");
        }
        FileOutputStream out = new FileOutputStream(fullPath);
        DataOutputStream outFile = new DataOutputStream(out);
        outFile.writeInt(actualCopy.size()); // write number of pairs(key/value) in storage
        outFile.writeUTF(keyType); // write the type of keys
        outFile.writeUTF(valueType); // write the type of values
        for (Map.Entry<Key, Value> i : actualCopy.entrySet()) { // write all pairs to file
            keySerializer.writeToFile(i.getKey(), outFile);
            valueSerializer.writeToFile(i.getValue(), outFile);
        }
        isOpen = false;
        outFile.close();
        out.close();
    }
}
