package KeyValueStorage;

public class SerializerFactory {
    public static Serializer takeSerializer(String str) throws Exception {
        switch (str) {
            case "Integer":
                return new IntegerSerializer();
            case "Double":
                return new DoubleSerializer();
            case "String":
                return new StringSerializer();
            default:
                throw new Exception("invalid type");
        }
    }
}
