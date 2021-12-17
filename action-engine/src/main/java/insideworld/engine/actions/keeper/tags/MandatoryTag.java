package insideworld.engine.actions.keeper.tags;

/**
 * Mandatory tag which using in clone context.
 *
 * @see insideworld.engine.actions.keeper.context.Context#clone(Tag[])
 *
 * @since 0.3.0
 */
public interface MandatoryTag {

    Tag<?> get();

}
