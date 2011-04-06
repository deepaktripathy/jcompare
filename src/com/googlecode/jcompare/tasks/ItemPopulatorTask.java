/*
 *  The MIT License
 * 
 *  Copyright 2010 tusharjoshi.
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
package com.googlecode.jcompare.tasks;

import com.googlecode.jcompare.model.ElementProvider;
import com.googlecode.jcompare.model.Item;
import com.googlecode.jcompare.model.ItemState;
import com.googlecode.jcompare.model.impl.ItemImpl;
import com.googlecode.jcompare.model.impl.StockItemStates;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author tusharjoshi
 */
public class ItemPopulatorTask extends AbstractTask {

    private final Item item;
    private final ElementProvider provider;

    public ItemPopulatorTask(final TaskProcessor taskProcessor, final ElementProvider provider, final Item item) {
        super(taskProcessor);
        this.item = item;
        this.provider = provider;
    }

    @Override
    public void run() {

        String leftPath = item.getLeftPath();
        String rightPath = item.getRightPath();

        // get node children for both sides
        List<String> leftNodeChildren = null;

        if (StockItemStates.STATE_NOTAVAILABLE != item.getLeftState()) {
            leftNodeChildren =
                    provider.getNodeChildren(leftPath,
                    item.getLeftData());
        }
        List<String> rightNodeChildren = null;

        if (StockItemStates.STATE_NOTAVAILABLE != item.getRightState()) {
            rightNodeChildren =
                    provider.getNodeChildren(rightPath,
                    item.getRightData());
        }

        List<String> nodeList = addItems(leftNodeChildren, rightNodeChildren, false);

        List<String> leftLeafChildren = null;

        if (StockItemStates.STATE_NOTAVAILABLE != item.getLeftState()) {
            leftLeafChildren =
                    provider.getLeafChildren(item.getLeftPath(),
                    item.getLeftData());
        }
        List<String> rightLeafChildren = null;

        if (StockItemStates.STATE_NOTAVAILABLE != item.getRightState()) {
            rightLeafChildren =
                    provider.getLeafChildren(item.getRightPath(),
                    item.getRightData());
        }

        List<String> leafList = addItems(leftLeafChildren, rightLeafChildren, true);
        
        Item child;
        
        if( !leafList.isEmpty()) {
            int childCount = item.getChildCount();
            for (int i = 0; i < childCount; i++) {
                child = item.getChild(i);
                if (child.isLeaf()) {
                    if (!isCancelled()) {
                        StatusCompletionTask statusCompletionTask =
                                new StatusCompletionTask(taskProcessor, provider, child);
                        if (!isCancelled()) {
                            execute(statusCompletionTask);
                        }
                    }
                }
            }
        }

        if (0 == nodeList.size()) {

            // TODO need a chain of status tasks till the last parent
            // also add one sequential thread executor
            StatusCompletionTask statusCompletionTask =
                    new StatusCompletionTask(taskProcessor, provider, item);
            if (!isCancelled()) {
                execute(statusCompletionTask);
            }
        } else {
            int childCount = item.getChildCount();
            for (int i = 0; i < childCount; i++) {
                child = item.getChild(i);
                if (!child.isLeaf()) {
                    if (!isCancelled()) {
                        ItemPopulatorTask populator = new ItemPopulatorTask(taskProcessor, provider, child);
                        execute(populator);
                    }
                }
            }
        }

    }

    private List<String> addItems(List<String> leftChildren, List<String> rightChildren, boolean isLeaf) {

        String leftPath = item.getLeftPath();
        String rightPath = item.getRightPath();

        Set<String> nodeSet = new HashSet<String>();

        if (leftChildren != null) {
            for (String fileName : leftChildren) {
                nodeSet.add(fileName);
            }
        }

        if (rightChildren != null) {
            for (String fileName : rightChildren) {
                nodeSet.add(fileName);
            }
        }

        List<String> list = new ArrayList<String>(nodeSet);
        Collections.sort(list);

        for (String key : list) {
            boolean leftAvailable = false;
            if (null != leftChildren) {
                leftAvailable = leftChildren.contains(key);
            }
            boolean rightAvailable = false;
            if (null != rightChildren) {
                rightAvailable = rightChildren.contains(key);
            }

            Item child = new ItemImpl();
            child.setParent(item);

            child.setLeftPath(provider.getPath(leftPath, key));
            child.setRightPath(provider.getPath(rightPath, key));

            Object leftData;
            Object rightData;

            if (leftAvailable && rightAvailable) {
                child.setLeftState(StockItemStates.STATE_UNCHECKED);
                child.setRightState(StockItemStates.STATE_UNCHECKED);
                leftData = provider.getData(leftPath, key);
                rightData = provider.getData(rightPath, key);
                child.setLeftData(leftData);
                child.setRightData(rightData);

                if (isLeaf) {
                    ItemState itemState = provider.getState(leftPath, rightPath, leftData, rightData);
                    child.setLeftState(itemState.getLeftState());
                    child.setRightState(itemState.getRightState());
                    
                    System.out.println("\n\nStatus check called for: \n\tL: "
                        + child.getLeftPath() + ":" + child.getLeftState() + "\n\tR: "
                        + child.getRightPath() + ":" + child.getRightState());
                }
            } else {
                if (leftAvailable) {
                    child.setLeftState(StockItemStates.STATE_ORPHAN);
                    leftData = provider.getData(leftPath, key);
                    child.setLeftData(leftData);

                    child.setRightState(StockItemStates.STATE_NOTAVAILABLE);
                    
                    System.out.println("\n\nStatus check called for: \n\tL: "
                        + child.getLeftPath() + ":" + child.getLeftState() + "\n\tR: "
                        + child.getRightPath() + ":" + child.getRightState());
                } else {
                    child.setLeftState(StockItemStates.STATE_NOTAVAILABLE);

                    child.setRightState(StockItemStates.STATE_ORPHAN);
                    rightData = provider.getData(rightPath, key);
                    child.setRightData(rightData);
                    
                    System.out.println("\n\nStatus check called for: \n\tL: "
                        + child.getLeftPath() + ":" + child.getLeftState() + "\n\tR: "
                        + child.getRightPath() + ":" + child.getRightState());
                }
            }

            child.setLeaf(isLeaf);

            item.addChild(child);
        }

        return list;
    }
}
