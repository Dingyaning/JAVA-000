package HttpClientDemo;

public class HttpClientTest {
    public static void main(String[] args) {
        String s = HttpClientDemo.get("http://localhost:8801/", null, null);
        System.out.println(s);
    }
}
