package insideworld.engine.security.common;

import insideworld.engine.security.common.entities.Role;

public class RoleConst implements Role {

    private final String name;

    public RoleConst(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Role getAppend() {
        throw new UnsupportedOperationException("For constant roles this not available.");
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        final boolean result;
        if (obj instanceof Role) {
            result = this.name.equals(((Role) obj).getName());
        } else {
            result = false;
        }
        return result;
    }


    @Override
    public long getId() {
        return 0;
    }
}
