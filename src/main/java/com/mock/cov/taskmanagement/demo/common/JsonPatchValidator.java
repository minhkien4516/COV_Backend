package com.mock.cov.taskmanagement.demo.common;

import com.mock.cov.taskmanagement.demo.common.TaskStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JsonPatchValidator {

    private static final Set<String> ALLOWED_PATHS = Set.of(
        "/title",
        "/description",
        "/completed",
        "/status"
    );

    private static final Set<String> ALLOWED_OPS = Set.of("add", "replace", "remove");

    public void validate(JsonNode patchNode) {
        if (!patchNode.isArray()) {
            throw new IllegalArgumentException("Patch must be a JSON array");
        }

        List<String> errors = new ArrayList<>();

        for (int i = 0; i < patchNode.size(); i++) {
            JsonNode operation = patchNode.get(i);

            if (!operation.isObject()) {
                errors.add("Operation " + i + " is not a valid JSON object");
                continue;
            }

            // Validate "op"
            JsonNode opNode = operation.get("op");
            if (opNode == null || !opNode.isTextual()) {
                errors.add("Operation " + i + ": missing or invalid 'op'");
                continue;
            }
            String opType = opNode.asText();
            if (!ALLOWED_OPS.contains(opType)) {
                errors.add("Operation " + i + ": invalid op '" + opType + "' — allowed: " + ALLOWED_OPS);
            }

            // Validate "path"
            JsonNode pathNode = operation.get("path");
            if (pathNode == null || !pathNode.isTextual()) {
                errors.add("Operation " + i + ": missing or invalid 'path'");
                continue;
            }
            String path = pathNode.asText();
            if (!ALLOWED_PATHS.contains(path)) {
                errors.add("Operation " + i + ": invalid path '" + path + "' — allowed: " + ALLOWED_PATHS);
            }

            // Validate "value" for add/replace
            if ("replace".equals(opType) || "add".equals(opType)) {
                JsonNode valueNode = operation.get("value");
                if (valueNode == null) {
                    errors.add("Operation " + i + ": missing 'value'");
                    continue;
                }
                validateValue(path, valueNode, i, errors);
            }
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errors));
        }
    }

    private void validateValue(String path, JsonNode valueNode, int opIndex, List<String> errors) {
        switch (path) {
            case "/title":
                if (!valueNode.isTextual()) {
                    errors.add("Operation " + opIndex + ": title must be a string");
                } else if (valueNode.asText().length() > 200) {
                    errors.add("Operation " + opIndex + ": title must be under 200 characters");
                }
                break;

            case "/description":
                if (!valueNode.isTextual()) {
                    errors.add("Operation " + opIndex + ": description must be a string");
                } else if (valueNode.asText().length() > 500) {
                    errors.add("Operation " + opIndex + ": description must be under 500 characters");
                }
                break;

            case "/status":
                if (!valueNode.isTextual()) {
                    errors.add("Operation " + opIndex + ": status must be a string");
                } else {
                    try {
                        TaskStatus.valueOf(valueNode.asText());
                    } catch (IllegalArgumentException e) {
                        errors.add("Operation " + opIndex + ": status must be one of: TODO, IN_PROGRESS, DONE");
                    }
                }
                break;

            case "/completed":
                if (!valueNode.isBoolean()) {
                    errors.add("Operation " + opIndex + ": completed must be true or false");
                }
                break;
        }
    }
}