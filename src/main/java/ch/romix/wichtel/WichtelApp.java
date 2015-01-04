package ch.romix.wichtel;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WichtelApp {

  public static void main(String[] args) {
    SpringApplication.run(WichtelApp.class, args);
  }

  @Bean
  public JavaMailSender mailSender() {
    String host = System.getenv("wichtel.smtp.host");
    String user = System.getenv("wichtel.user");
    String password = System.getenv("wichtel.password");
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
    javaMailSender.setJavaMailProperties(props);
    javaMailSender.setProtocol("smtp");
    javaMailSender.setHost(host);
    javaMailSender.setPort(587);
    javaMailSender.setUsername(user);
    javaMailSender.setPassword(password);
    return javaMailSender;
  }
}
