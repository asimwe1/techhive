package com.techhive.Technology.Models;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

public class RangeUserType implements UserType<Range<Double>> {

    @Override
    public int getSqlType() {
        return Types.OTHER; // PostgreSQL range types are handled as OTHER
    }

    @Override
    public Class<Range<Double>> returnedClass() {
        return (Class<Range<Double>>) (Class<?>) Range.class;
    }

    @Override
    public boolean equals(Range<Double> x, Range<Double> y) {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Range<Double> x) {
        return Objects.hashCode(x);
    }

    @Override
    public Range<Double> nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        String rangeStr = rs.getString(position);
        if (rangeStr == null) {
            return null;
        }
        // Parse PostgreSQL range format, e.g., "[6000,8000]"
        String[] bounds = rangeStr.replace("[", "").replace("]", "").split(",");
        Double min = Double.valueOf(bounds[0]);
        Double max = Double.valueOf(bounds[1]);
        return new Range<>(min, max);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Range<Double> value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            String rangeStr = "[" + value.getMin() + "," + value.getMax() + "]";
            st.setObject(index, rangeStr, Types.OTHER);
        }
    }

    @Override
    public Range<Double> deepCopy(Range<Double> value) {
        return value == null ? null : new Range<>(value.getMin(), value.getMax());
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Range<Double> value) {
        return (Serializable) value;
    }

    @Override
    public Range<Double> assemble(Serializable cached, Object owner) {
        return (Range<Double>) cached;
    }
}