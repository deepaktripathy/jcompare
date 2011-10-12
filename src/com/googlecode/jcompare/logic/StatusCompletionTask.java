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
package com.googlecode.jcompare.logic;

import com.googlecode.jcompare.model.ElementProvider;
import com.googlecode.jcompare.model.Item;
import com.googlecode.jcompare.model.Item.State;
import com.googlecode.jcompare.model.ItemState;
import com.googlecode.jcompare.model.StockItemStates;
import com.googlecode.jcompare.tasks.AbstractTask;
import com.googlecode.jcompare.tasks.TaskProcessor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tusharjoshi
 */
public class StatusCompletionTask extends AbstractTask {

    private final Item item;
    private final ElementProvider provider;

    public StatusCompletionTask(final TaskProcessor taskProcessor,
            final ElementProvider provider, final Item item) {
        super(taskProcessor);
        this.item = item;
        this.provider = provider;
    }

    @Override
    public void run() {

        if (StockItemStates.STATE_NOTAVAILABLE == item.getLeftState()
                || StockItemStates.STATE_NOTAVAILABLE == item.getRightState()) {
            return;
        }

        System.out.println("\n\nStatus check called for: \n\tL: "
                + item.getLeftPath() + ":" + item.getLeftState() + "\n\tR: "
                + item.getRightPath() + ":" + item.getRightState());

        List<State> leftStatesList = createLeftStatesArray(item);
        List<State> rightStatesList = createRightStatesArray(item);

        ItemState states = provider.getState(leftStatesList, rightStatesList,
                item.getLeftData(), item.getRightData());

        if (null != states) {
            State leftState = states.getLeftState();
            State rightState = states.getRightState();

            if (null != leftState && null != rightState) {
                item.setLeftState(leftState);
                item.setRightState(rightState);
            }
        }

        if (null != item.getParent()) {
            StatusCompletionTask statusCompletionTask =
                    new StatusCompletionTask(taskProcessor,
                    provider, item.getParent());
            if (!isCancelled()) {
                execute(statusCompletionTask);
            }
        }
    }

    private List<State> createLeftStatesArray(Item item) {

        List<State> stateList = new ArrayList<State>();

        int childCount = item.getChildCount();
        for (int i = 0; i < childCount; i++) {
            Item child = item.getChild(i);
            //if (!child.isLeaf()) {
                stateList.add(child.getLeftState());
            //}
        }

        return stateList;
    }

    private List<State> createRightStatesArray(Item item) {

        List<State> stateList = new ArrayList<State>();

        int childCount = item.getChildCount();
        for (int i = 0; i < childCount; i++) {
            Item child = item.getChild(i);
            //if (!child.isLeaf()) {
                stateList.add(child.getRightState());
            //}
        }

        return stateList;
    }
}
