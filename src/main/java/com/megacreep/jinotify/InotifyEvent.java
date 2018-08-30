package com.megacreep.jinotify;

public class InotifyEvent {
    private int wd;
    private int mask;
    private int cookie;
    private int len;
    private String name;

    public InotifyEvent() {
    }

    public InotifyEvent(int wd, int mask, int cookie, int len, String name) {
        this.wd = wd;
        this.mask = mask;
        this.cookie = cookie;
        this.len = len;
        this.name = name;
    }

    public int getWd() {
        return wd;
    }

    public void setWd(int wd) {
        this.wd = wd;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public int getCookie() {
        return cookie;
    }

    public void setCookie(int cookie) {
        this.cookie = cookie;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "InotifyEvent{"
                + "wd=" + wd
                + ", mask=" + mask
                + ", cookie=" + cookie
                + ", len=" + len
                + ", name='" + name + '\''
                + '}';
    }
}
