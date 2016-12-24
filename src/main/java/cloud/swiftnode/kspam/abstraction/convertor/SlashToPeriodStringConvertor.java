package cloud.swiftnode.kspam.abstraction.convertor;

import cloud.swiftnode.kspam.abstraction.StringConvertor;

/**
 * Created by EntryPoint on 2016-12-24.
 */
public class SlashToPeriodStringConvertor extends StringConvertor {
    public SlashToPeriodStringConvertor(Object obj) {
        super(obj);
    }

    @Override
    public String convert() {
        return obj.toString().replaceAll("/", ".");
    }
}
