package email.java.model;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Date;
import java.util.Map;

import com.auxilii.msgparser.*;

import email.java.config.LoadConfig;

public class Email {

    String PRIORITY_EMAIL = "anthoine.prefontaine@racicotchandonnet.ca";

    PersonInfo recipient;
    PersonInfo sender;
    String dateReceived;

    File currentEmail;

    public Email(File file) {
        currentEmail = file;
    }

    public void readFile() throws UnsupportedOperationException, IOException {

        Map<String, String> filtre = LoadConfig.getInstance().getFilters();

        MsgParser msgp = new MsgParser();

        Message msg = msgp.parseMsg(currentEmail.getPath());

        String fromEmail = msg.getFromEmail();
        String fromName = filtre.containsKey(fromEmail) ? filtre.get(fromEmail) : msg.getFromName();

        String toEmail = "";
        String toName = "";

        if (fromEmail == PRIORITY_EMAIL) {
            toEmail = msg.getToEmail();
            toName = filtre.containsKey(toEmail) ? filtre.get(toEmail) : msg.getToName();
        } else {
            RecipientEntry recipient = findPriorityUser(msg.getRecipients());

            toEmail = recipient.getToEmail();
            toName = filtre.containsKey(toEmail) ? filtre.get(toEmail) : recipient.getToName();
        }

        recipient = new PersonInfo(toName, toEmail);
        sender = new PersonInfo(fromName, fromEmail);
        setDate(msg.getDate());

    }

    private void setDate(Date dateToModify) {
        String inputDateString = dateToModify.toString();
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy", Locale.US);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yy-MM-dd");

        try {
            Date date = inputDateFormat.parse(inputDateString);
            dateReceived = outputDateFormat.format(date);

            System.out.println(dateReceived);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RecipientEntry findPriorityUser(List<RecipientEntry> recipients) {

        for (RecipientEntry recipientEntry : recipients) {

            if (recipientEntry.getToEmail().equals(PRIORITY_EMAIL)) {
                return recipientEntry;
            }

        }
        return null;
    }

    public String fileName() {

        return String.format("%s %s %s %s - .msg", dateReceived, sender.name, String.valueOf('\u00E0'),
                recipient.name);
    }
}
