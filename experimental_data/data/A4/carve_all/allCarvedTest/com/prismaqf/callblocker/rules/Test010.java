package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test010 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestActionOnCreating/Trace-1651091719224.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void setName(java.lang.String)>_177_350
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_setName_001() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule4 = null;
        filterrule4 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule4.getPatternKeys();
        filterrule4.getPatternKeys();
        filterrule4.getName();
        filterrule4.setName("dummy");
    }
}
