package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test507 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestSaveAction/Trace-1651091701954.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void setName(java.lang.String)>_231_461
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_setName_003() throws Exception {
        java.lang.Object object5 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule12 = null;
        filterrule12 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule12.getPatternKeys();
        filterrule12.getPatternKeys();
        filterrule12.getName();
        filterrule12.setName("dummy");
        filterrule12.getDescription();
        filterrule12.setDescription("change me");
        filterrule12.equals(object5);
        filterrule12.getPatternKeys();
        filterrule12.getPatternKeys();
        filterrule12.equals(object5);
        filterrule12.setName("");
        filterrule12.setName("a");
    }
}
