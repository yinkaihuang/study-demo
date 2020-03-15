package cn.bucheng.springmybatisdemo.pluging;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Properties;

@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        }
)
public class MyPageInterceptor implements Interceptor {

    private Integer defaultLimit = 5;

    //    拦截方法，返回结果：目标方法执行后返回的结果
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        RowBounds rowBounds = (RowBounds) args[2];
        ResultHandler resultHandler = (ResultHandler) args[3];
        Executor executor = (Executor) invocation.getTarget();
        CacheKey cacheKey;
        BoundSql boundSql;
        //由于逻辑关系，只会进入一次
        if (args.length == 4) {
            //4 个参数时
            boundSql = ms.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
        } else {
            //6 个参数时
            cacheKey = (CacheKey) args[4];
            boundSql = (BoundSql) args[5];
        }

        //获取要执行的sql命令
        String sql = boundSql.getSql();
        System.out.println("要执行的sql命令为：" + sql);
        boolean check = false;
        String sqlTemp = sql.toLowerCase().trim();
        if (sqlTemp.startsWith("select") && !sqlTemp.contains("limit") && !sqlTemp.startsWith("select count(")) {
            sql += " limit 0," + defaultLimit;
            check = true;
        }
        BoundSql checkSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), parameter);
        //执行目标方法
        List resultList = executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, checkSql);
        if (check) {
            if (resultList != null && resultList.size() == defaultLimit) {
                throw new RuntimeException(sqlTemp+" 查询出来数据大于默认设置:"+defaultLimit+",请使用分页查询避免出现OOM");
            }
        }
        return resultList;

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