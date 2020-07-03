package bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Value("${bot.lastTime}")
    private String lastTime;

    private final Map<Date, String> reminderMap = new HashMap<>();

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

        Date curDate = new Date();

        if (update.getMessage().getText().equals("Выпивка")) {

            Date messageDate = getDate(lastTime);
            if (messageDate != null && curDate.getTime() < messageDate.getTime()) {
                String text = "Парни, чё сидим?! Надо ж за полторушечкой бежать! Время-то уже вон скока!";
                reminderMap.put(messageDate, text);
            }

        } else if (update.getMessage().getText().equals("Очистить")) {
            reminderMap.clear();
        } else {

            // Тут пишем разбор сообщения и добавляем в напоминалку
            String[] partsStr = update.getMessage().getText().split("\\s", 2);
            if (partsStr.length == 2) {
                Date messageDate = getDate(partsStr[0]);
                if (messageDate != null) {

                    if (curDate.getTime() > messageDate.getTime()) {
                        // Переносим напоминалку на следующий день
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(messageDate);
                        calendar.add(Calendar.DATE, 1);
                        messageDate = calendar.getTime();
                    }
                    reminderMap.put(messageDate, partsStr[1]);

                }
            }

        }

        DateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm");
        StringBuilder sendText = new StringBuilder();
        reminderMap.forEach((d, s) -> sendText.append(df.format(d)).append(" - ").append(s).append("\n\n"));

        if (sendText.length() > 0) {
            SendMessage message = new SendMessage();
            message.setText(sendText.toString());
            message.setChatId(update.getMessage().getChatId());
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

    @Scheduled(cron = "0 05 9 * * 5") // Каждую пятницу в 9:05 утра
    public void sendVideoOnFriday() {

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

    @Scheduled(cron = "0 * * * * *") // Каждую минуту
    public void sendReminder() {

        Date curDate = new Date();
        List<Date> forDelete = new ArrayList<>();

        reminderMap.forEach((d, s) -> {

            if (curDate.getTime() >= d.getTime()) {

                SendMessage message = new SendMessage();
                message.setText(s);
                message.setChatId(chatId);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                forDelete.add(d);

            }

        });

        for (Date d:forDelete) {
            reminderMap.remove(d);
        }

    }

    public Date getDate(String strTime) {

        String[] partsTime = strTime.split(":");
        if (partsTime.length == 2) {

            Calendar today = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(
                    today.get(Calendar.YEAR),
                    today.get(Calendar.MONTH),
                    today.get(Calendar.DAY_OF_MONTH),
                    Integer.parseInt(partsTime[0]),
                    Integer.parseInt(partsTime[1]),
                    0);

            return calendar.getTime();
        } else {
            return null;
        }

    }

}
