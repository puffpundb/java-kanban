package server;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HistoryHandlerTest extends HttpTaskServerTest {
    @Test
    public void shouldServerRightGetHistory() throws IOException, InterruptedException {
        server.getTaskManagerForTest().getTaskById(0);

        List<Task> rightList = server.getTaskManagerForTest().getHistory();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        JsonArray listFromServer = JsonParser.parseString(response.body()).getAsJsonArray();
        List<Task> listFromJson = GSON.fromJson(listFromServer, new TypeToken<List<Task>>() {
        }.getType());

        Assertions.assertEquals(rightList, listFromJson);
    }
}
