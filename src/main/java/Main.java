import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class Main {

    private static final String URL = "http://94.198.50.185:7081/api/users";
    private static final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        String sessionId = getSessionId();
        HttpHeaders headers = HeadersWithCookies(sessionId);

        String responsePost = sendRequestWithBody(HttpMethod.POST, URL, headers, "{\"id\":3,\"name\":\"James\",\"lastName\":\"Brown\",\"age\":30}");

        String responsePut = sendRequestWithBody(HttpMethod.PUT, URL, headers, "{\"id\":3,\"name\":\"Thomas\",\"lastName\":\"Shelby\",\"age\":30}");

        String deleteUrl = URL + "/3";
        String responseDelete = sendRequestWithBody(HttpMethod.DELETE, deleteUrl, headers, "{\"name\":\"Thomas\",\"lastName\":\"Shelby\"}");
        System.out.println( responsePost + responsePut + responseDelete);
    }

    private static String getSessionId() {
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET, HttpEntity.EMPTY, String.class);
        List<String> cookies = response.getHeaders().get("Set-Cookie");
        if (cookies != null) {
            for (String cookie : cookies) {
                if (cookie.startsWith("JSESSIONID")) {
                    return cookie.split(";")[0];
                }
            }
        }
        throw new IllegalStateException("No session ID found in response");
    }

    private static HttpHeaders HeadersWithCookies(String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", sessionId);
        return headers;
    }

    private static String sendRequestWithBody(HttpMethod method, String url, HttpHeaders headers, String body) {
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, method, requestEntity, String.class);
        return response.getBody();
    }
}
