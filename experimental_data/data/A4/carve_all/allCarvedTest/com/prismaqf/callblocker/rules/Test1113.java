package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test1113 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#PickFromLog/Trace-1651091669275.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.util.Set getPatternKeys()>_135_267
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getPatternKeys_004() throws Exception {
        java.lang.Object object2 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule9 = null;
        filterrule9 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule9.getPatternKeys();
        filterrule9.getPatternKeys();
        filterrule9.getName();
        filterrule9.setName("dummy");
        filterrule9.getDescription();
        filterrule9.setDescription("change me");
        filterrule9.equals(object2);
        filterrule9.getPatternKeys();
        filterrule9.getPatternKeys();
    }
}
