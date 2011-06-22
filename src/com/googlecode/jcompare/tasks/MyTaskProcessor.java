/*
 * The MIT License
 *
 * Copyright 2011 Tushar Joshi <tusharvjoshi@gmail.com>.
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
package com.googlecode.jcompare.tasks;

import com.googlecode.jcompare.model.ItemTree;
import com.googlecode.jcompare.tasks.TaskProcessor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tushar Joshi <tusharvjoshi@gmail.com>
 */
public class MyTaskProcessor implements TaskProcessor {
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

    public void join() {
        try {
            if (null == threadPool) {
                threadPool.awaitTermination(2, TimeUnit.MINUTES);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ItemTree.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
