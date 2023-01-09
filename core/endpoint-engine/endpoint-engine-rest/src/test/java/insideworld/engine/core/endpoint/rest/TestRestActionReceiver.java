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
//package insideworld.engine.core.endpoint.rest;
//
//import insideworld.engine.core.action.keeper.output.Output;
//import insideworld.engine.core.endpoint.rest.RestActionReceiver;
//import insideworld.engine.core.endpoint.rest.RestParameter;
//import java.io.InputStream;
//import javax.inject.Inject;
//import javax.inject.Singleton;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.HttpHeaders;
//
//@Path("/actions")
//@Singleton
//public class TestRestActionReceiver {
//
//    private RestActionReceiver receiver;
//
//    /**
//     * Default constructor.
//     *
//     * @param builder Output task builder.
//     * @param executor Action executor.
//     */
//    @Inject
//    public TestRestActionReceiver(final RestActionReceiver receiver) {
//        this.receiver = receiver;
//    }
//
//    @POST
//    @Path("/{action}")
//    @Consumes("application/json")
//    @Produces("application/json")
//    public Output executeAction(
//        @PathParam("action") final String action,
//        @javax.ws.rs.core.Context final HttpHeaders headers,
//        final InputStream rawbody
//    ) {
//        return this.receiver.execute(action, new RestParameter(headers, rawbody)).result();
//    }
//
////    @POST
////    @Path("/{action}")
////    @Consumes("application/json")
////    @Produces("application/json")
////    public Uni<Output> executeAction(
////        @PathParam("action") final String action,
////        @javax.ws.rs.core.Context final HttpHeaders headers,
////        final InputStream rawbody
////    ) {
////        return Uni.createFrom().emitter(
////            emitter -> this.facade
////                .execute(action, new RestParameter(headers, rawbody))
////                .subscribe(emitter::complete)
////        );
////    }
//
//}
