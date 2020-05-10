package cn.bucheng.asm;

public class Demo {
    private static final String HELLO_CONST = "Hello";
    private static String CONST = null;

    static {
        CONST = HELLO_CONST + "%s!";
    }

    public static void main(String[] args) {
        if (args != null && args.length == 1) {
            System.out.println(String.format(CONST, args[0]));
        }
    }
}