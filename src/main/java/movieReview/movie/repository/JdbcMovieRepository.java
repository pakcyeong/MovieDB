package movieReview.movie.repository;

import movieReview.movie.controller.MovieForm;
import movieReview.movie.domain.Movie;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcMovieRepository implements MovieRepository {

    private final DataSource dataSource;

    public JdbcMovieRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Movie save(Movie movie) {
        String sql = "insert into movie(name, dname, actor, rdate, genre) values(?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, movie.getName());
            pstmt.setString(2, movie.getDname());
            pstmt.setString(3, movie.getActor());
            pstmt.setLong(4, movie.getRdate());
            pstmt.setString(5, movie.getGenre());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                movie.setNid(rs.getLong(1));
            } else {
                throw new SQLException("id 조회 실패");
            }
            return movie;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Movie> findbyName(String name) {
        String sql = "select * from movie where name = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                Movie movie = new Movie();
                movie.setNid(rs.getLong("nid"));
                movie.setName(rs.getString("name"));
                return Optional.of(movie);
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public List<Movie> findCond(MovieForm form) {
        String sql = "select * from movie where 1 ";
        if (form.getName()!="")
            sql = sql + "AND name like '%" + form.getName() + "%'";
        if (form.getDname()!="")
            sql = sql + "AND dname like '%" + form.getDname() + "%'";
        if (form.getActor()!="")
            sql = sql + "AND actor like '%" + form.getActor() + "%'";
        if (form.getGenre()!="")
            sql = sql + "AND genre like '%" + form.getGenre() + "%'";


        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            List<Movie> movies = new ArrayList<>();
            while(rs.next()) {
                Movie movie = new Movie();
                movie.setNid(rs.getLong(1));
                movie.setName(rs.getString(2));
                movie.setDname(rs.getString(3));
                movie.setActor(rs.getString(4));
                movie.setRdate(rs.getLong(5));
                movie.setGenre(rs.getString(6));
                movies.add(movie);
            }
            return movies;
        } catch (Exception e){
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }


    @Override
    public List<Movie> findAll() {
        String sql = "select * from movie";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            List<Movie> movies = new ArrayList<>();

            while(rs.next()) {
                Movie movie = new Movie();
                movie.setNid(rs.getLong("id"));
                movie.setName(rs.getString("name"));
                movie.setDname(rs.getString("dname"));
                movie.setActor(rs.getString("actor"));
                movie.setRdate(rs.getLong("rdate"));
                movie.setGenre(rs.getString("genre"));
                movies.add(movie);
            }
            return movies;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }
    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs)
    {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}

