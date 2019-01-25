package com.bbz.test.model;

import java.util.Arrays;
import java.util.List;

public class User {

    private int id;

    private String name;

    private String email;

    private String password;

    private List<String> roles;

    private boolean enabled;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public User() {
    }

    public User(String name, String email, String password, List<String> roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(int id, String name, String email, String password, List<String> roles, boolean enabled) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.enabled = enabled;
    }

    public String getUrlToEmail() {
        String email = this.email;
        String mailproviderUrl = "";
        List<String> mailprovidersDomainList = Arrays.asList("@gmail.com","@yahoo.com","@hotmail.com","@aol.com","@hotmail.co.uk","@hotmail.fr","@msn.com","@yahoo.fr","@wanadoo.fr","@orange.fr","@comcast.net","@yahoo.co.uk","@yahoo.com.br","@yahoo.co.in","@live.com","@rediffmail.com","@free.fr","@gmx.de","@web.de","@yandex.ru","@ymail.com","@libero.it","@outlook.com","@uol.com.br","@bol.com.br","@mail.ru","@cox.net","@hotmail.it","@sbcglobal.net","@sfr.fr","@live.fr","@verizon.net","@live.co.uk","@googlemail.com","@yahoo.es","@ig.com.br","@live.nl","@bigpond.com","@terra.com.br","@yahoo.it","@neuf.fr","@yahoo.de","@alice.it","@rocketmail.com","@att.net","@laposte.net","@facebook.com","@bellsouth.net","@yahoo.in","@hotmail.es","@charter.net","@yahoo.ca","@yahoo.com.au","@rambler.ru","@hotmail.de","@tiscali.it","@shaw.ca","@yahoo.co.jp","@sky.com","@earthlink.net","@optonline.net","@freenet.de","@t-online.de","@aliceadsl.fr","@virgilio.it","@home.nl","@qq.com","@telenet.be","@me.com","@yahoo.com.ar","@tiscali.co.uk","@yahoo.com.mx","@voila.fr","@gmx.net","@mail.com","@planet.nl","@tin.it","@live.it","@ntlworld.com","@arcor.de","@yahoo.co.id","@frontiernet.net","@hetnet.nl","@live.com.au","@yahoo.com.sg","@zonnet.nl","@club-internet.fr","@juno.com","@optusnet.com.au","@blueyonder.co.uk","@bluewin.ch","@skynet.be","@sympatico.ca","@windstream.net","@mac.com","@centurytel.net","@chello.nl","@live.ca","@aim.com","@bigpond.net.au");
        for(String mailproviderDomen : mailprovidersDomainList) {
            if(email.endsWith(mailproviderDomen)) {
                mailproviderUrl = "https://" + mailproviderDomen;
                break;
            }
        }
        System.out.println("getUrlToEmail(): " + mailproviderUrl);
        return mailproviderUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + "......." + '\'' +
                ", roles=" + roles +
                ", enabled=" + enabled +
                '}';
    }
}