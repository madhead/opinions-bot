package by.jprof.opinionsbot;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;

@Named("opinions-handler")
public class Handler
        implements RequestHandler<APIGatewayV2ProxyRequestEvent, APIGatewayV2ProxyResponseEvent> {
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);
    private static final APIGatewayV2ProxyResponseEvent OK = new APIGatewayV2ProxyResponseEvent();

    static {
        OK.setStatusCode(200);
        OK.setBody("{}");
    }

    TelegramBot bot;
    ObjectMapper mapper;

    public Handler(TelegramBot bot, ObjectMapper mapper) {
        this.bot = bot;
        this.mapper = mapper;
    }

    @Override
    public APIGatewayV2ProxyResponseEvent handleRequest(
            final APIGatewayV2ProxyRequestEvent apiGatewayV2ProxyRequestEvent,
            final Context context
    ) {
        return handlerBusinessLogic(apiGatewayV2ProxyRequestEvent);
    }

    private APIGatewayV2ProxyResponseEvent handlerBusinessLogic(
            final APIGatewayV2ProxyRequestEvent apiGatewayV2ProxyRequestEvent
    ) {
        try {
            final var body = apiGatewayV2ProxyRequestEvent.getBody();
            logger.info("Request body: {}", body);
            final var update = mapper.readValue(body, Update.class);
            logger.info("update is received, {}", update);
            final var echoText = update.message().text();
            logger.info("Echo text: {}", echoText);
            final var sendMessage = new SendMessage(update.message().chat().id(), echoText);
            bot.execute(sendMessage);
            return OK;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            final var badResponse = new APIGatewayV2ProxyResponseEvent();
            badResponse.setStatusCode(500);
            try {
                badResponse.setBody(mapper.writeValueAsString(e));
            } catch (JsonProcessingException jsonProcessingException) {
                logger.error("bad response write error",jsonProcessingException);
            }
            return badResponse;
        }
    }


}
