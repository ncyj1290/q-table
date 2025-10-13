package com.itwillbs.qtable.controller.reservation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReservationController {

	@GetMapping("reservation")
	public String reservation(
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "personCount", required = false) String personCount,
			@RequestParam(value = "time", required = false) String time,
			Model model) {

		model.addAttribute("reservationDate", date);
		model.addAttribute("personCount", personCount);
		model.addAttribute("reservationTime", time);

		return "reservation/reservation";
	}

}
