package com.flight.view;

import javafx.beans.property.SimpleObjectProperty;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectiveObjectProperty<T> extends SimpleObjectProperty<Object> {
    private final Class<T> type;

    public ReflectiveObjectProperty(Object bean, String name, Class<T> type) {
        super(bean, name);
        this.type = type;
    }

    private static String capitalize(String string) {
        return string.substring(0, 1)
                .toUpperCase() + string.substring(1);
    }

    @Override
    public void set(Object obj) {
        try {
            Method setter = super.getBean()
                    .getClass()
                    .getMethod("set" + capitalize(super.getName()), type);
            if (obj == null) return;
            setter.invoke(super.getBean(), obj);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Object get() {
        if (super.getBean() == null) return null;

        Method getter;
        try {
            getter = super.getBean()
                    .getClass()
                    .getMethod("get" + capitalize(super.getName()));
        } catch (NoSuchMethodException e) {
            try {
                getter = super.getBean().getClass().getMethod("is" + capitalize(super.getName()));
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }
        }
        try {
            return getter.invoke(super.getBean());
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
