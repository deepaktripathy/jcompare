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
package com.googlecode.jcompare.model.impl;

import com.googlecode.jcompare.model.ElementProvider;
import com.googlecode.jcompare.model.Item;
import com.googlecode.jcompare.model.Item.State;
import com.googlecode.jcompare.model.ItemState;
import com.googlecode.jcompare.tasks.ItemPopulatorTask;
import com.googlecode.jcompare.tasks.TaskContext;
import com.googlecode.jcompare.tasks.TaskProcessor;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tusharjoshi
 */
public final class ItemTree {

    private Item item = null;
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

    public void populate() {
        ItemPopulatorTask populateTask = new ItemPopulatorTask(taskProcessor, provider, item);
        TaskContext taskContext = new TaskContext() {

            private volatile boolean cancelled = false;

            public boolean isCancelled() {
                return cancelled;
            }

            public void setCancelled(boolean value) {
                this.cancelled = value;
            }
        };
        populateTask.setTaskContext(taskContext);
        taskProcessor.execute(populateTask);
    }

    public static class MyTaskProcessor implements TaskProcessor {

        private ExecutorService threadPool = null;

        public void execute(Runnable task) {
            if (null == threadPool) {
                threadPool = Executors.newFixedThreadPool(1);
            }
            Future<?> taskFuture = threadPool.submit(task);
        }

        public void shutdown() {
            threadPool.shutdown();
            threadPool = null;
        }
        
        public void join()
        {
            try {
                if (null == threadPool) {
                    threadPool.awaitTermination(2, TimeUnit.MINUTES);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(ItemTree.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static class MyElementProvider implements ElementProvider {

        public List<String> getNodeChildren(String path, Object data) {
            File pathFile = new File(path);
            String[] nameList = pathFile.list(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    File newFile = new File(dir + File.separator + name);
                    if (newFile.isDirectory()) {
                        String[] childList = newFile.list();
                        if( childList.length > 0 ) {
                            return true;
                        }
                    } 
                    
                    return false;
                    
                }
            });
            ArrayList<String> list = new ArrayList<String>();
            for (String name : nameList) {
                list.add(name);
            }

            return list;
        }

        public List<String> getLeafChildren(String path, Object data) {
            File pathFile = new File(path);
            String[] nameList = pathFile.list(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    File newFile = new File(dir + File.separator + name);
                    if (newFile.isFile()) {
                        return true;
                    } else {
                        String[] childList = newFile.list();
                        if( childList.length == 0 ) {
                            return true;
                        }
                    }
                    
                    return false;
                }
            });
            ArrayList<String> list = new ArrayList<String>();
            for (String name : nameList) {
                list.add(name);
            }

            return list;
        }

        public Object getData(String path, String key) {
            return new File(getPath(path, key));
        }

        public String getPath(String path, String key) {
            return path + File.separator + key;
        }

        public ItemState getState(List<State> leftStateList, List<State> rightStateList, Object leftData, Object rightData) {
            
            
            ItemState itemState;
            
            if( leftStateList.isEmpty() && rightStateList.isEmpty())
            {
                itemState = getState( leftData.toString(), rightData.toString(), leftData, rightData);
            } else {
                
                itemState = new ItemState();
                itemState.setLeftState(determineStatus(leftStateList));
                itemState.setRightState(determineStatus(rightStateList));
            }
            
            return itemState;
        }
        
        public State determineStatus(List<State> stateList) {
            
            boolean uncheckedStatus = stateExists(stateList, StockItemStates.STATE_UNCHECKED);   
            if( uncheckedStatus ) {
                return StockItemStates.STATE_UNCHECKED;
            }
            
            boolean newStatus = stateExists(stateList, newState);            
            boolean oldStatus = stateExists(stateList, oldState);         
            boolean sameStatus = stateExists(stateList, sameState);
            
            State state;
            if( newStatus && oldStatus) {
                state = newOldState;
            } else {
                if( newStatus) {
                    state = newState;
                } else if ( oldStatus) {
                    state = oldState;
                } else if ( sameStatus) {
                    state = sameState;
                } else {
                    state = StockItemStates.STATE_UNCHECKED;
                }
            }
            
            return state;
        }
        
        public boolean stateExists(List<State> stateList, State state) {
            boolean status = false;
            for( State stateItem : stateList ) {
                if( stateItem == state ) {
                    status =  true;
                }
            }
            
            return status;
        }

        public static class NewState implements Item.State {

            @Override
            public String toString() {
                return "NewState";
            }

        }

        public static class OldState implements Item.State {

            @Override
            public String toString() {
                return "OldState";
            }
            
        }

        public static class NewOldState implements Item.State {

            @Override
            public String toString() {
                return "NewOldState";
            }
            
        }

        public static class SameState implements Item.State {

            @Override
            public String toString() {
                return "SameState";
            }
            
        }

        private final Item.State newState = new NewState();

        private final Item.State oldState = new OldState();

        private final Item.State newOldState = new NewOldState();

        private final Item.State sameState = new SameState();

        public ItemState getState(String leftPath, String rightPath, Object leftData, Object rightData) {
            File leftFile = (File) leftData;
            File rightFile = (File) rightData;
            long leftModified = leftFile.lastModified();
            long rightModified = rightFile.lastModified();
            ItemState itemState = new ItemState();

            if (leftModified > rightModified) {
                itemState.setLeftState(newState);
                itemState.setRightState(oldState);
            } else if (leftModified < rightModified) {
                itemState.setLeftState(oldState);
                itemState.setRightState(newState);
            } else {
                itemState.setLeftState(sameState);
                itemState.setRightState(sameState);
            }
            return itemState;
        }
    }
}
