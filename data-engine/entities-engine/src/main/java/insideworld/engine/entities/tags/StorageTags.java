package insideworld.engine.entities.tags;

import insideworld.engine.actions.keeper.tags.MultipleTag;
import insideworld.engine.actions.keeper.tags.SingleTag;

public final class StorageTags {

    public static final SingleTag<Long> ID = new SingleTag<>("id");

    public static final MultipleTag<Long> IDS = new MultipleTag<>("ids");

    public static final SingleTag<String> NAME = new SingleTag<>("name");

    public static final SingleTag<Integer> COUNT = new SingleTag<>("count");

}
