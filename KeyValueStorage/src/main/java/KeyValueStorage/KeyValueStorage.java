package KeyValueStorage;

import java.io.Closeable;
import java.util.Iterator;

public interface KeyValueStorage<K, V> extends Closeable {
    V read(K key);
    boolean exists(K key);
    void write(K key, V value);
    void delete(K key);
    Iterator<K> readKeys();
    int size();
//    default void flush() {
//        throw new UnsupportedOperationException("Not implemented yet");
//    }
}
