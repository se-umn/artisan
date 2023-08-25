package abc.shadows;

@org.robolectric.annotation.Implements(value = android.widget.TextView.class)
public class ABCTextViewShadow152 extends org.robolectric.shadows.ShadowTextView {

    @org.robolectric.annotation.RealObject()
    private android.widget.TextView realObject;

    private java.util.Map<String,android.widget.TextView> mockedCalls = new java.util.HashMap<String,android.widget.TextView>();

    private boolean strictShadow = false;

    public void setMockFor(java.lang.String methodName, android.widget.TextView mockedInstance) {
        mockedCalls.put(methodName, mockedInstance);
    }

    public void setStrictShadow() {
        strictShadow = true;
    }

    @org.robolectric.annotation.Implementation()
    protected void setText(java.lang.CharSequence charSequence1) {
        if (mockedCalls.containsKey("<android.widget.TextView: void setText(java.lang.CharSequence)>")) {
            mockedCalls.get("<android.widget.TextView: void setText(java.lang.CharSequence)>").setText(charSequence1);
        } else {
            if (strictShadow) {
                throw new java.lang.RuntimeException("Should not have executed this method.");
            } else {
                org.robolectric.shadow.api.Shadow.directlyOn(realObject, android.widget.TextView.class).setText(charSequence1);
            }
        }
    }
}
