package ch.romix.wichtel;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WichtelApp {

  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(WichtelApp.class);
    application.run(args);
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

  @Bean
  public DataSource createDataSource() {
    DBProperties properties = new DBProperties();
    DataSourceBuilder factory = DataSourceBuilder.create() //
        .driverClassName(properties.getDriverClassName()) //
        .url(properties.getUrl()) //
        .username(properties.getUsername()) //
        .password(properties.getPassword());
    return factory.build();
  }
}
