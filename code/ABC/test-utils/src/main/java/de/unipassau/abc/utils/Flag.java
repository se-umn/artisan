package de.unipassau.abc.utils;

public class Flag {

    private boolean value = false;

    public Flag() {

    }

    public void setValue(boolean value){
        this.value=value;
    }

    public boolean getValue(){
        return value;
    }
}
