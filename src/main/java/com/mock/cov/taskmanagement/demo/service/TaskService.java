package com.mock.cov.taskmanagement.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.mock.cov.taskmanagement.demo.common.JsonPatchUtil;
import com.mock.cov.taskmanagement.demo.common.TaskStatus;
import com.mock.cov.taskmanagement.demo.entity.Task;
import com.mock.cov.taskmanagement.demo.repository.TaskRepository;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;

	public List<Task> getAllTasks() {
		return taskRepository.findAll();
	}
	
	public Page<Task> getAllTasksPagination(Pageable pageable) {
		 return taskRepository.findAll(pageable);
	}

	public Optional<Task> getTaskById(Long id) {
		return taskRepository.findById(id);
	}

	public Task createTask(Task task) {
		return taskRepository.save(task);
	}

	public Task updateTask(Long id, Task taskDetails) {
		Task task = taskRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
		task.setTitle(taskDetails.getTitle());
		task.setDescription(taskDetails.getDescription());
		task.setCompleted(taskDetails.isCompleted());
		task.setStatus(taskDetails.getStatus() != null ? taskDetails.getStatus() : TaskStatus.IN_PROGRESS);

		return taskRepository.save(task);
	}
	
	public Task patchTaskJson(Long id, JsonPatch patch) {

	    Task task = taskRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

	    try {
	        Task patchedTask = JsonPatchUtil.applyPatch(patch, task, Task.class);
	        System.out.println(patchedTask);
	        return taskRepository.save(patchedTask);
	    } catch (JsonPatchException e) {
	    	throw new IllegalArgumentException("Invalid patch format: " + e.getMessage());
	    }
	}

	public void deleteTask(Long id) {
		if (!taskRepository.existsById(id)) {
			throw new RuntimeException("Task not found with id: " + id);
		}
		taskRepository.deleteById(id);
	}
}
