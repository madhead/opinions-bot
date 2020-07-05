package by.jprof.opinionsbot;

import by.jprof.opinionsbot.config.MarshallingConfig;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;
import com.pengrad.telegrambot.TelegramBot;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class HandlerTest {

    public static final String UPDATE = "{\n" +
            "  \"update_id\": 402422594,\n" +
            "  \"message\": {\n" +
            "    \"message_id\": 219,\n" +
            "    \"from\": {\n" +
            "      \"id\": 133429192,\n" +
            "      \"is_bot\": false,\n" +
            "      \"first_name\": \"Waldemar\",\n" +
            "      \"last_name\": \"Tsiamruk\",\n" +
            "      \"username\": \"wtsiamruk\",\n" +
            "      \"language_code\": \"en\"\n" +
            "    },\n" +
            "    \"chat\": {\n" +
            "      \"id\": 133429192,\n" +
            "      \"first_name\": \"Waldemar\",\n" +
            "      \"last_name\": \"Tsiamruk\",\n" +
            "      \"username\": \"wtsiamruk\",\n" +
            "      \"type\": \"private\"\n" +
            "    },\n" +
            "    \"date\": 1593452959,\n" +
            "    \"text\": \"/remove_from_whitelist https://www.youtube.com/c/maxkatz1\",\n" +
            "    \"entities\": [\n" +
            "      {\n" +
            "        \"offset\": 0,\n" +
            "        \"length\": 22,\n" +
            "        \"type\": \"bot_command\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"offset\": 23,\n" +
            "        \"length\": 34,\n" +
            "        \"type\": \"url\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    @Inject TelegramBot bot;
    @Inject Handler handler;

    @Test
    void handlerReturnsOkIfNoErrorsOccurs() {
        final var apiRequest = new APIGatewayV2ProxyRequestEvent();
        apiRequest.setBody(UPDATE);
        final var mockBot = Mockito.mock(TelegramBot.class);
        QuarkusMock.installMockForType(mockBot, TelegramBot.class);

        final var response = handler.handleRequest(apiRequest, null);

        assertEquals("{}", response.getBody());
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void onSomeErrorReturnsErrorCode() {
        final var apiRequest = new APIGatewayV2ProxyRequestEvent();
        apiRequest.setBody("{}"); // bad body
        final var mockBot = Mockito.mock(TelegramBot.class);
        QuarkusMock.installMockForType(mockBot, TelegramBot.class);

        final var response = handler.handleRequest(apiRequest, null);

        assertEquals(500, response.getStatusCode());
    }


}