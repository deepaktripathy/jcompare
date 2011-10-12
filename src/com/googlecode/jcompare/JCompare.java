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
package com.googlecode.jcompare;

import com.googlecode.jcompare.filesys.FilesysElementProvider;
import com.googlecode.jcompare.model.ItemTree;
import com.googlecode.jcompare.tasks.MyTaskProcessor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tushar Joshi <tusharvjoshi@gmail.com>
 */
public class JCompare {

    public static void main(String[] args) {

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("Exception:" + e.getMessage());
            }
        });

        MyTaskProcessor taskProcessor = new MyTaskProcessor();

        ItemTree itemTree = new ItemTree("C:/Users/tushar_joshi/Documents/test1", 
                "C:/Users/tushar_joshi/Documents/test2", taskProcessor, 
                new FilesysElementProvider());
        itemTree.populate();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ItemTree.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        taskProcessor.join();

        System.out.println("Done");
        //taskProcessor.shutdown();
    }
}
