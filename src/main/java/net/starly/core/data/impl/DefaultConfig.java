package net.starly.core.data.impl;

import java.util.List;

public interface DefaultConfig {
    void setString(String path, String value);
    String getString(String path);

    void setBoolean(String path, boolean value);
    boolean getBoolean(String path);

    void setChar(String path, char value);
    char getChar(String path);

    void setByte(String path, byte value);
    byte getByte(String path);

    void setShort(String path, short value);
    short getShort(String path);

    void setInt(String path, int value);
    int getInt(String path);

    void setLong(String path, long value);
    long getLong(String path);

    void setFloat(String path, float value);
    float getFloat(String path);

    void setDouble(String path, double value);
    double getDouble(String path);

    void setObject(String path, Object value);
    Object getObject(String path);

    void setObjectList(String path, List<Object> value);
    List<Object> getObjectList(String path);

    void setStringList(String path, List<String> value);
    List<String> getStringList(String path);
}
