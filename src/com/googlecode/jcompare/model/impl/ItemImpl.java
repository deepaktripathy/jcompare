/*
 * The MIT License
 * 
 * Copyright (c) 2011 Tushar Joshi <tusharvjoshi@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.googlecode.jcompare.model.impl;

import com.googlecode.jcompare.model.Item;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tushar Joshi <tusharvjoshi@gmail.com>
 */
public class ItemImpl implements Item {

    private Item parentItem = null;
    private State leftState;
    private State rightState;
    private String name;
    private String leftPath;
    private String rightPath;
    private boolean leaf;
    private WeakReference leftData = null;
    private WeakReference rightData = null;
    private PropertyChangeSupport propertyChangeSupport =
            new PropertyChangeSupport(this);
    private List<Item> itemList = new ArrayList<Item>();

    public State getLeftState() {
        return this.leftState;
    }

    public void setLeftState(State state) {
        this.leftState = state;
    }

    public State getRightState() {
        return this.rightState;
    }

    public void setRightState(State state) {
        this.rightState = state;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeftPath() {
        return this.leftPath;
    }

    public void setLeftPath(String path) {
        this.leftPath = path;
    }

    public String getRightPath() {
        return this.rightPath;
    }

    public void setRightPath(String path) {
        this.rightPath = path;
    }

    public boolean isLeaf() {
        return this.leaf;
    }

    public void setLeaf(boolean value) {
        this.leaf = value;
    }

    public int getChildCount() {
        return itemList.size();
    }

    public void addChild(Item childItem) {
        itemList.add( childItem);
    }

    public Item getChild(int index) {
        return itemList.get(index);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public String getStateDescription(State state) {
        return state.toString();
    }

    @Override
    public String toString() {
        return name;
    }

    public String toString2() {
        StringBuilder buffer = new StringBuilder();

        buffer.append("Left Path:" + leftPath);
        buffer.append("\n");
        buffer.append("Right Path:" + rightPath);
        buffer.append("\n");
        buffer.append("Name: " + name);
        buffer.append("\n");
        buffer.append("Left State: " +getStateDescription(leftState));
        buffer.append("\n");
        buffer.append("Right State: " +getStateDescription(rightState));
        buffer.append("\n");
        buffer.append("IsLeaf: "+ leaf);
        buffer.append("\n");

        if( !leaf ) {
            int size = getChildCount();
            buffer.append("\n");
            for( int index = 0 ; index < size; index++ ) {
                Item child = getChild(index);
                buffer.append( ((ItemImpl)child).toString2());
            }
        }
        buffer.append("\n");


        return buffer.toString();
    }

    public void clearChildren() {
        itemList.clear();
    }

    public Object getLeftData() {
        Object data =  null;
        if( null != leftData ) {
            data = leftData.get();
        }
        return data;
    }

    public void setLeftData(Object data) {
        if( null != data ) {
            this.leftData = new WeakReference(data);
        }
    }

    public Object getRightData() {
        Object data =  null;
        if( null != rightData ) {
            data = rightData.get();
        }
        return data;
    }

    public void setRightData(Object data) {
        if( null != data ) {
            this.rightData = new WeakReference(data);
        }
    }

    public Item getParent() {
        return parentItem;
    }

    public void setParent(Item parentItem) {
        this.parentItem = parentItem;
    }
}
