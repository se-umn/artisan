package de.unipassau.calculator;

import java.lang.Exception;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class CarvedTest_1 {
  @Test
  public void testsendResult() throws Exception {
    // <org.robolectric.Robolectric: org.robolectric.android.controller.ActivityController buildActivity(java.lang.Class)>_-96
    org.robolectric.android.controller.ActivityController activitycontroller0 = org.robolectric.Robolectric.buildActivity(de.unipassau.calculator.MainActivity.class);
    // <org.robolectric.android.controller.ComponentController: java.lang.Object get()>_-95
    de.unipassau.calculator.MainActivity mainactivity0 = activitycontroller0.get();
    // <org.robolectric.android.controller.ActivityController: org.robolectric.android.controller.ActivityController create(android.os.Bundle)>_-94
    activitycontroller0.create();
    // <org.robolectric.android.controller.ActivityController: org.robolectric.android.controller.ActivityController start()>_-93
    activitycontroller0.start();
    // <org.robolectric.android.controller.ActivityController: org.robolectric.android.controller.ActivityController postCreate(android.os.Bundle)>_-92
    activitycontroller0.postCreate();
    // <org.robolectric.android.controller.ActivityController: org.robolectric.android.controller.ActivityController resume()>_-91
    activitycontroller0.resume();
    // <org.mockito.Mockito: java.lang.Object mock(java.lang.Class)>_-90
    android.widget.Button button0 = org.mockito.Mockito.mock(android.widget.Button.class);
    // <org.mockito.Mockito: org.mockito.stubbing.OngoingStubbing when(java.lang.Object)>_-2
    org.mockito.stubbing.OngoingStubbing ongoingstubbing0 = org.mockito.Mockito.when(button0.getId());
    // <org.mockito.stubbing.OngoingStubbing: org.mockito.stubbing.OngoingStubbing thenReturn(java.lang.Object,java.lang.Object[])>_-1
    org.mockito.stubbing.OngoingStubbing ongoingstubbing0 = ongoingstubbing0.thenReturn(2131165251);
    // <de.unipassau.calculator.MainActivity: void sendResult(android.view.View)>_12
    mainactivity0.sendResult(button0);
  }
}
