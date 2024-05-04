package viancis.lab6.common.commands;

import java.io.Serializable;

public enum CommandType implements Serializable {
    WITHOUT_ARGUMENTS(0, false, false,true),
    WITH_FORM(0, true, false,true),
    WITH_ARGUMENTS(1, false, false, true),
    WITH_MANY_ARGUMENTS_FORM(-1, false, false, true),

    WITH_ARGUMENTS_FORM(1, true, false, true),
    WITH_LOGIN(0, false, true, false);

    private final int argumentCount;

    private final boolean needForm;

    private final boolean needLogin;

    private final boolean needAuth;

    CommandType(int argumentCount, boolean needForm, boolean needLogin, boolean needAuth) {
        this.argumentCount = argumentCount;
        this.needForm = needForm;
        this.needLogin = needLogin;
        this.needAuth = needAuth;
    }

    public int getArgs() {
        return argumentCount;
    }

    public boolean isNeedForm() {
        return needForm;
    }

    public boolean isNeedLogin() {
        return needLogin;
    }

    public boolean isNeedAuth() {
        return needAuth;
    }
}
