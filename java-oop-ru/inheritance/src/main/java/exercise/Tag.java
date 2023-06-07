package exercise;

import java.util.Map;

// BEGIN
public class Tag {
    String mainTag;
    Map<String, String> attributes;

    public Tag(String mainTag, Map<String, String> attributes) {
        this.mainTag = mainTag;
        this.attributes = attributes;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("<" + mainTag);
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            builder.append(" ").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
        }
        builder.append(">");
        return builder.toString();
    }
}
// END
