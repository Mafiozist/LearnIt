package com.learnit.tests;

import com.learnit.database.connection.OfflineDatabaseConnection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class OfflineDatabaseConnectionTest {

    @Test
    void getConnection() throws SQLException {
        Assertions.assertFalse(OfflineDatabaseConnection.getInstance().getConnection().isClosed());
    }
}