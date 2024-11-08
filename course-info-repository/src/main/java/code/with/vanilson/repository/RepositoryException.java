package code.with.vanilson.repository;

import java.sql.SQLException;

/**
 * RepositoryException
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-11-08
 */
public class RepositoryException extends RuntimeException {


    public RepositoryException(String message, SQLException e) {
        super(message,e);
    }
}