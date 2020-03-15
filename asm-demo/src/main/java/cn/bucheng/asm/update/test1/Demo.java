package cn.bucheng.asm.update.test1;

/**
 * @date 2018/12/28 23:36
 */
public class Demo {
    public void getDemoInfo(String name) {
//        Logger.beforeMethod(name);
        System.out.println("getDemoInfo被调用...:"+name);
//        Logger.afterMethod(name);
    }
}
