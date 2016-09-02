package com.redhat.gss;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyAuthenticatorServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {

        System.out.println("ProxyAuthenticatorServlet.init()");

        String proxyUser = System.getProperty("http.proxyUser");
        String proxyPassword = System.getProperty("http.proxyPassword");

        if (proxyUser == null) {
            System.out.println("http.proxyUser is not configured");
        } else if (proxyPassword == null) {
            System.out.println("http.proxyPassword is not configured");
        } else {
            System.out.println("Setting ProxyAuthenticator");
            Authenticator.setDefault(new ProxyAuthenticator(proxyUser, proxyPassword));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("This servelt is just used for load-on-startup");
    }

    class ProxyAuthenticator extends Authenticator {

        private String user, password;

        public ProxyAuthenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, password.toCharArray());
        }
    }
}
