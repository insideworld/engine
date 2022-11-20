///*
// * Copyright (c) 2022 Anton Eliseev
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
// * associated documentation files (the "Software"), to deal in the Software without restriction,
// * including without limitation the rights to use, copy, modify, merge, publish, distribute,
// * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
// * is furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in all copies or
// * substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
// * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
// * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
// */
//
//package insideworld.engine.datatransfer.endpoint.send;
//
//import insideworld.engine.actions.Action;
//import insideworld.engine.actions.chain.Link;
//import insideworld.engine.actions.keeper.context.Context;
//import insideworld.engine.actions.keeper.output.Output;
//import insideworld.engine.datatransfer.endpoint.actions.ActionSender;
//import insideworld.engine.exception.CommonException;
//import insideworld.engine.injection.ObjectFactory;
//import javax.enterprise.context.Dependent;
//
//@Dependent
//public class SendLink implements Link {
//
//    private final ObjectFactory factory;
//    private Class<? extends Action> response;
//
//    private ActionSender destination;
//
//    public SendLink(final ObjectFactory factory) {
//
//        this.factory = factory;
//    }
//
//    @Override
//    public void process(final Context context, final Output output) throws CommonException {
//        this.destination.send(output, this.response);
//    }
//
//
//    /**
//     * Add response action.
//     * Don't call it if you don't need to receive response.
//     * @param presponse Action to call on responce.
//     * @return The same instance.
//     */
//    public final SendLink setResponse(Class<? extends Action> presponse) {
//        this.response = presponse;
//        return this;
//    }
//
//    public final SendLink setDestination(final Class<? extends ActionSender> destination) {
//        this.destination = this.factory.createObject(destination);
//        return this;
//    }
//}
