package org.uclouvain.visualsearchtree.tree;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class NodeDeserializer implements JsonDeserializer<Tree.Node> {
    /**
     * @param json    The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context
     * @return
     * @throws JsonParseException
     */
    @Override
    public Tree.Node<String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Type listType_child = new TypeToken<Tree.Node<String>>(){}.getType();
        Type listType_edges = new TypeToken<String>(){}.getType();
        JsonObject jsonObject = json.getAsJsonObject();
        List<Tree.Node<String>> children = new LinkedList<>();
        List<String> edges = new LinkedList<>();
        String info = "";
        if (jsonObject.get("info")!= null) {
            info = jsonObject.get("info").getAsString();
        }
        for (JsonElement item : jsonObject.get("children").getAsJsonArray()) {
            children.add(context.deserialize(item, listType_child));
        }
        for (JsonElement item : jsonObject.get("edgeLabels").getAsJsonArray()) {
            edges.add(context.deserialize(item, listType_edges));
        }
        Tree.Node des = new Tree.Node(
                jsonObject.get("nodeId").getAsInt(),
                jsonObject.get("nodePid").getAsInt(),
                jsonObject.get("label").getAsString(),
                children,
                edges,
                checkType(jsonObject.get("type").getAsString()),
                info
                );
        return des;
    }

    public Tree.NodeType checkType(String type){
        switch (type){
            case "SKIP":
                return Tree.NodeType.SKIP;
            case "FAIL":
                return Tree.NodeType.FAIL;
            case "SOLUTION":
                return Tree.NodeType.SOLUTION;
            default:
                return Tree.NodeType.INNER;
        }
    }
}
