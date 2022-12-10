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
//package insideworld.engine.security.core.action;
//
//import insideworld.engine.actions.chain.AbstractChainAction;
//import insideworld.engine.actions.chain.Link;
//import insideworld.engine.actions.chain.LinkException;
//import insideworld.engine.actions.chain.LinksBuilder;
//import insideworld.engine.actions.keeper.links.OutputRemove;
//import insideworld.engine.entities.actions.links.ReadEntityLink;
//import insideworld.engine.entities.converter.ExportEntityLink;
//import insideworld.engine.entities.tags.StorageTags;
//import insideworld.engine.security.common.UserTags;
//import insideworld.engine.security.common.entities.User;
//import insideworld.engine.security.core.UserTags;
//import insideworld.engine.security.core.entities.User;
//import java.util.Collection;
//import javax.enterprise.util.TypeLiteral;
//import javax.inject.Inject;
//
///**
// *
// */
//
//public abstract class AbstractReadUsersAction extends AbstractChainAction implements RoleAction {
//
//    @Inject
//    public AbstractReadUsersAction(final LinksBuilder builder) {
//        super(builder);
//    }
//
//    @Override
//    protected Collection<Link> attachLinks(final LinksBuilder builder) throws LinkException {
//        return builder
//                .addLink(
//                        new TypeLiteral<ReadEntityLink<User>>() {},
//                        link ->
//                            link.setType(User.class).setTags(StorageTags.IDS, UserTags.USERS_EXT))
//                .addLink(ExportEntityLink.class, link -> link.setTag(UserTags.USERS_EXT))
//                .addLink(OutputRemove.class, link -> link.add(UserTags.TOKEN))
//                .build();
//    }
//
//    @Override
//    public String key() {
//        return "users.users.read";
//    }
//}
