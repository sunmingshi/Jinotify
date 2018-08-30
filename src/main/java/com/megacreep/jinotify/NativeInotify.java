package com.megacreep.jinotify;

import java.util.List;

public class NativeInotify {

    /**
     * @return fd
     */
    native int init();

    native int init1(int flag);

    /**
     * @param fd
     * @param path
     * @param mask
     * @return wd
     */
    native int addWatch(int fd, String path, int mask);

    native int removeWatch(int fd, int wd);

    native List<InotifyEvent> takeEvent(int fd, int wd);
}
