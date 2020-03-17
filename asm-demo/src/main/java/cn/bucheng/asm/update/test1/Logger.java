package cn.bucheng.asm.update.test1;

/**
 * 日志代码
 *
 * @date 2018/12/28 23:55
 */
public class Logger {

    public void enterMethod() {
        System.out.println("enter method");
    }

    public void leaveMethod() {
        System.out.println("leave method");
    }

    public static final void beforeMethod(String name) {
        int i = 1+1;
        System.out.println("i:"+i);
        System.out.println("方式二:方法开始运行1...:" + name);
    }

    public static final void afterMethod(String name) {
        int i = 1+1;
        System.out.println("i:"+i);
        System.out.println("方式二:方法运行结束2...:" + name);
    }
}
