package com.megacreep.jinotify;

import java.util.List;

final class NativeInotify {

    native int init();

    native int init1(int flag);

    native int addWatch(int fd, String path, int mask);

    native int removeWatch(int fd, int wd);

    native List<InotifyEvent> takeEvent(int fd, int wd);
}
