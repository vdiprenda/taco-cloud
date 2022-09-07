package tacos.web;

import org.springframework.beans.factory.annotation.Autowired;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Taco;
import tacos.TacoOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tacos.data.IngredientRepository;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {

    private final IngredientRepository ingredientRepo;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepo){
        this.ingredientRepo = ingredientRepo;
    }

@ModelAttribute
public void addIngredientsToModel(Model model) {
        /*
	List<Ingredient> ingredients = Arrays.asList(
	  new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
	  new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
	  new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
	  new Ingredient("CARN", "Carnitas", Type.PROTEIN),
	  new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
	  new Ingredient("LETC", "Lettuce", Type.VEGGIES),
	  new Ingredient("CHED", "Cheddar", Type.CHEESE),
	  new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
	  new Ingredient("SLSA", "Salsa", Type.SAUCE),
	  new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
	); */
    Iterable<Ingredient> ingredients = ingredientRepo.findAll();
    Type[] types = Ingredient.Type.values();
    for (Type type : types) {
        model.addAttribute(type.toString().toLowerCase(),
                filterByType(ingredients, type));
        }
  }

  @ModelAttribute(name = "tacoOrder")
  public TacoOrder order() {
    return new TacoOrder();
  }

  @ModelAttribute(name = "taco")
  public Taco taco() {
    return new Taco();
  }

  @GetMapping
  public String showDesignForm() {
    int i = 1;

	return "design";
  }


  @PostMapping
  public String processTaco(@Valid Taco taco, Errors errors,
  			@ModelAttribute TacoOrder tacoOrder) {

    if (errors.hasErrors()){
        return "design";
    }
    tacoOrder.addTaco(taco);
    log.info("Processing taco: {}", taco);

    return "redirect:/orders/current";
  }

/*
  @PostMapping
  public String processTaco(Taco taco,
		  // @Valid Taco taco, Errors errors,
		  @ModelAttribute TacoOrder tacoOrder) {

    if (errors.hasErrors()) {
      return "design";
    }

    tacoOrder.addTaco(taco);
    log.info("Processing taco: {}", taco);
    return "redirect:/orders/current";
  }
*/

  private Iterable<Ingredient> filterByType(
      Iterable<Ingredient> ingredients, Type type) {
      return StreamSupport.stream(ingredients.spliterator(), false)
              .filter(i -> i.getType().equals(type))
              .collect(Collectors.toList());
  }
}
