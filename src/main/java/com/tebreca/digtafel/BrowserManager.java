package com.tebreca.digtafel;

import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.CefInitializationException;
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter;
import me.friwi.jcefmaven.UnsupportedPlatformException;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class BrowserManager extends MavenCefAppHandlerAdapter {

    private CefApp cefApp;
    private CefClient cefClient;
    private CefBrowser browser;

    private boolean frozen = false;

    private MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (frozen){
                e.consume();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (frozen){
                e.consume();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (frozen){
                e.consume();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (frozen){
                e.consume();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (frozen){
                e.consume();
            }
        }
    };

    private final JFrame window = new JFrame();

    public void setup() throws UnsupportedPlatformException, CefInitializationException, IOException, InterruptedException {
        CefAppBuilder builder = new CefAppBuilder();
        builder.setAppHandler(this);
        cefApp = builder.build();
        cefClient = cefApp.createClient();
        browser = cefClient.createBrowser("https://www.google.com", true, false);
        Component component = browser.getUIComponent();
        component.addMouseListener(mouseListener);
        window.add(component);
        window.setAlwaysOnTop(true);
        window.setFocusable(false);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setUndecorated(true);
        window.setVisible(true);
    }

    public void open(String url) {
        browser.loadURL(url);
    }

    public void freeze(){
        this.frozen = true;
    }

    public void unfreeze(){
        this.frozen = false;
    }


}
