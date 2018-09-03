# Jinotify
The JNI implements of Linux sys/inotify.h

## Environment
Need Linux 2.6.14 + inotify.h

inotify是Linux核心子系统之一，做为文件系统的附加功能，它可监控文件系统并将异动通知应用程序。更新目录查看、重新加载配置文件、追踪变更、备份、同步甚至上传等许多自动化作业流程，都可因而受惠。

## Compile so

1 cd src/main/native

2 gcc -I $JAVA_HOME/include -I $JAVA_HOME/include/linux com_megacreep_jinotify_NativeInotify.c -fPIC -shared -o libNativeInotify.so

## 内存占用

1 native代码在JVM线程中被调用，所需内存由JVM堆内存分配，在代码执行结束后，由JVM负责回收


## Example

```java

public class Demo {

    public static void main(String...args) {
        Inotify inotify = new Inotify();
        inotify.init();
        inotify.addWatch("/path/to/watch/dir",Mask.IN_ACCESS);
        List<InotifyEvent> events = inotify.takeEvent();// blocked
        for(InotifyEvent event : events) {
            //do something
            System.out.println(event);
        }
        int code = inotify.removeWatch();
    }
}

```

