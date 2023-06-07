package exercise;

import java.util.Map;
import java.util.List;

// BEGIN
public class PairedTag extends Tag {
    String body;
    List<Tag> children;

    public PairedTag(String mainTag, Map<String, String> attributes, String body, List<Tag> children) {
        super(mainTag, attributes);
        this.body = body;
        this.children = children;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(super.toString());
        if (children != null) {
            for (Tag tag : children) {
                builder.append(tag.toString());
            }
        }
        builder.append(body);
        builder.append("</").append(mainTag).append(">");
        return builder.toString();
    }
}
// END
