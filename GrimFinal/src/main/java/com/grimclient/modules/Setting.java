package com.grimclient.modules;
public class Setting<T> {
    public enum Type{BOOLEAN,INTEGER,FLOAT,ENUM}
    private final String name,description;
    private T value,defaultValue,minValue,maxValue;
    private final Type type;
    public Setting(String name,T def,String desc){
        this.name=name;defaultValue=def;value=def;description=desc;
        if(def instanceof Boolean)type=Type.BOOLEAN;
        else if(def instanceof Integer)type=Type.INTEGER;
        else if(def instanceof Float)type=Type.FLOAT;
        else type=Type.ENUM;
    }
    public Setting(String name,T def,T min,T max,String desc){this(name,def,desc);minValue=min;maxValue=max;}
    public T getValue(){return value;}
    public void setValue(T v){value=v;}
    public String getName(){return name;}
    public String getDescription(){return description;}
    public T getMin(){return minValue;}
    public T getMax(){return maxValue;}
    public Type getType(){return type;}
    public boolean hasRange(){return minValue!=null&&maxValue!=null;}
    public float getPercent(){
        if(!(value instanceof Float)||minValue==null)return 0f;
        float v=(Float)value,mn=(Float)minValue,mx=(Float)maxValue;return(v-mn)/(mx-mn);
    }
    public float getPercentInt(){
        if(!(value instanceof Integer)||minValue==null)return 0f;
        int v=(Integer)value,mn=(Integer)minValue,mx=(Integer)maxValue;return(float)(v-mn)/(mx-mn);
    }
}
