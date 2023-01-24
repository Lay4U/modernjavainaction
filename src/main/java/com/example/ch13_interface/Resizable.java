package com.example.ch13_interface;

public interface Resizable extends Drawable{
    int getWidth();
    int getHeight();
    void setWidth(int width);
    void setHeight(int height);
    void setAbsoluteSize(int width, int height);
    default void setRelativeSize(int wFactor, int hFactor){
        setAbsoluteSize(getWidth() / wFactor, getHeight() / hFactor);
    }

//    default boolean removeIf(Predicate<? super E> filter) {
//        boolean removed = false;
//        Iterater<E> each = iterator();
//        while(each.hasNext()) {
//            if(filter.test(each.next())){
//                each.remove();
//                removed = true;
//            }
//        }
//        return removed;
//    }
}
