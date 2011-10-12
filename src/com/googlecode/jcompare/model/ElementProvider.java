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

import java.util.List;

/**
 *
 * @author tusharjoshi
 */
public interface ElementProvider {

    public List<String> getNodeChildren(String path, Object data);

    public List<String> getLeafChildren(String path, Object data);

    public Object getData(String path, String key);

    public String getPath(String path, String key);

    public ItemState getState(String leftPath, String rightPath, Object leftData, Object rightData);

    public ItemState getState(List<Item.State> leftStateList,
            List<Item.State> rightStateList, Object leftData, Object rightData);



}
