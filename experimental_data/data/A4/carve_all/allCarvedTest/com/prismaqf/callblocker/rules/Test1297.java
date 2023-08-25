package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test1297 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#EditAPatternShouldWork/Trace-1651091691440.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.util.Set getPatternKeys()>_130_257
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getPatternKeys_003() throws Exception {
        java.lang.Object object1 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule8 = null;
        filterrule8 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule8.getPatternKeys();
        filterrule8.getPatternKeys();
        filterrule8.getName();
        filterrule8.setName("dummy");
        filterrule8.getDescription();
        filterrule8.setDescription("change me");
        filterrule8.equals(object1);
        filterrule8.getPatternKeys();
    }
}
