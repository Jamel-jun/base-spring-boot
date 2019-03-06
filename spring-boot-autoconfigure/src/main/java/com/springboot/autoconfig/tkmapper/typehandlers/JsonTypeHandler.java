package com.springboot.autoconfig.tkmapper.typehandlers;

import com.springboot.common.jackson.JsonUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * for mysql json db type
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(Object.class)
public class JsonTypeHandler extends BaseTypeHandler<Object> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, JsonUtil.toJson(parameter));
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        String jsonString = rs.getString(columnName);
        return JsonUtil.parseJson(jsonString);
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String jsonString = rs.getString(columnIndex);
        return JsonUtil.parseJson(jsonString);
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        String jsonString = cs.getString(columnIndex);
        return JsonUtil.parseJson(jsonString);
    }


}
