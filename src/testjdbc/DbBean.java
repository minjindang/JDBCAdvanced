package testjdbc;

import com.mysql.jdbc.Driver;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbBean {
	private String jdbcUrl = "jdbc:mysql://localhost:3306/test";
	private String username = "root";
	private String password = "root";

	public void setPassword(String password) {
		this.password = password;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List SelectRS(String sql) {
		List list = new ArrayList();
		Connection conn = null;
		SQLException ex = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		try {
			conn = DriverManager.getConnection(this.jdbcUrl, this.username,
					this.password);
			if (!conn.isClosed()) {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				rsmd = rs.getMetaData();
				while (rs.next()) {
					Map map = new HashMap();
					int columnCount = rsmd.getColumnCount();
					for (int i = 0; i < columnCount; i++) {
						String columnName = rsmd.getColumnName(i + 1);
						map.put(columnName, rs.getObject(i + 1));
					}
					list.add(map);
				}
			}
		} catch (SQLException e) {
			ex = e;

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e1) {
					if (ex == null) {
						ex = e1;
					}
				}
			}
			if (ex != null)
				throw new RuntimeException(ex);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					if (ex == null) {
						ex = e;
					}
				}
			}
			if (ex != null) {
				throw new RuntimeException(ex);
			}
		}
		return list;
	}

	public List SelectRSwhere(String sql, List paraObj) {
		List list = new ArrayList();
		Connection conn = null;
		SQLException ex = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		try {
			conn = DriverManager.getConnection(jdbcUrl, username, password);
			if (!conn.isClosed()) {
				preparedStatement = conn.prepareStatement(sql);
				for (int i = 0; i < paraObj.size(); i++)
					preparedStatement.setObject(i + 1, paraObj.get(i));
				rs = preparedStatement.executeQuery();
				rsmd = rs.getMetaData();
				while (rs.next()) {
					Map map = new HashMap();
					int columnCount = rsmd.getColumnCount();
					for (int i = 0; i < columnCount; i++) {
						String columnName = rsmd.getColumnName(i + 1);
						map.put(columnName, rs.getObject(i + 1));
					}
					list.add(map);
				}
			}
		} catch (SQLException e) {
			ex = e;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					if (ex == null) {
						ex = e;
					}
				}
			}
			if (ex != null) {
				throw new RuntimeException(ex);
			}
		}
		return list;
	}

	public int InsertData(String sql, List paraObj) {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		int result = 0;
		try {
			DriverManager.registerDriver(new Driver());
			conn = DriverManager.getConnection(this.jdbcUrl, this.username,
					this.password);

			preparedStatement = conn.prepareStatement(sql);

			for (int i = 0; i < paraObj.size(); i++)
				preparedStatement.setObject(i + 1, paraObj.get(i));
			result = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("執行SQL\"" + sql + "\"時發生例外：" + e.getMessage());

			if (preparedStatement != null)
				try {
					preparedStatement.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		} finally {
			if (preparedStatement != null)
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return result;
	}
}