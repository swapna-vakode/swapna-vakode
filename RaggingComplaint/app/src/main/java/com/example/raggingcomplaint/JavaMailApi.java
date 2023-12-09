package com.example.raggingcomplaint;

import android.content.Context;
import android.os.AsyncTask;

import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import io.opencensus.internal.Utils;

public class JavaMailApi extends AsyncTask<Void,Void,Void> {
   Context context;
   Session session;
   String mail,subject,message;

    public JavaMailApi(Context context, String mail, String subject, String message) {
        this.context = context;
        this.mail = mail;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(context,"Complaint registration  mail will be sent to your registered mail-id.",Toast.LENGTH_SHORT).show();
    }

    protected Void doInBackground(Void... voids) {
        Properties properties=new Properties();
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port","465");
        properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.port","465");

      session=Session.getDefaultInstance(properties,new javax.mail.Authenticator(){
          @Override
          protected PasswordAuthentication getPasswordAuthentication() {
              return new PasswordAuthentication(Utils.EMAIL,Utils.PASSWORD);

          }
      });
try {
    MimeMessage mm=new MimeMessage(session);
    mm.setFrom(new InternetAddress(Utils.EMAIL));
    mm.addRecipient(Message.RecipientType.TO,new InternetAddress(mail));
    mm.setSubject(subject);
    mm.setText(message);
    Transport.send(mm);
}catch (MessagingException e){
    e.printStackTrace();
}
        return null;
    }

public  class Utils{
public static final  String EMAIL="raggingcomplaintproject@gmail.com",PASSWORD="ragging2020";

}
}