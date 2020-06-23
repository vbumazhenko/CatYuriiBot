package bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

@Component
public class Bot extends TelegramLongPollingBot {

    @Value("${bot.username}")
    private String botUserName;

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.imagespath}")
    private String imagesPath;

    @Value("${bot.chatid}")
    private String chatId;

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {

//        SendMessage message = new SendMessage();
//        message.setText("Привет, меня зовут Юрий!");
//        message.setChatId(update.getMessage().getChatId());
//        System.out.println(update.getMessage().getChatId());
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//        System.out.println(update.getMessage().getChatId());
//        sendMessageToChat();
    }

    //@Scheduled(cron = "03 05 9 * * 5")
    @Scheduled(cron = "00 05 5 * * 5") // +4 часа
    public void sendMessageToChat() {

        File file = new File(imagesPath + "friday1.mp4");

        SendVideo message = new SendVideo();
        message.setVideo(file);
        message.setChatId(chatId);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

}
