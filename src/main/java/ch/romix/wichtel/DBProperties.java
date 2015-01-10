package ch.romix.wichtel;

import java.net.URI;
import java.net.URISyntaxException;

public class DBProperties {

  private String username;
  private String password;
  private String url;

  public DBProperties() {
    URI dbUri;
    try {
      username = "postgres";
      password = "postgres";
      url = "jdbc:postgresql://localhost/postgres";
      String dbProperty = System.getenv("DATABASE_URL");
      if (dbProperty != null) {
        dbUri = new URI(dbProperty);

        username = dbUri.getUserInfo().split(":")[0];
        password = dbUri.getUserInfo().split(":")[1];
        url = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
      }


    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  public String getUrl() {
    return url;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getDriverClassName() {
    return "org.postgresql.Driver";
  }
}
