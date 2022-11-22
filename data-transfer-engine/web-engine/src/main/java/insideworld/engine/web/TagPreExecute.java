/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.web;

//import insideworld.engine.actions.keeper.context.Context;
//import insideworld.engine.datatransfer.endpoint.PreExecute;
//import insideworld.engine.exception.CommonException;
//import java.util.List;
//import javax.inject.Singleton;
//
//@Singleton
//public class TagPreExecute implements PreExecute<ReceiveParameters> {
//
//    private final List<TagHandler> handlers;
//
//    public TagPreExecute(final List<TagHandler> handlers) {
//        this.handlers = handlers;
//    }
//
//
//    @Override
//    public void preExecute(Context context, ReceiveParameters parameter) throws CommonException {
//        for (final TagHandler handler : this.handlers) {
//            handler.perform(context);
//        }
//    }
//
//    @Override
//    public int order() {
//        return 10_000;
//    }
//}
