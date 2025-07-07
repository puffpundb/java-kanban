package server;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PrioritizedHandlerTest extends HttpTaskServerTest {
    @Test
    public void shouldServerRightGetPrioritized() throws IOException, InterruptedException {
        List<Task> rightList = server.getTaskManagerForTest().getPrioritizedTasks();
        JsonArray rightJsonArray = JsonParser.parseString(GSON.toJson(rightList)).getAsJsonArray();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        JsonArray listFromServer = JsonParser.parseString(response.body()).getAsJsonArray();

        Assertions.assertEquals(rightJsonArray, listFromServer);
    }
}
