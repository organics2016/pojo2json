package ink.organics.pojo2json;

import ink.organics.pojo2json.parser.POJO2JSONParserFactory;

public class EditorPopupMenuDefaultAction extends EditorPopupMenuAction {

    public EditorPopupMenuDefaultAction() {
        super(POJO2JSONParserFactory.DEFAULT_POJO_2_JSON_PARSER);
    }
}
