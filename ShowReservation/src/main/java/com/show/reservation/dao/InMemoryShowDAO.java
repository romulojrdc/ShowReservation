package com.show.reservation.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.show.reservation.model.ShowModel;

@Repository
public class InMemoryShowDAO implements ShowDAO {
	
	private final Map<Integer, ShowModel> showMap = new HashMap<Integer, ShowModel>();
	
	@Override
	public ShowModel findById(Integer id) {
		return showMap.get(id);
	}

	@Override
	public List<ShowModel> findAll() {
		return (List<ShowModel>) showMap.values();
	}

	@Override
	public Integer save(ShowModel showModel) {
		showMap.put(showModel.getShowId(), showModel);
		return showModel.getShowId();
	}

	@Override
	public void update(ShowModel showModel) {
		showMap.put(showModel.getShowId(), showModel);
	}

	@Override
	public void delete(Integer id) {
		showMap.remove(id);
	}

}
