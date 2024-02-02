package com.show.reservation.dao;

import java.util.List;

import com.show.reservation.model.ShowModel;

public interface ShowDAO {
	
	ShowModel findById(Integer id);

	List<ShowModel> findAll();

	Integer save(ShowModel showModel);

	void update(ShowModel showModel);

	void delete(Integer id);
}
