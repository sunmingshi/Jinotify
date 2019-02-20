# Jinotify

## Environment

Need Linux 2.6.14 + inotify.h

## Compile libNativeInotify.so

1 cd src/main/native

2 gcc -I $JAVA_HOME/include -I $JAVA_HOME/include/linux com_megacreep_jinotify_NativeInotify.c -fPIC -shared -o libNativeInotify.so

3 replace the 'libNativeInotify.so' if necessary

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