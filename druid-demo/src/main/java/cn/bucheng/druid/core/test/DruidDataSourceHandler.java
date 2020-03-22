package cn.bucheng.druid.core.test;

import com.alibaba.druid.pool.DruidDataSourceStatLogger;
import com.alibaba.druid.pool.DruidDataSourceStatLoggerAdapter;
import com.alibaba.druid.pool.DruidDataSourceStatValue;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.stat.JdbcSqlStatValue;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class DruidDataSourceHandler extends DruidDataSourceStatLoggerAdapter implements DruidDataSourceStatLogger {

    private final static Logger logger = LoggerFactory.getLogger(DruidPooledConnection.class);


    public DruidDataSourceHandler() {
        this.configFromProperties(System.getProperties());
    }

    /***DruidDataSourceStatValue这个对象包含这整个SQL监控数据，我们通过转换获取自己需要的数据**/
    @Override
    public void log(DruidDataSourceStatValue statValue) {
        List<JdbcSqlStatValue> sqlList = statValue.getSqlList();
        if (sqlList != null && sqlList.size() > 0) {
            List<LogInfo> logInfos = new ArrayList<LogInfo>(sqlList.size());
            JdbcSqlStatValue sqlStaValue = null;
            LogInfo logInfo = null;
            Throwable executeThrowable = null;
            MonitorInfo monitorInfo = new MonitorInfo();
            try {
                for (int i = 0; i < sqlList.size(); i++) {
                    sqlStaValue = sqlList.get(i);
                    logInfo = new LogInfo();
                    logInfo.setSql(sqlStaValue.getSql());
                    logInfo.setSqlParam(sqlStaValue.getLastSlowParameters());
                    logInfo.setExecuteMillisMax(sqlStaValue.getExecuteMillisMax());
                    logInfo.setMaxParallel(sqlStaValue.getConcurrentMax());
                    logInfo.setExecuteMillisTotal(sqlStaValue.getExecuteMillisTotal());
                    logInfo.setExecuteCount(sqlStaValue.getExecuteCount());
                    logInfo.setExecuteErrorCount(sqlStaValue.getExecuteErrorCount());
                    logInfo.setMaxOccurTime(sqlStaValue.getExecuteNanoSpanMaxOccurTime());
                    logInfo.setHis_0_1(sqlStaValue.getHistogram_0_1());
                    logInfo.setHis_1_10(sqlStaValue.getHistogram_1_10());
                    logInfo.setHis_10_100(sqlStaValue.getHistogram_10_100());
                    logInfo.setHis_100_1000(sqlStaValue.getHistogram_100_1000());
                    logInfo.setHis_1000_10000(sqlStaValue.getHistogram_1000_10000());
                    logInfo.setHis_10000_100000(sqlStaValue.getHistogram_10000_100000());
                    logInfo.setHis_100000_1000000(sqlStaValue.getHistogram_100000_1000000());
                    logInfo.setHis_1000000_more(sqlStaValue.getHistogram_1000000_more());
                    executeThrowable = sqlStaValue.getExecuteErrorLast();
                    if (executeThrowable != null) {
                        logInfo.setLastErrorClass(executeThrowable.getClass().getName());
                        logInfo.setLastErrorMessage(executeThrowable.getMessage());
                        logInfo.setLastErrorStackTrace(Utils.toString(executeThrowable.getStackTrace()));
                        logInfo.setLastErrorTime(sqlStaValue.getExecuteErrorLastTime());
                    }
                    logInfos.add(logInfo);
                }
            } catch (Exception e) {
                logger.error("convert monitor log exception", e);
            }
            monitorInfo.setLogInfos(logInfos);
            monitorInfo.setSysIdentify(getIdentify());
            sendMonitorLog(monitorInfo);
        }
    }

    /**
     * 发送监控信息
     **/
    public abstract void sendMonitorLog(MonitorInfo monitorInfo);

    public abstract String getIdentify();

    @Override
    public void configFromProperties(Properties properties) {
        super.configFromProperties(properties);
    }

    @Override
    public void setLogger(Log logger) {
    }

    @Override
    public void setLoggerName(String loggerName) {
        super.setLoggerName(loggerName);
    }

}