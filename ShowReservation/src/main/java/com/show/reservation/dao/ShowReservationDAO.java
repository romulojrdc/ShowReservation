package com.show.reservation.dao;

import java.util.List;

import com.show.reservation.model.ShowReservationModel;

public interface ShowReservationDAO {
	
	ShowReservationModel findById(Integer id);

	List<ShowReservationModel> findByShowId(Integer showId);
	
	List<ShowReservationModel> findByShowIdPhoneNumber(Integer showId, String phoneNumber);

	List<ShowReservationModel> findAll();

	Integer save(ShowReservationModel showReservationModel);

	void update(ShowReservationModel showReservationModel);

	void delete(int id);
}
