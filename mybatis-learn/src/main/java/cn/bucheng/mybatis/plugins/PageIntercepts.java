package cn.bucheng.mybatis.plugins;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.Properties;

@Intercepts({@Signature(
        type = StatementHandler.class,//对象类型，只能是四大对象类型
        method = "prepare", //拦截方法
        args = {Connection.class, Integer.class/*方法中用到的参数类型*/})})//可以点击拦截对象查看拦截方法的参数，
public class PageIntercepts implements Interceptor {

    public static final String BOUND_SQL = "boundSql.sql";
    private int defaultSize = 100;

    private static ThreadLocal<Integer> defaultLimit = new ThreadLocal<>();

    public static Integer getAndRemoveLimit() {
        try {
            return defaultLimit.get();
        } finally {
            defaultLimit.remove();
        }
    }

    //    拦截方法，返回结果：目标方法执行后返回的结果
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            //获取目标对象
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            //获取要执行的sql命令
            String sql = statementHandler.getBoundSql().getSql();
            System.out.println("要执行的sql命令为：" + sql);
            if (!sql.toLowerCase().startsWith("select")) {
                return invocation.proceed();
            }
            if (sql.toLowerCase().contains("limit ")) {
                return invocation.proceed();
            }
            //为sql语句添加分页
            sql += " limit 0," + defaultSize;
            //元数据对象，可以对原始数据进行操作
            MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
            //重新绑定修改后的sql语句
            metaObject.setValue(BOUND_SQL, sql);
            //为当前线程变量设置默认值
            defaultLimit.set(defaultSize);
            //执行目标方法
            return invocation.proceed();
        } catch (Throwable e) {
            defaultLimit.remove();
            throw e;
        }
    }

    @Override
    public Object plugin(Object target) {
        Object obj = Plugin.wrap(target, this);
        return obj;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}