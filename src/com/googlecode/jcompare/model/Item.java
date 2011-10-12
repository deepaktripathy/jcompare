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
package com.googlecode.jcompare.model;

import java.beans.PropertyChangeListener;

/**
 *
 * @author Tushar Joshi <tusharvjoshi@gmail.com>
 */
public interface Item {

    public static interface State {
    }

    public Item getParent();

    public void setParent(Item parentItem);

    public String getStateDescription(State state);

    public State getLeftState();

    public void setLeftState(State state);

    public State getRightState();

    public void setRightState(State state);

    public String getName();

    public void setName(String name);

    public String getLeftPath();

    public void setLeftPath(String path);

    public String getRightPath();

    public void setRightPath(String path);

    public boolean isLeaf();

    public void setLeaf(boolean value);

    public int getChildCount();

    public void clearChildren();

    public void addChild(Item childItem);

    public Item getChild(int index);

    public Object getLeftData();

    public void setLeftData(Object data);

    public Object getRightData();

    public void setRightData(Object data);

    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);
}
