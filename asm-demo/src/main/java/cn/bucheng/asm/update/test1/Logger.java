package cn.bucheng.asm.update.test1;

/**
 * 日志代码
 *
 * @date 2018/12/28 23:55
 */
public class Logger {

    public static final void beforeMethod(String name) {
        System.out.println("方式二:方法开始运行...:" + name);
    }

    public static final void afterMethod(String name) {
        System.out.println("方式二:方法运行结束...:" + name);
    }
}
