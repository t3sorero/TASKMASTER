import java.lang.reflect.Field;
@SuppressWarnings("unchecked")
public class TestUtils {
    public static <T> T getPrivateField(Object instance, String fieldName, Class<T> type) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(instance);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void setPrivateField(Object instance, String fieldName, Object value) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
