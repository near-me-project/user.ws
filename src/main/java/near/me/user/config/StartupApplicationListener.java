package near.me.user.config;

import circuit.breaker.RestClient;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private Environment environment;
    private RestClient restClient;
    private ServletWebServerApplicationContext webServerAppCtxt;

    @Autowired
    public StartupApplicationListener(Environment environment, ServletWebServerApplicationContext webServerAppCtxt) {
        this.environment = environment;
        this.webServerAppCtxt = webServerAppCtxt;
        restClient = new RestClient();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        final int port = webServerAppCtxt.getWebServer().getPort();
        final String applicationName = webServerAppCtxt.getApplicationName();

        String urlPath = "http://localhost:" + port + "" + applicationName;

        RegistrationForm form = RegistrationForm.builder().urlPath(urlPath).healthCheck(urlPath + "/status").build();

        restClient.async.POST(environment.getProperty("discovery.url") + "" + applicationName, form);
    }
}

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
class RegistrationForm {
    private String urlPath;
    private String healthCheck;
}
