package com.example.student.BSUIR.HealthyLifestyleBot.Database.Configs;

import com.example.student.BSUIR.HealthyLifestyleBot.Data.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.UserTokenHandler;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.*;
import java.util.ResourceBundle;

@Slf4j
public class DatabaseHandler {

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("application");
    private Connection connection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connection = resourceBundle.getString("spring.datasource.url") + resourceBundle.getString("spring.datasourse.databaseName");
        Class.forName(resourceBundle.getString("spring.datasource.driver-class-name"));

        Connection dbConnection = DriverManager.getConnection(connection, resourceBundle.getString("spring.datasource.username"), resourceBundle.getString("spring.datasource.password"));

        log.info("The connection was successful:" + connection + resourceBundle.getString("spring.datasource.username") + resourceBundle.getString("spring.datasource.password")) ;

        return dbConnection;
    }

    public void addUserData(User user, Message message) throws SQLException, ClassNotFoundException {
        connection = getDbConnection();
        String SQL = "INSERT INTO project_healthy_lifestyle_users (user_id, user_name, user_surname, user_age, user_height, user_weight, user_list_of_disease) VALUE (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);

        log.info("MySQL query is: " + SQL);

        preparedStatement.setLong(1, message.getFrom().getId()); // проверить
        preparedStatement.setString(2, user.getName());
        preparedStatement.setString(3, user.getSurname());
        preparedStatement.setInt(4, user.getAge());
        preparedStatement.setFloat(5,  user.getHeight());
        preparedStatement.setFloat(6, user.getWeight());
        preparedStatement.setString(7,  user.getDisease());

        consoleInformation(user);
        preparedStatement.executeUpdate();
        log.info("We added new user in database");
    }

    public String showUserData(Message message, ResourceBundle localBundle) throws SQLException, ClassNotFoundException {
        connection = getDbConnection();
        String SQL  = "SELECT * FROM project_healthy_lifestyle_users WHERE user_id = ?";
        log.info("MySQL query is: " + SQL);

        PreparedStatement preparedStatement = connection.prepareStatement(SQL);

        preparedStatement.setLong(1, message.getFrom().getId()); // проверить

        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();
        resultSet.getString(2);
        resultSet.getString(3);
        resultSet.getInt(4);
        resultSet.getFloat(5);
        resultSet.getFloat(6);
        resultSet.getString(7);

        User infoUser = new User(resultSet.getString(2),
        resultSet.getString(3),
        resultSet.getInt(4),
        resultSet.getFloat(5),
        resultSet.getFloat(6),
        resultSet.getString(7));
        consoleInformation(infoUser);

        log.info("We show all information user in database");

        return infoUser.showAllInformationAboutUser(localBundle);
    }

    private void consoleInformation(User user){
        System.out.println("===========================================");
        System.out.println("Information about user: ");
        System.out.println(user.getName());
        System.out.println(user.getSurname());
        System.out.println(user.getAge());
        System.out.println(user.getHeight());
        System.out.println(user.getWeight());
        System.out.println(user.getDisease());
        System.out.println("===========================================");
    }

    public void updateName(Object value, String nameData) throws SQLException, ClassNotFoundException {
        connection = getDbConnection();

        String SQL = "UPDATE project_healthy_lifestyle_users SET " + nameData +  " = ? WHERE user_id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        switch (nameData){
            case "user_name":
            case "user_surname":
            case "user_list_of_disease": {
                preparedStatement.setString(1, (String) value);
                break;
            }
            case "user_age": {
                preparedStatement.setInt(1, (Integer) value);
                break;
            }
            case "user_height":
            case "user_weight": {
                preparedStatement.setFloat(1, (Float) value);
                break;
            }
        }

        preparedStatement.setLong(2, 26);
        preparedStatement.executeUpdate();
    }

}

