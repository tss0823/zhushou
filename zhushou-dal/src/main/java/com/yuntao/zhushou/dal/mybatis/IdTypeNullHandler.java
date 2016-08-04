package com.yuntao.zhushou.dal.mybatis;

import org.apache.ibatis.type.Alias;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 开发测试环境用，id不做任何转换。
 *
 * @author jamesp
 */
@Alias("idHandler")
public final class IdTypeNullHandler extends BaseTypeHandler<String> {
    public static String encode(long l) {
        return Long.toString(l);
    }

    private long decode(String p) {
        return Long.parseLong(p);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setLong(i, decode(parameter));
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        long l = rs.getLong(columnName);
        return encode(l);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        long l = rs.getLong(columnIndex);
        return encode(l);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        long l = cs.getLong(columnIndex);
        return encode(l);
    }

}
