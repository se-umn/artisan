import android.widget.EditText;
import android.widget.TextView;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadows.ShadowTextView;

import java.util.HashMap;
import java.util.Map;

@Implements(EditText.class)
public class EditTextShadow extends ShadowTextView {

    private Map<String, EditText> mockedCalls = new HashMap<>();

    public void setMockFor(String methodName, EditText mockedInstance){
        mockedCalls.put(methodName, mockedInstance);
    }

    @Implementation
    public Editable getText(){
        if( this.mockedCalls.containsKey("<android.widget.EditText: android.text.Editable getText()>") ){
            return mockedCalls.get("<android.widget.EditText: android.text.Editable getText()>").getText();
        } else {
            // Forward the call to the underlying real object
            // TODO Not sure we need to declare here @RealObject as well !
            return ((TextView) super.realView).getEditableText();
        }
    }
}
