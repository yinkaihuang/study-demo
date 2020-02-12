package cn.bucheng.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.ArrayList;
import java.util.List;

public class LimitByCoding {
    public static void main(String[] args) throws InterruptedException {
        String resource = "HELLO_WORLD";
        initFlowRules(resource);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            Entry entry = null;
            try {
                entry = SphU.entry(resource);
                /* 您的业务逻辑 - 开始 */
                System.out.println("hello world");
                /* 您的业务逻辑 - 结束 */
            } catch (BlockException e1) {
                /* 流控逻辑处理 - 开始 */
                System.out.println("block!");
                /* 流控逻辑处理 - 结束 */
            } finally {
                if (entry != null) {
                    entry.exit();
                }
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }

    private static void initFlowRules(String resource) {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource(resource);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 20.
        rule.setCount(20);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }
}