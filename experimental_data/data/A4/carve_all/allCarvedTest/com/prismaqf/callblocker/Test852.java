package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test852 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestUpdateActionValidation/Trace-1651091659983.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditFilterPatterns: void onCreate(android.os.Bundle)>_391_782
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditFilterPatterns_onCreate_002() throws Exception {
        java.util.Set set144 = null;
        java.util.HashSet hashset2 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule136 = null;
        java.util.ArrayList arraylist116 = null;
        android.content.Intent intent21 = null;
        java.lang.Object object44 = null;
        android.os.Parcel parcel387 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber423 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel388 = stubber423.when(parcel387);
        parcel388.writeString("dummy");
        org.mockito.stubbing.Stubber stubber424 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel389 = stubber424.when(parcel387);
        parcel389.writeString("change me");
        org.mockito.stubbing.Stubber stubber425 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel390 = stubber425.when(parcel387);
        parcel390.writeInt(0);
        org.mockito.stubbing.Stubber stubber426 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel391 = stubber426.when(parcel387);
        parcel391.writeString("dummy");
        org.mockito.stubbing.Stubber stubber427 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel392 = stubber427.when(parcel387);
        parcel392.writeString("change me");
        org.mockito.stubbing.Stubber stubber428 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel393 = stubber428.when(parcel387);
        parcel393.writeInt(0);
        com.prismaqf.callblocker.rules.FilterRule filterrule137 = org.mockito.Mockito.mock(com.prismaqf.callblocker.rules.FilterRule.class);
        org.mockito.stubbing.Stubber stubber429 = org.mockito.Mockito.doReturn(set144);
        com.prismaqf.callblocker.rules.FilterRule filterrule138 = stubber429.when(filterrule137);
        filterrule138.getPatternKeys();
        org.mockito.stubbing.Stubber stubber430 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.FilterRule filterrule139 = stubber430.when(filterrule137);
        filterrule139.addPattern("1-23*4+5)6");
        org.mockito.stubbing.Stubber stubber431 = org.mockito.Mockito.doReturn(set144);
        com.prismaqf.callblocker.rules.FilterRule filterrule140 = stubber431.when(filterrule137);
        filterrule140.getPatternKeys();
        android.os.Parcel parcel394 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber432 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel395 = stubber432.when(parcel394);
        parcel395.writeString("dummy");
        org.mockito.stubbing.Stubber stubber433 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel396 = stubber433.when(parcel394);
        parcel396.writeString("change me");
        org.mockito.stubbing.Stubber stubber434 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel397 = stubber434.when(parcel394);
        parcel397.writeInt(0);
        android.os.Bundle bundle87 = org.mockito.Mockito.mock(android.os.Bundle.class);
        org.mockito.stubbing.Stubber stubber435 = org.mockito.Mockito.doNothing();
        android.os.Bundle bundle88 = stubber435.when(bundle87);
        bundle88.putParcelable("com.prismaqft.callblocker:ptrule", filterrule140);
        org.mockito.stubbing.Stubber stubber436 = org.mockito.Mockito.doNothing();
        android.os.Bundle bundle89 = stubber436.when(bundle87);
        bundle89.putParcelable("com.prismaqft.callblocker:keyorig", filterrule136);
        org.mockito.stubbing.Stubber stubber437 = org.mockito.Mockito.doNothing();
        android.os.Bundle bundle90 = stubber437.when(bundle87);
        bundle90.putStringArrayList("com.prismaqf.callblocker:checked", arraylist116);
        android.content.Context context43 = ApplicationProvider.getApplicationContext();
        filterrule136 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule136.getPatternKeys();
        filterrule136.getPatternKeys();
        filterrule136.getName();
        filterrule136.setName("dummy");
        filterrule136.getDescription();
        filterrule136.setDescription("change me");
        filterrule136.equals(object44);
        filterrule136.getPatternKeys();
        filterrule136.getPatternKeys();
        filterrule136.equals(object44);
        intent21 = new android.content.Intent(context43, com.prismaqf.callblocker.EditFilterPatterns.class);
        android.content.Intent intent22 = intent21.putExtra("com.prismaqft.callblocker:ptrule", (android.os.Parcelable) filterrule136);
        android.content.Intent intent23 = intent22.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) filterrule136);
        filterrule136.writeToParcel(parcel393, 0);
        filterrule136.writeToParcel(parcel393, 0);
        intent23.getExtras();
        com.prismaqf.callblocker.rules.FilterRule filterrule141 = (com.prismaqf.callblocker.rules.FilterRule) intent23.getParcelableExtra("com.prismaqft.callblocker:keyorig");
        filterrule140.getPatternKeys();
        hashset2 = new java.util.HashSet();
        filterrule140.equals(filterrule141);
        filterrule141.writeToParcel(parcel397, 0);
        filterrule140.equals(filterrule141);
        filterrule140.addPattern("1-23*4+5)6");
        filterrule140.getPatternKeys();
        filterrule140.equals(filterrule141);
        hashset2.contains("123*456");
        hashset2.contains("123*456");
        bundle90.putParcelable("com.prismaqft.callblocker:ptrule", filterrule140);
        bundle90.putParcelable("com.prismaqft.callblocker:keyorig", filterrule141);
        arraylist116 = new java.util.ArrayList(hashset2);
        bundle90.putStringArrayList("com.prismaqf.callblocker:checked", arraylist116);
        org.robolectric.android.controller.ActivityController activitycontroller35 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditFilterPatterns.class, intent23);
        activitycontroller35.get();
        activitycontroller35.create();
    }
}
