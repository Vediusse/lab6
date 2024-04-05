package viancis.lab6.common.commands;

import java.io.Serializable;

public enum CommandType implements Serializable {
    WITHOUT_ARGUMENTS(0, false),
    WITH_FORM(0, true),
    WITH_ARGUMENTS(1, false),
    WITH_MANY_ARGUMENTS_FORM(-1, true),

    WITH_ARGUMENTS_FORM(1, true);

    private final int argumentCount;
    private final boolean needForm;

    CommandType(int argumentCount, boolean needForm) {
        this.argumentCount = argumentCount;
        this.needForm = needForm;
    }

    public int getArgs() {
        return argumentCount;
    }

    public boolean isNeedForm() {
        return needForm;
    }
}
