package com.practice.chat.friendcircle;

import com.practice.chat.dao.FriendCircleDAO;
import com.practice.chat.dbconnector.DBHelper;
import com.practice.chat.utils.ImageUtil;
import javafx.scene.image.ImageView;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendCircleImpl implements FriendCircleDAO {
    @Override
    public List<FriendCircle> findFriendCircleBySenderNumber(String sender) throws SQLException {
        DBHelper helper = DBHelper.getInstance();

        String sql = """
                select *
                from TB_FriendCircle
                where SenderNumber = '%s'
                """;
        sql = sql.formatted(sender);

        ResultSet resultSet = helper.getStatement().executeQuery(sql);
        List<FriendCircle> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new FriendCircle(resultSet.getString(1),
                    resultSet.getBytes(2),
                    resultSet.getString(3),
                    resultSet.getString(4)));
        }

        return list;
    }

    @Override
    public void addFriendCircle(FriendCircle friendCircle) throws SQLException {
        DBHelper helper = DBHelper.getInstance();

        String sql = "insert into TB_FriendCircle values(?,?,?,?)";
        PreparedStatement statement = helper.preparedStatement(sql);

        statement.setString(1, friendCircle.getSenderNumber());
        statement.setBytes(2, friendCircle.getSharedPhoto());
        statement.setString(3, friendCircle.getSharedContentText());
        statement.setString(4, friendCircle.getShareTime());

        statement.execute();
    }
}
