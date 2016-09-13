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

        String httpProxyUser = System.getProperty("http.proxyUser");
        String httpProxyPassword = System.getProperty("http.proxyPassword");
        String httpsProxyUser = System.getProperty("https.proxyUser");
        String httpsProxyPassword = System.getProperty("https.proxyPassword");

        System.out.println("Setting ProxyAuthenticator");
        Authenticator.setDefault(new ProxyAuthenticator(httpProxyUser, httpProxyPassword, httpsProxyUser, httpsProxyPassword));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("This servelt is just used for load-on-startup");
    }

    class ProxyAuthenticator extends Authenticator {

        private String httpProxyUser;
        private String httpProxyPassword;
        private String httpsProxyUser;
        private String httpsProxyPassword;

        public ProxyAuthenticator(String httpProxyUser, String httpProxyPassword, String httpsProxyUser, String httpsProxyPassword) {
            this.httpProxyUser = httpProxyUser;
            this.httpProxyPassword = httpProxyPassword;
            this.httpsProxyUser = httpsProxyUser;
            this.httpsProxyPassword = httpsProxyPassword;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            if (getRequestorType() == RequestorType.PROXY) {
                String protocol = getRequestingProtocol();

                if (protocol.equalsIgnoreCase("http")) {
                    return new PasswordAuthentication(httpProxyUser, httpProxyPassword.toCharArray());
                } else if (protocol.equalsIgnoreCase("https")) {
                    return new PasswordAuthentication(httpsProxyUser, httpsProxyPassword.toCharArray());
                }
            }
            return null;
        }
    }
}
