package by.jprof.opinionsbot.config;

import com.pengrad.telegrambot.TelegramBot;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import static java.lang.System.getenv;
import static java.util.Objects.requireNonNull;

public class TelegramBotConfig {

    public static final String token = getenv(EnvironmentKeys.TELEGRAM_BOT_TOKEN);

    @Produces
    @ApplicationScoped
    TelegramBot telegramBot() {
        return new TelegramBot(requireNonNull(token));
    }

}
