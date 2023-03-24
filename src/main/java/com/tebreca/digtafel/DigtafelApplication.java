package com.tebreca.digtafel;

import com.google.gson.Gson;
import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.CefInitializationException;
import me.friwi.jcefmaven.UnsupportedPlatformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

@AutoConfiguration
@SpringBootApplication
public class DigtafelApplication {

    private static final BrowserManager browserManager = new BrowserManager();
    private static String domain;
    private static int preferredId;
    private final Gson gson = new Gson();

    @Bean
    static BrowserManager manager() {
        return browserManager;
    }

    public static String getServer() {
        return server;
    }

    private static String server;

    public static void main(String[] args) {

        SpringApplicationBuilder builder = new SpringApplicationBuilder(DigtafelApplication.class);

        builder.headless(false);

        ConfigurableApplicationContext context = builder.run(args);
    }

    @Scheduled(fixedDelay = 1000)
    public static void setup() throws InterruptedException {
        if (server != null)
            return;
        String ip = "";
        boolean connected = false;
        for (int i = 0; i <= 255; i++) {
            ip = domain + i + "/";
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(ip, 8080));
                connected = true;
                break;
            } catch (Exception ignored) {
            }
        }
        if (connected) {
            server = ip;
            connect();
        }
    }

    private static void connect() {
        WebClient client = WebClient.create();
        WebClient.ResponseSpec response = client.get().uri(server + "/ready?id=" + preferredId).retrieve();//TODO
    }


    public DigtafelApplication() throws Exception {
        File config = new File("./config.json");
        if (config.exists()) {
            try (FileReader reader = new FileReader(config)) {
                Config config1 = gson.fromJson(reader, Config.class);
                domain = config1.getDomain();
                preferredId = config1.getId();
            }
        } else {
            if (!config.createNewFile()){
                 System.exit(-1);
            }
            try(FileWriter fileWriter = new FileWriter(config)){
                gson.toJson(new Config(), fileWriter);
            }
        }


        browserManager.setup();


    }

    public static class Config {

        int id;

        String domain;

        public Config() {
            this(1, "http://192.168.0.");
        }

        public Config(int id, String domain) {
            this.id = id;
            this.domain = domain;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }
    }
}
