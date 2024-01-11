package edu.dpl.travelroad.controllers;

import edu.dpl.travelroad.models.Place;
import edu.dpl.travelroad.repositories.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final PlaceRepository placeRepository;

    @Autowired
    public HomeController(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("wished", placeRepository.findByVisited(false));
        model.addAttribute("visited", placeRepository.findByVisited(true));
        return "home";  // home.html
    }

    @GetMapping("/visited")
    public String visited(Model model) {
	model.addAttribute("visited", placeRepository.findByVisited(true));
	return "visited"; // visited.html
    }

   @GetMapping("/wished")
   public String wished(Model model) {
	model.addAttribute("wished", placeRepository.findByVisited(false));
	return "wished"; // wished.html
   }
}
