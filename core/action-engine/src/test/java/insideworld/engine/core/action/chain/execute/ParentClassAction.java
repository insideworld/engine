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

package insideworld.engine.core.action.chain.execute;

import insideworld.engine.core.action.chain.AbstractChainAction;
import insideworld.engine.core.action.chain.Link;
import insideworld.engine.core.action.chain.LinkException;
import insideworld.engine.core.action.chain.LinksBuilder;
import insideworld.engine.core.action.executor.key.ClassKey;
import java.util.Collection;
import java.util.UUID;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Singleton;

/**
 * Parent action to execute child action without any parameters using class key.
 *
 * @since 0.14.0
 */
@Singleton
class ParentClassAction extends
    AbstractChainAction<UUID, TestAux, TestAux> {

    /**
     * Default constructor.
     *
     * @param builder Links builder instance.
     */
    public ParentClassAction(final LinksBuilder<TestAux> builder) {
        super(builder);
    }

    @Override
    public String key() {
        return "insideworld.engine.core.action.chain.execute.ParentClassAction";
    }

    @Override
    public Class<? extends UUID> inputType() {
        return UUID.class;
    }

    @Override
    public Class<? extends TestAux> outputType() {
        return TestAux.class;
    }

    @Override
    protected Collection<Link<? super TestAux>> attachLinks(final LinksBuilder<TestAux> builder)
        throws LinkException {
        return builder.addLink(
            new TypeLiteral<ExecuteActionLink<UUID, ChildAction.Output, TestAux>>() {},
            link -> link.setInput(
                (aux, factory) -> aux.getUUID()
            ).setOutput(
                (aux, output) -> {
                    aux.setUUID(output.getUUID());
                    aux.setAdditional(output.getAdditional());
                }
            ).setAction(new ClassKey<>(ChildAction.class))
        ).build();
    }

    @Override
    protected TestAux aux(final UUID input) {
        return new TestAux() {

            private UUID uuid = input;

            private String additional;

            @Override
            public UUID getUUID() {
                return this.uuid;
            }

            @Override
            public void setUUID(final UUID uuid) {
                this.uuid = uuid;
            }

            @Override
            public String getAdditional() {
                return this.additional;
            }

            @Override
            public void setAdditional(final String additional) {
                this.additional = additional;
            }
        };
    }

    @Override
    protected TestAux output(final TestAux aux) {
        return aux;
    }


}
