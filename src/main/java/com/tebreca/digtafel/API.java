package com.tebreca.digtafel;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class API {

    private final BrowserManager browserManager;

    @Autowired
    public API(BrowserManager manager){
        browserManager = manager;
    }

    @GetMapping("/start")
    public int start() {
        return 200;
    }

    @GetMapping("/freeze")
    public boolean freeze() {
        browserManager.freeze();
        return true;
    }

    @GetMapping("/unfreeze")
    public boolean unfreeze() {
        browserManager.unfreeze();
        return true;
    }

    @GetMapping("/open")
    public boolean open(String ip) {
        browserManager.open(ip);
        return true;
    }

    @GetMapping("/stop")
    public boolean stop() {
        return false;
    }

    @GetMapping("/status")
    public Status status() {
        return new Status(false, false);
    }

    @GetMapping("/off")
    public boolean off() {
        return false;
    }


    public static class Status {
        boolean on;

        public Status(boolean on, boolean frozen) {
            this.on = on;
            this.frozen = frozen;
        }

        public boolean isOn() {
            return on;
        }

        public void setOn(boolean on) {
            this.on = on;
        }

        public boolean isFrozen() {
            return frozen;
        }

        public void setFrozen(boolean frozen) {
            this.frozen = frozen;
        }

        boolean frozen;
    }
}
