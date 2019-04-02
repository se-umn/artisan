package com.farmerbb.notepad.activity;

import java.lang.Exception;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

@RunWith(RobolectricTestRunner.class)
public class CarvedTest_1 {
  @Test
  public void testgetCabArray() throws Exception {
    android.app.Activity activity0 = null;
    android.view.View view0 = null;
    android.view.View view1 = null;
    boolean boolean0;
    java.util.List list0 = null;
    com.farmerbb.notepad.activity.MainActivity mainactivity0 = null;
    int int0;
    java.lang.Class class0;
    android.widget.ArrayAdapter arrayadapter0 = null;
    int int1;
    com.farmerbb.notepad.util.NoteListItem[] notelistitem0 = null;
    int int2;
    android.os.Bundle bundle0 = null;
    int int3;
    android.widget.ArrayAdapter arrayadapter1 = null;
    java.lang.Class class1;
    int int4;
    android.content.ContextWrapper contextwrapper0 = null;
    java.util.List list1 = null;
    int int5;
    java.io.File file0 = null;
    android.view.View view2 = null;
    java.lang.String string0;
    java.util.ArrayList arraylist0 = null;
    java.lang.String string1;
    android.os.Bundle bundle1 = null;
    java.util.ArrayList arraylist1 = null;
    int int6;
    android.content.SharedPreferences sharedpreferences0 = null;
    int int7;
    android.content.SharedPreferences sharedpreferences1 = null;
    int int8;
    int int9;
    int int10;
    int int11;
    notelistitem0 = org.mockito.Mockito.mock(com.farmerbb.notepad.util.NoteListItem[].class);
    ActivityController<MainActivity> activityController = Robolectric.buildActivity(MainActivity.class);
    mainactivity0 = activityController.get();
    activityController.create(bundle0);
    activityController.start();
    activityController.postCreate(bundle1);
    activityController.resume();
    sharedpreferences0 = activity0.getPreferences(0);
    string1 = contextwrapper0.getPackageName();
    sharedpreferences1 = contextwrapper0.getSharedPreferences("com.farmerbb.notepad_preferences",0);
    view2 = activity0.findViewById(2131296386);
    view1 = activity0.findViewById(2131296384);
    int2 = android.support.v4.content.ContextCompat.getColor(mainactivity0,2131099790);
    int1 = android.support.v4.content.ContextCompat.getColor(mainactivity0,2131099790);
    view0 = activity0.findViewById(2131296368);
    file0 = contextwrapper0.getFilesDir();
    arraylist1 = new java.util.ArrayList(0);
    list1 = java.util.Arrays.asList(notelistitem0);
    boolean0 = arraylist1.addAll(list0);
    arrayadapter1 = new android.widget.ArrayAdapter(mainactivity0,2131492932,arraylist1);
    arrayadapter0 = new android.widget.ArrayAdapter(mainactivity0,2131492931,arraylist1);
    arraylist0 = mainactivity0.getCabArray();
  }
}

