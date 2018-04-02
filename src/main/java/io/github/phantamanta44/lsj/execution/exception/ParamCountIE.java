package io.github.phantamanta44.lsj.execution.exception;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.object.IFunction;

public class ParamCountIE extends InterpretationException {

    public ParamCountIE(IFunction<?, ?> function, int expectedCount, int actualCount) {
        super(String.format("Parameter count error: function %s expected %d found %d",
                function.getName(), expectedCount, actualCount));
    }

    public ParamCountIE(IFunction<?, ?> function, String expected, int actualCount) {
        super(String.format("Parameter count error: function %s expected %s found %d",
                function.getName(), expected, actualCount));
    }

}
