import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {

        System.out.println("1111111111");

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
