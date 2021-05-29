import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadows.ShadowTextView;
import org.robolectric.shadow.api.Shadow;

import java.util.HashMap;
import java.util.Map;
import java.lang.CharSequence;

@Implements(TextView.class)
public class TextViewShadow extends ShadowTextView {

    @RealObject
    private TextView realTextView;

    private Map<String, TextView> mockedCalls = new HashMap<>();

    public void setMockFor(String methodName, TextView mockedInstance){
        mockedCalls.put(methodName, mockedInstance);
    }

    @Implementation
    public CharSequence getText() {
        if(this.mockedCalls.containsKey("<android.widget.TextView: java.lang.CharSequence getText()>")){
            return mockedCalls.get("<android.widget.TextView: java.lang.CharSequence getText()>").getText();
        } else {
            return Shadow.directlyOn(realTextView, TextView.class).getText();
        }
    }
}
