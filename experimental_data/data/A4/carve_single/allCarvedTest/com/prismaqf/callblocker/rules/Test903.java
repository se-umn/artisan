package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test903 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#PickFromLogWithScreenRotation/Trace-1651091652973.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void setName(java.lang.String)>_106_208
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_setName_001() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule3 = null;
        filterrule3 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule3.getPatternKeys();
        filterrule3.getPatternKeys();
        filterrule3.getName();
        filterrule3.setName("dummy");
    }
}
