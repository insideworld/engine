package insideworld.engine.actions;

import insideworld.engine.actions.keeper.tags.MultipleTag;
import insideworld.engine.actions.keeper.tags.SingleTag;

public final class ActionsTags {

    private ActionsTags() {
    }

    public static final SingleTag<Action> ACTION = new SingleTag<>("action");

    public static final SingleTag<String> ALIAS = new SingleTag<>("alias");

    public static final SingleTag<Void> EMPTY = new SingleTag<>("empty");

    public static final MultipleTag<Void> EMPTIES = new MultipleTag<>("empty");

    public static final SingleTag<Object> BREAK_CHAIN = new SingleTag<>("breakChain");

    public static final SingleTag<String> BULK = new SingleTag<>("bulk");

    public static final SingleTag<String> FROM_ACTION = new SingleTag<>("action.from-action");

}
