package com.sergeykotov.adapter.dao;

import com.sergeykotov.adapter.domain.Rule;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class RuleDao {
    private static final String CREATE_CMD = "insert into rule (name, note, creation_time) values (?, ?, ?);";
    private static final String EXTRACT_CMD =
            "select r.id, r.name, r.note, r.creation_time, r.last_update_time from rule r;";
    private static final String UPDATE_CMD = "update rule set name = ?, note = ?, last_update_time = ? where id = ?";
    private static final String DELETE_CMD = "delete from rule where id = ?";

    public boolean create(Rule rule) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_CMD)) {
            preparedStatement.setString(1, rule.getName());
            preparedStatement.setString(2, rule.getNote());
            preparedStatement.setString(3, rule.getCreationTime().toString());
            return preparedStatement.executeUpdate() == 1;
        }
    }

    public List<Rule> extract() throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EXTRACT_CMD);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Rule> rules = new ArrayList<>();
            while (resultSet.next()) {
                Rule rule = new Rule();
                rule.setId(resultSet.getLong("id"));
                rule.setName(resultSet.getString("name"));
                rule.setNote(resultSet.getString("note"));
                rule.setCreationTime(LocalDateTime.parse(resultSet.getString("creation_time")));
                rule.setLastUpdateTime(LocalDateTime.parse(resultSet.getString("last_update_time")));
                rules.add(rule);
            }
            return Collections.unmodifiableList(rules);
        }
    }

    public boolean update(Rule rule) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CMD)) {
            preparedStatement.setString(1, rule.getName());
            preparedStatement.setString(2, rule.getNote());
            preparedStatement.setString(3, rule.getLastUpdateTime().toString());
            preparedStatement.setLong(4, rule.getId());
            return preparedStatement.executeUpdate() == 1;
        }
    }

    public boolean deleteById(long id) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CMD)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() == 1;
        }
    }
}