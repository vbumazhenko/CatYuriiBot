import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {

        SendMessage message = new SendMessage();
        message.setText("Привет, меня зовут Юрий!");
        message.setChatId(update.getMessage().getChatId());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotUsername() {
        return "catYuriiBot";
    }

    @Override
    public String getBotToken() {
        return "703222619:AAGE1s9H7EF7LA5jqjFYZ5Yn_TAXm1K8jVo";
    }
}
