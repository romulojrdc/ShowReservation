package com.show.reservation.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.show.reservation.model.ShowReservationModel;

@Repository
public class InMemoryShowReservationDAO implements ShowReservationDAO {

	private final Map<Integer, ShowReservationModel> showMap = new HashMap<Integer, ShowReservationModel>();
	
	@Override
	public ShowReservationModel findById(Integer id) {
		return showMap.get(id);
	}

	@Override
	public List<ShowReservationModel> findByShowId(Integer showId) {
		return showMap.values().stream().filter(model -> model.getShowId() == showId).collect(Collectors.toList());
	}
	
	@Override
	public List<ShowReservationModel> findByShowIdPhoneNumber(Integer showId, String phoneNumber) {
		return showMap.values().stream()
				.filter(model -> (model.getShowId() == showId && model.getPhoneNumber().equals(phoneNumber)))
				.collect(Collectors.toList());
	}
	
	@Override
	public List<ShowReservationModel> findAll() {
		return (List<ShowReservationModel>) showMap.values();
	}

	@Override
	public Integer save(ShowReservationModel showReservationModel) {
		// generate Next ticket Number.. in DB should be  @GeneratedValue(strategy = GenerationType.IDENTITY)
		Integer tickerNumber = generateTicketNumber();
		showReservationModel.setTicketNumber(tickerNumber);
		showMap.put(tickerNumber, showReservationModel);
		return tickerNumber;
	}

	private Integer generateTicketNumber() {
		if (showMap.isEmpty()) {
			return 1;
		}
		// always add 1 from the latest ticketNumber
		return showMap.keySet().stream().sorted().findFirst().get() + 1;
	}

	@Override
	public void update(ShowReservationModel showReservationModel) {
		showMap.put(showReservationModel.getTicketNumber(), showReservationModel);
	}

	@Override
	public void delete(int id) {
		showMap.remove(id);
	}

}
