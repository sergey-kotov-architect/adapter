package com.sergeykotov.adapter.dao;

import com.sergeykotov.adapter.task.TaskDto;
import com.sergeykotov.adapter.task.TaskResult;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class TaskResultDao {
    private static final String SAVE_CMD = "insert into task_result " +
            "(start_time, end_time, submission_time, task_name, task_note, succeeded, note) values " +
            "(?, ?, ?, ?, ?, ?, ?);";
    private static final String EXTRACT_CMD = "select t.start_time, t.end_time, t.submission_time, t.task_name, " +
            "t.task_note, t.succeeded, t.note from task_result t;";

    public boolean save(TaskResult taskResult) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_CMD)) {
            preparedStatement.setString(1, taskResult.getStartTime());
            preparedStatement.setString(2, taskResult.getEndTime());
            preparedStatement.setString(3, taskResult.getTask().getSubmissionTime());
            preparedStatement.setString(4, taskResult.getTask().getName());
            preparedStatement.setString(5, taskResult.getTask().getNote());
            preparedStatement.setBoolean(6, taskResult.isSucceeded());
            preparedStatement.setString(7, taskResult.getNote());
            return preparedStatement.executeUpdate() == 1;
        }
    }

    public List<TaskResult> extract() throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EXTRACT_CMD);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<TaskResult> taskResults = new ArrayList<>();
            while (resultSet.next()) {
                TaskDto taskDto = new TaskDto();
                taskDto.setSubmissionTime(resultSet.getString("submission_time"));
                taskDto.setName(resultSet.getString("task_name"));
                taskDto.setNote(resultSet.getString("task_note"));

                TaskResult taskResult = new TaskResult();
                taskResult.setStartTime(resultSet.getString("start_time"));
                taskResult.setEndTime(resultSet.getString("end_time"));
                taskResult.setTask(taskDto);
                taskResult.setSucceeded(resultSet.getBoolean("succeeded"));
                taskResult.setNote(resultSet.getString("note"));
                taskResults.add(taskResult);
            }
            return Collections.unmodifiableList(taskResults);
        }
    }
}