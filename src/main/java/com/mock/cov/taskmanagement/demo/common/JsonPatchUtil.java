package com.mock.cov.taskmanagement.demo.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

public class JsonPatchUtil {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	public static <T> T applyPatch(JsonPatch patch, T target, Class<T> clazz) throws JsonPatchException {
		try {
			mapper.registerModule(new JavaTimeModule());
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Optional
			JsonNode targetNode = mapper.convertValue(target, JsonNode.class);
			JsonNode patchedNode = patch.apply(targetNode);
			System.out.println(mapper.treeToValue(patchedNode, clazz));
			return mapper.treeToValue(patchedNode, clazz);
		} catch (Exception e) {
			System.out.println(e.getMessage());
            throw new RuntimeException("Error applying patch", e);
		}
	}
}
