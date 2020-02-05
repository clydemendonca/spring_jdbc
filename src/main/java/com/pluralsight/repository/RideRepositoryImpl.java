package com.pluralsight.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.pluralsight.model.Ride;
import com.pluralsight.repository.util.RideRowMapper;

@Repository("rideRepository")
public class RideRepositoryImpl implements RideRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Ride> getRides() {
		List<Ride> rides = jdbcTemplate.query("SELECT * FROM ride", new RideRowMapper());
		return rides;
	}

	@Override
	public Ride createRide(Ride ride) {
		// TODO Auto-generated method stub
//		jdbcTemplate.update("INSERT INTO ride (name, duration) VALUES (?,?);", ride.getName(), ride.getDuration());

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// TODO Auto-generated method stub
				PreparedStatement ps = con.prepareStatement("INSERT INTO ride (name, duration) VALUES (?,?)",
						new String[] { "id" });
				ps.setString(1, ride.getName());
				ps.setInt(2, ride.getDuration());
				return ps;
			}
		}, keyHolder);

		Number id = keyHolder.getKey();

		return getRide(id.intValue());
	}

	private Ride getRide(int id) {
		// TODO Auto-generated method stub
		Ride ride = jdbcTemplate.queryForObject("SELECT * FROM ride WHERE id=?", new RideRowMapper(), id);
		return ride;
	}

}
