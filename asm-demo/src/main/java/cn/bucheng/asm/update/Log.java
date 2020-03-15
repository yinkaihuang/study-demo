package cn.bucheng.asm.update;

/**
 * 日志代码
 * @date 2018/12/28 23:55
 */
public class Log {

    public void enterMethod(){
        System.out.println("enter method");
    }

    public void leaveMethod(){
        System.out.println("leave method");
    }

    public static final void beforeMethod() {
        System.out.println("方式二:方法开始运行...");
    }

    public static final void afterMethod() {
        System.out.println("方式二:方法运行结束...");
    }
}
