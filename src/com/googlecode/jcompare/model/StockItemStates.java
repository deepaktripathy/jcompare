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

/**
 *
 * @author tusharjoshi
 */
public class StockItemStates {

    public static final Item.State STATE_UNCHECKED = UncheckedItemState.getInstance();
    public static final Item.State STATE_NOTAVAILABLE = NotAvailableItemState.getInstance();
    public static final Item.State STATE_ORPHAN = OrphanItemState.getInstance();

    public static class UncheckedItemState implements Item.State {

        private static Item.State INSTANCE = new UncheckedItemState();

        public static Item.State getInstance() {
            return INSTANCE;
        }

        private UncheckedItemState() {
        }

        @Override
        public String toString() {
            return "UncheckedItemState";
        }
    }

    public static class NotAvailableItemState implements Item.State {

        private static Item.State INSTANCE = new NotAvailableItemState();

        public static Item.State getInstance() {
            return INSTANCE;
        }

        private NotAvailableItemState() {
        }

        @Override
        public String toString() {
            return "NotAvailableItemState";
        }
    }

    public static class OrphanItemState implements Item.State {

        private static Item.State INSTANCE = new OrphanItemState();

        public static Item.State getInstance() {
            return INSTANCE;
        }

        private OrphanItemState() {
        }

        @Override
        public String toString() {
            return "OrphanItemState";
        }
    }
}
