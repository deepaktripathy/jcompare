/*
 *  The MIT License
 * 
 *  Copyright (c) 2011 Tushar Joshi <tusharvjoshi@gmail.com>
 * 
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 * 
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 * 
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package com.googlecode.jcompare.model;

import com.googlecode.jcompare.logic.ItemPopulatorTask;
import com.googlecode.jcompare.model.impl.ItemImpl;
import com.googlecode.jcompare.tasks.Processeable;
import com.googlecode.jcompare.tasks.TaskContext;
import com.googlecode.jcompare.tasks.TaskProcessor;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author tusharjoshi
 */
public final class ItemTree implements Processeable {

    private Item item = null;
    private TaskContext taskContext = null;
    
    private final PropertyChangeSupport propertyChangeSupport
            = new PropertyChangeSupport(this);
    private final ElementProvider provider;
    private final TaskProcessor taskProcessor;

    public ItemTree(String leftPath, String rightPath, TaskProcessor taskProcessor, ElementProvider provider) {
        this.provider = provider;
        this.taskProcessor = taskProcessor;

        item = new ItemImpl();
        item.setLeftPath(leftPath);
        item.setRightPath(rightPath);
        item.setLeftData(provider.getData("", leftPath));
        item.setRightData(provider.getData("", rightPath));
        item.setLeftState(StockItemStates.STATE_UNCHECKED);
        item.setRightState(StockItemStates.STATE_UNCHECKED);
    }

    public void startProcess() {
        ItemPopulatorTask populateTask = new ItemPopulatorTask(taskProcessor, provider, item);
        taskContext = new DefaultTaskContext();
        populateTask.setTaskContext(taskContext);
        taskProcessor.execute(populateTask);
    }

    public void stopProcess() {
        taskContext.setCancelled(true);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}
