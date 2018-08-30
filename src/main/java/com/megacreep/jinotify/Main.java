package com.megacreep.jinotify;

import java.util.List;

public class Main {

    private static NativeInotify nativeInotify;

    public static void main(String[] args) throws Exception {

        System.loadLibrary("NativeInotify");

        nativeInotify = new NativeInotify();
        int fd = nativeInotify.init();
        System.out.println("fd is " + fd);
        int wd = nativeInotify.addWatch(fd, args[0], Mask.IN_ACCESS | Mask.IN_CREATE);
        System.out.println("wd is " + wd);
        for (int i = 0; i < 5; i++) {
            List<InotifyEvent> events = nativeInotify.takeEvent(fd, wd);
            System.out.println(events);
        }
        int ret = nativeInotify.removeWatch(fd, wd);
        System.out.println("ret is " + ret);
    }
}
