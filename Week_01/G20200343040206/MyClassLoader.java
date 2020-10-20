import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;

public class MyClassLoader extends ClassLoader {
    public static void main(String[] args) throws Exception {
        File file = new File("E:/myjava/JAVA-000/Week_01/G20200343040206/Hello.xlass");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FileInputStream fileInputStream = new FileInputStream(file);

        int b = 0;
        while ((b = fileInputStream.read()) != -1) {
            byteArrayOutputStream.write(255-b);
        }
        fileInputStream.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        Class<?> aClass = new MyClassLoader().defineClass("Hello", bytes, 0, bytes.length);
        Object obj = aClass.newInstance();
        Method method = aClass.getMethod("hello");
        method.invoke(obj);
    }
}
